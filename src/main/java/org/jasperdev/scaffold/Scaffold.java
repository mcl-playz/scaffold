package org.jasperdev.scaffold;

import org.bukkit.plugin.java.JavaPlugin;

public final class Scaffold extends JavaPlugin {
	@Override
	public void onEnable(){
		getLogger().info("Scaffold loaded successfully!");
	}
}
