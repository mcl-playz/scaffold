package org.jasperdev.mcommandframework;

import org.bukkit.plugin.java.JavaPlugin;
import org.jasperdev.mcommandframework.commands.MCFCommand;

public final class MCommandFramework extends JavaPlugin {
    @Override
    public void onEnable() {
        MCommandManager commandManager = new MCommandManager(this);

        commandManager.registerCommand(new MCFCommand());

        getLogger().info("MCommandFramework loaded successfully!");
    }
}
