package org.jasperdev.scaffold.api;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jasperdev.scaffold.annotations.*;
import org.jasperdev.scaffold.annotations.Command;
import org.jasperdev.scaffold.annotations.ExecutableBy.SenderType;
import org.jasperdev.scaffold.models.ArgumentData;
import org.jasperdev.scaffold.models.ArgumentData.ChoicesProvider;
import org.jasperdev.scaffold.models.CommandContext;
import org.jasperdev.scaffold.models.Config;
import org.jasperdev.scaffold.tree.CommandNode;
import org.jasperdev.scaffold.tree.HelpGenerator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.jasperdev.scaffold.models.ArgumentData.inferType;

public final class ScaffoldCommandManager implements CommandExecutor, TabCompleter {
	private final JavaPlugin plugin;
	private Config config;
	private final Map<String, CommandNode> commands = new HashMap<>();

	public ScaffoldCommandManager(@Nonnull JavaPlugin plugin){
		this.plugin = plugin;
		this.config = new Config(plugin);
	}

	public ScaffoldCommandManager setConfig(Config config){
		this.config = config;
		return this;
	}

	public void registerCommand(@Nonnull CommandBase command){
		String commandName = command.getClass().getSimpleName();
		try {
			if(!Modifier.isPublic(command.getClass().getModifiers())){
				throw new IllegalArgumentException(
						commandName + " must be public to be registered as a command."
				);
			}

			CommandNode root = buildCommandTree(command);
			commandName = root.getName();
			Permission classPermission = command.getClass().getAnnotation(Permission.class);

			if(config.canAutoInjectHelp()){
				CommandNode helpNode = new CommandNode("help", "Usage for command").setExecutor(ctx ->
						ctx.sender().sendMessage(HelpGenerator.generate(root))
				);
				root.addChild(helpNode);
			}

			registerBukkitCommand(root, classPermission);
			commands.put(root.getName(), root);
		} catch (Exception e) {
			plugin.getLogger().severe("Failed to register command '" + commandName + "': " + e.getMessage());
			config.getExceptionHandler().handle(null, e);
		}
	}

	private CommandNode buildCommandTree(@Nonnull CommandBase instance){
		Command command = instance.getClass().getAnnotation(Command.class);
		CommandNode root = new CommandNode(command.value(), command.description());
		boolean rootRegistered = false;

		for(Method method : instance.getClass().getDeclaredMethods()){
			boolean isRoot = method.isAnnotationPresent(Root.class);
			boolean isSub = method.isAnnotationPresent(Sub.class);
			if(!isRoot && !isSub) continue;
			method.setAccessible(true);

			if(isRoot){
				if(rootRegistered){
					throw new IllegalArgumentException(
							"Only one @Root method is allowed per command class, found multiple in "
									+ instance.getClass().getSimpleName()
					);
				}
				rootRegistered = true;
			}

			Sub sub = method.getAnnotation(Sub.class);
			CommandNode current = root;

			if(sub != null){
				String[] parts = sub.value().split(" ");
				for(int i = 0; i < parts.length; i++){
					String part = parts[i];
					boolean isLast = i == parts.length - 1;

					CommandNode existing = current.getChild(part);
					if(existing != null){
						current = existing;
					} else {
						CommandNode newNode = new CommandNode(part, isLast ? sub.description() : "");
						current.addChild(newNode);
						current = newNode;
					}
				}
			}

			// build arg nodes — optional args must be trailing
			Map<String, ChoicesProvider> choices = instance.choices();
			CommandNode firstOptionalParent = null;
			boolean seenOptional = false;
			for(Parameter param : method.getParameters()){
				if(!param.isAnnotationPresent(Arg.class)) continue;

				Arg arg = param.getAnnotation(Arg.class);
				if(seenOptional && !arg.optional()){
					throw new IllegalArgumentException(
							"Required argument '" + arg.value() + "' cannot follow optional arguments in @Sub(\"" + sub.value() + "\")"
					);
				}
				if(arg.optional() && !param.isAnnotationPresent(Nullable.class)){
					throw new IllegalArgumentException(
							"Optional argument '" + arg.value() + "' must be annotated @Nullable in @Sub(\"" + sub.value() + "\")"
					);
				}
				seenOptional = arg.optional();

				ArgumentData option;

				if(choices.containsKey(arg.value())){
					ChoicesProvider provider = choices.get(arg.value());
					option = new ArgumentData(arg.value(), arg.value(), provider);
				} else {
					option = new ArgumentData(arg.value(), arg.value(), inferType(param.getType()));
				}
				option.setOptional(arg.optional());

				if(arg.optional() && firstOptionalParent == null){
					firstOptionalParent = current;
				}

				CommandNode argNode = new CommandNode(option);
				current.addChild(argNode);
				current = argNode;
			}

			String permission = resolveAnnotation(
					method.getAnnotation(Permission.class),
					instance.getClass().getAnnotation(Permission.class),
					Permission::value
			);
			SenderType senderType = resolveAnnotation(
					method.getAnnotation(ExecutableBy.class),
					instance.getClass().getAnnotation(ExecutableBy.class),
					ExecutableBy::value,
					SenderType.ALL
			);

			CommandNode.CommandExecutor exec = ctx -> {
				if(senderType == SenderType.PLAYER && !(ctx.sender() instanceof Player)){
					ctx.sender().sendMessage(config.formatError(config.getSenderNotPlayerMessage()));
					return;
				}
				if(senderType == SenderType.CONSOLE && ctx.sender() instanceof Player){
					ctx.sender().sendMessage(config.formatError(config.getSenderNotConsoleMessage()));
					return;
				}
				if(permission != null && !ctx.sender().hasPermission(permission)){
					ctx.sender().sendMessage(config.formatError(config.getNoPermissionMessage()));
					return;
				}
				try {
					method.invoke(instance, buildArgs(method, ctx));
				} catch (Exception e) {
					config.getExceptionHandler().handle(ctx.sender(), e);
				}
			};

			current.setExecutor(exec);
			if(firstOptionalParent != null){
				firstOptionalParent.setExecutor(exec);
			}
		}

		return root;
	}

	@Override
	public boolean onCommand(@Nonnull CommandSender sender, @Nonnull org.bukkit.command.Command command, @Nonnull String label, @Nonnull String[] args){
		CommandNode currentNode = commands.get(command.getName().toLowerCase());
		if(currentNode == null) return false;

		Map<String, Object> collectedArgs = new HashMap<>();

		try {
			for(String currentInput : args){
				CommandNode nextNode = findMatchingChild(currentNode, currentInput);

				if(nextNode == null){
					sender.sendMessage(config.formatError(config.parse(config.getUnknownSubcommandMessage(), currentInput)));
					return true;
				}

				currentNode = nextNode;

				// If the node is an option, parse and store the value
				if(currentNode.getType() != null){
					Object parsedValue = parseArgument(currentNode, currentInput);
					collectedArgs.put(currentNode.getName(), parsedValue);
				}
			}

			if(currentNode.getExecutor() != null){
				CommandContext context = new CommandContext(sender, command, label, collectedArgs);
				currentNode.getExecutor().execute(context);
			} else {
				sender.sendMessage(config.formatError(config.getIncompleteCommandMessage()));
			}

		} catch (IllegalArgumentException e) {
			sender.sendMessage(config.formatError(e.getMessage()));
		}

		return true;
	}

	@Override
	public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull org.bukkit.command.Command command, @Nonnull String label, @Nonnull String[] args){
		CommandNode currentNode = commands.get(command.getName().toLowerCase());
		if(currentNode == null) return null;

		// Walk to the current typing level
		for(int i = 0; i < args.length - 1; i++){
			currentNode = findMatchingChild(currentNode, args[i]);
			if(currentNode == null) return Collections.emptyList();
		}

		String lastArg = args[args.length - 1].toLowerCase();

		return currentNode.getChildren().stream()
				.flatMap(child -> {
					ArgumentData.ArgumentType type = child.getType();

					// Literal Subcommands (type is null)
					if(type == null){
						return Stream.of(child.getName());
					}

					// Handle specific argument types
					return switch(type){
						case CHOICE -> {
							ArgumentData data = child.getArgumentData();
							yield (data != null && data.getChoices() != null)
									? data.getChoices().stream()
									: Stream.empty();
						}
						case BOOLEAN -> Stream.of("true", "false");
						case PLAYER -> Bukkit.getOnlinePlayers().stream()
								.map(Player::getName);
						default -> Stream.empty();
					};
				})
				.filter(name -> name.toLowerCase().startsWith(lastArg))
				.collect(Collectors.toList());
	}

	private void registerBukkitCommand(CommandNode root, Permission permission) throws Exception{
		Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
		constructor.setAccessible(true);

		PluginCommand pluginCommand = constructor.newInstance(root.getName(), plugin);
		pluginCommand.setExecutor(this);
		pluginCommand.setTabCompleter(this);
		pluginCommand.setDescription(root.getDescription());
		pluginCommand.setUsage("/" + root.getName() + " help");

		if(permission != null){
			pluginCommand.setPermission(permission.value());
		}

		Field commandMapField = SimplePluginManager.class.getDeclaredField("commandMap");
		commandMapField.setAccessible(true);
		CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getPluginManager());

		commandMap.register(plugin.getName(), pluginCommand);
	}

	private Object[] buildArgs(Method method, CommandContext ctx){
		List<Object> args = new ArrayList<>();
		for(Parameter param : method.getParameters()){
			if(param.getType() == CommandContext.class){
				args.add(ctx);
			} else if(param.isAnnotationPresent(Arg.class)){
				Arg arg = param.getAnnotation(Arg.class);
				Object val = ctx.args().get(arg.value().toLowerCase());
				if(val == null && !arg.optional()){
					throw new NoSuchElementException("Argument '" + arg.value() + "' missing");
				}
				args.add(val);
			}
		}
		return args.toArray();
	}

	private CommandNode findMatchingChild(CommandNode parent, String input){
		for(CommandNode child : parent.getChildren()){
			// Literal match
			if(child.getType() == null && child.getName().equalsIgnoreCase(input)){
				return child;
			}
			// Argument match (matches any input, validation happens in parseArgument)
			if(child.getType() != null){
				return child;
			}
		}
		return null;
	}

	private Object parseArgument(CommandNode node, String input) throws IllegalArgumentException{
		ArgumentData.ArgumentType type = node.getType();
		if(type == null) return input;

		try {
			return switch(type){
				case CHOICE -> {
					List<String> allowed = (node.getArgumentData() != null) ? node.getArgumentData().getChoices() : null;
					if(allowed == null || allowed.stream().noneMatch(s -> s.equalsIgnoreCase(input))){
						throw new IllegalArgumentException(config.parse(config.getUnknownOptionMessage(), allowed.toString()));
					}
					yield input.toLowerCase();
				}
				case INTEGER -> Integer.parseInt(input);
				case DOUBLE -> Double.parseDouble(input);
				case FLOAT -> Float.parseFloat(input);
				case BOOLEAN -> Boolean.parseBoolean(input);
				case PLAYER -> {
					Player player = Bukkit.getPlayerExact(input);
					if(player == null) throw new IllegalArgumentException("Player '" + input + "' not found.");
					yield player;
				}
				case OFFLINE_PLAYER -> Arrays.stream(Bukkit.getOfflinePlayers())
						.filter(p -> p.getName() != null && p.getName().equalsIgnoreCase(input))
						.findFirst()
						.orElseThrow(() -> new IllegalArgumentException("Player '" + input + "' not found."));
				case STRING -> input;
			};
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("'" + input + "' is not a valid " + type.name().toLowerCase());
		}
	}

	@Nullable
	private <A extends Annotation, V> V resolveAnnotation(A method, A clazz, Function<A, V> extractor){
		return resolveAnnotation(method, clazz, extractor, null);
	}

	private <A extends Annotation, V> V resolveAnnotation(A method, A clazz, Function<A, V> extractor, V defaultValue){
		if(method != null) return extractor.apply(method);
		if(clazz != null) return extractor.apply(clazz);
		return defaultValue;
	}
}