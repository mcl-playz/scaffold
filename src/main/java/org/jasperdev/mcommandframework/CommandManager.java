package org.jasperdev.mcommandframework;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommandManager implements CommandExecutor, TabCompleter {
    private final JavaPlugin plugin;
    private final List<MCommand> commands;

    public CommandManager(@Nonnull JavaPlugin plugin) {
        this.plugin = plugin;
        this.commands = new ArrayList<>();
    }

    public void registerCommand(@Nonnull MCommand mCommand) {
        commands.add(mCommand);

        try {
            Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class, org.bukkit.plugin.Plugin.class);
            constructor.setAccessible(true);

            PluginCommand pluginCommand = constructor.newInstance(mCommand.getName(), plugin);
            pluginCommand.setExecutor(this);
            pluginCommand.setTabCompleter(this);

            if(mCommand.getDescription() != null) pluginCommand.setDescription(mCommand.getDescription());
            if(mCommand.getPermission() != null) pluginCommand.setPermission(plugin.getName().toLowerCase() + "." + mCommand.getPermission());
            pluginCommand.setUsage("/" + mCommand.getName() + " <subcommands>");

            Field commandMapField = SimplePluginManager.class.getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getPluginManager());

            commandMap.register(plugin.getName(), pluginCommand);
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to register command: " + mCommand.getName());
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        MCommand mCommand = findCommand(command.getName());
        if (mCommand == null) {
            return false;
        }

        List<SubcommandData> subcommands = mCommand.getSubcommands();

        if (subcommands == null || subcommands.isEmpty() || args.length == 0) {
            mCommand.execute(new SubcommandContext(sender, command, label, args));
            return true;
        }

        String subcommandName = args[0];
        for (SubcommandData subcommand : subcommands) {
            if (subcommand.name.equalsIgnoreCase(subcommandName)) {
                ArrayList<String> argsAltered = new ArrayList<>(Arrays.asList(args));
                argsAltered.removeFirst();
                SubcommandContext context = new SubcommandContext(sender, command, label, argsAltered.toArray(new String[0]));

                subcommand.getExecFunc().apply(context);
                return true;
            }
        }

        sender.sendMessage("Unknown subcommand: " + subcommandName);
        return true;
    }

    @Override
    @Nullable
    public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        MCommand mCommand = findCommand(command.getName());
        if (mCommand == null) {
            return null;
        }

        List<SubcommandData> subcommands = mCommand.getSubcommands();
        if (subcommands == null || subcommands.isEmpty()) {
            return null;
        }

        if (args.length == 1) {
            return subcommands.stream()
                    .map(sub -> sub.name)
                    .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }

        return null;
    }

    private MCommand findCommand(String name) {
        for (MCommand cmd : commands) {
            if (cmd.getName().equalsIgnoreCase(name)) {
                return cmd;
            }
        }
        return null;
    }
}