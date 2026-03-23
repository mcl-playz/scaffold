package org.jasperdev.mcommandframework.api;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jasperdev.mcommandframework.annotations.Arg;
import org.jasperdev.mcommandframework.annotations.Command;
import org.jasperdev.mcommandframework.annotations.Permission;
import org.jasperdev.mcommandframework.annotations.Sub;
import org.jasperdev.mcommandframework.tree.MCmdNode;
import org.jasperdev.mcommandframework.models.MCommandContext;
import org.jasperdev.mcommandframework.models.OptionData;
import org.jasperdev.mcommandframework.models.OptionData.ChoicesProvider;

import javax.annotation.Nonnull;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.jasperdev.mcommandframework.models.OptionData.inferType;

public class MCommandManager implements CommandExecutor, TabCompleter {
	private final JavaPlugin plugin;
	private final Map<String, MCmdNode> commands = new HashMap<>();

	public MCommandManager(@Nonnull JavaPlugin plugin){
		this.plugin = plugin;
	}

	private Object[] buildArgs(Method method, MCommandContext ctx) {
		List<Object> args = new ArrayList<>();
		for (Parameter param : method.getParameters()) {
			if (param.getType() == MCommandContext.class) {
				args.add(ctx);
			} else if (param.isAnnotationPresent(Arg.class)) {
				Arg arg = param.getAnnotation(Arg.class);
				args.add(ctx.getArg(arg.value(), param.getType()));
			}
		}
		return args.toArray();
	}

	public MCmdNode buildCommandTree(@Nonnull MCommand instance){
		Command command = instance.getClass().getAnnotation(Command.class);
		MCmdNode root = new MCmdNode(command.value(), command.description());

		for (Method method : instance.getClass().getDeclaredMethods()) {
			if (!method.isAnnotationPresent(Sub.class)) continue;
			method.setAccessible(true);

			Sub sub = method.getAnnotation(Sub.class);
			String[] parts = sub.value().split(" ");

			// build path nodes
			MCmdNode current = root;
			for (int i = 0; i < parts.length; i++) {
				String part = parts[i];
				boolean isLast = i == parts.length - 1;

				MCmdNode existing = current.getChild(part);
				if (existing != null) {
					current = existing;
				} else {
					MCmdNode newNode = new MCmdNode(part, isLast ? sub.description() : "");
					current.addChild(newNode);
					current = newNode;
				}
			}

			// build arg nodes
			Map<String, ChoicesProvider> choices = instance.choices();
			for (Parameter param : method.getParameters()) {
				if (!param.isAnnotationPresent(Arg.class)) continue;

				Arg arg = param.getAnnotation(Arg.class);
				OptionData option;

				if (choices.containsKey(arg.value())) {
					ChoicesProvider provider = choices.get(arg.value());
					option = new OptionData(arg.value(), arg.value(), provider);
				} else {
					option = new OptionData(arg.value(), arg.value(), inferType(param.getType()));
				}

				MCmdNode argNode = new MCmdNode(option);
				current.addChild(argNode);
				current = argNode;
			}

			final MCmdNode leaf = current;
			Permission classPermission = instance.getClass().getAnnotation(Permission.class);
			Permission methodPermission = method.getAnnotation(Permission.class);
			String permission = methodPermission != null ? methodPermission.value()
					: classPermission != null ? classPermission.value()
					: null;

			leaf.setExecutor(ctx -> {
				if (permission != null && !ctx.sender().hasPermission(permission)) {
					ctx.sender().sendMessage("§cYou don't have permission to do that.");
					return;
				}
				try {
					method.invoke(instance, buildArgs(method, ctx));
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}

		return root;
	}

	public void registerCommand(@Nonnull MCommand command){
		if (!Modifier.isPublic(command.getClass().getModifiers())) {
			throw new IllegalArgumentException(
					command.getClass().getSimpleName() + " must be public to be registered as a command."
			);
		}

		MCmdNode root = buildCommandTree(command);
		Permission classPermission = command.getClass().getAnnotation(Permission.class);

		// Register Command with Bukkit API
		try{
			Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
			constructor.setAccessible(true);

			PluginCommand pluginCommand = constructor.newInstance(root.getName(), plugin);
			pluginCommand.setExecutor(this);
			pluginCommand.setTabCompleter(this);

			if (classPermission != null) {
				pluginCommand.setPermission(classPermission.value());
			}

			Field commandMapField = SimplePluginManager.class.getDeclaredField("commandMap");
			commandMapField.setAccessible(true);
			CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getPluginManager());

			commandMap.register(plugin.getName(), pluginCommand);
			commands.put(root.getName(), root);
		} catch (Exception e){
			plugin.getLogger().severe("Failed to register command: " + root.getName());
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCommand(@Nonnull CommandSender sender, @Nonnull org.bukkit.command.Command command, @Nonnull String label, @Nonnull String[] args){
		MCmdNode currentNode = commands.get(command.getName().toLowerCase());
		if(currentNode == null) return false;

		Map<String, Object> collectedArgs = new HashMap<>();

		try{
			for(String currentInput : args){
				MCmdNode nextNode = findMatchingChild(currentNode, currentInput);

				if(nextNode == null){
					sender.sendMessage("§cUnknown argument: " + currentInput);
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
				MCommandContext context = new MCommandContext(sender, command, label, collectedArgs);
				currentNode.getExecutor().execute(context);
			} else {
				sender.sendMessage("§cIncomplete command usage.");
			}

		} catch (IllegalArgumentException e){
			sender.sendMessage("§cError: " + e.getMessage());
		}

		return true;
	}

	@Override
	public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull org.bukkit.command.Command command, @Nonnull String label, @Nonnull String[] args){
		MCmdNode currentNode = commands.get(command.getName().toLowerCase());
		if(currentNode == null) return null;

		// Walk to the current typing level
		for(int i = 0; i < args.length - 1; i++){
			currentNode = findMatchingChild(currentNode, args[i]);
			if(currentNode == null) return Collections.emptyList();
		}

		String lastArg = args[args.length - 1].toLowerCase();

		return currentNode.getChildren().stream()
				.flatMap(child -> {
					OptionData.OptionType type = child.getType();

					// Literal Subcommands (type is null)
					if(type == null){
						return Stream.of(child.getName());
					}

					// Handle specific argument types
					return switch(type) {
						case CHOICE -> {
							OptionData data = child.getOptionData();
							yield (data != null && data.getChoices() != null)
									? data.getChoices().stream()
									: Stream.empty();
						}
						case PLAYER -> Bukkit.getOnlinePlayers().stream()
								.map(Player::getName);
						default -> Stream.empty();
					};
				})
				.filter(name -> name.toLowerCase().startsWith(lastArg))
				.collect(Collectors.toList());
	}

	private MCmdNode findMatchingChild(MCmdNode parent, String input){
		for(MCmdNode child : parent.getChildren()){
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

	private Object parseArgument(MCmdNode node, String input) throws IllegalArgumentException{
		OptionData.OptionType type = node.getType();
		if(type == null) return input;

		try{
			return switch(type) {
				case CHOICE -> {
					List<String> allowed = (node.getOptionData() != null) ? node.getOptionData().getChoices() : null;
					if(allowed == null || allowed.stream().noneMatch(s -> s.equalsIgnoreCase(input))){
						throw new IllegalArgumentException("Invalid choice.");
					}
					yield input.toLowerCase();
				}
				case INTEGER -> Integer.parseInt(input);
				case DOUBLE -> Double.parseDouble(input);
				case FLOAT -> Float.parseFloat(input);
				case PLAYER -> {
					Player player = Bukkit.getPlayerExact(input);
					if(player == null) throw new IllegalArgumentException("Player '" + input + "' not found.");
					yield player;
				}
				case STRING -> input;
			};
		} catch (NumberFormatException e){
			throw new IllegalArgumentException("'" + input + "' is not a valid " + type.name().toLowerCase());
		}
	}
}