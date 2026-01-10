package org.jasperdev.mcommandframework;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jasperdev.mcommandframework.api.MCommand;
import org.jasperdev.mcommandframework.tree.MCmdNode;
import org.jasperdev.mcommandframework.models.CommandContext;
import org.jasperdev.mcommandframework.models.OptionData;

import javax.annotation.Nonnull;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MCommandManager implements CommandExecutor, TabCompleter {
	private final JavaPlugin plugin;
	private final Map<String, MCmdNode> commandNodes = new HashMap<>();

	public MCommandManager(@Nonnull JavaPlugin plugin){
		this.plugin = plugin;
	}

	public void registerCommand(@Nonnull MCommand mCommand){
		MCmdNode root = mCommand.setup();
		String name = root.getName().toLowerCase();
		commandNodes.put(name, root);

		try{
			Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class, org.bukkit.plugin.Plugin.class);
			constructor.setAccessible(true);

			PluginCommand pluginCommand = constructor.newInstance(name, plugin);
			pluginCommand.setExecutor(this);
			pluginCommand.setTabCompleter(this);

			Field commandMapField = SimplePluginManager.class.getDeclaredField("commandMap");
			commandMapField.setAccessible(true);
			CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getPluginManager());

			commandMap.register(plugin.getName(), pluginCommand);
		} catch (Exception e){
			plugin.getLogger().severe("Failed to register command: " + name);
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args){
		MCmdNode currentNode = commandNodes.get(command.getName().toLowerCase());
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
				CommandContext context = new CommandContext(sender, command, label, collectedArgs);
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
	public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args){
		MCmdNode currentNode = commandNodes.get(command.getName().toLowerCase());
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
								.map(org.bukkit.entity.Player::getName);

						// For INTEGER, FLOAT, STRING, etc., return nothing
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
					if(allowed == null || !allowed.contains(input.toLowerCase())){
						throw new IllegalArgumentException("Invalid choice. Use: " + (allowed != null ? allowed : "[]"));
					}
					yield input.toLowerCase();
				}
				case INTEGER -> Integer.parseInt(input);
				case FLOAT -> Float.parseFloat(input);
				case PLAYER -> {
					org.bukkit.entity.Player player = Bukkit.getPlayerExact(input);
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