package org.jasperdev.scaffold;

import org.bukkit.plugin.java.JavaPlugin;
import org.jasperdev.scaffold.api.ScaffoldCommandManager;

public final class Scaffold extends JavaPlugin {
	@Override
	public void onEnable(){
		ScaffoldCommandManager manager = new ScaffoldCommandManager(this);
		manager.registerCommand(new ScaffoldTestCommand());

		getLogger().info("Scaffold loaded successfully!");
	}
}
