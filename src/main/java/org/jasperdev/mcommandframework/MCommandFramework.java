package org.jasperdev.mcommandframework;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class MCommandFramework extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info(ChatColor.of("#b2ffab") + "MCommandFramework loaded successfully!");

    }
}
