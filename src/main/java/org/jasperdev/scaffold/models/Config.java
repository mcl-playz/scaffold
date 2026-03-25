package org.jasperdev.scaffold.models;

import org.bukkit.plugin.java.JavaPlugin;
import org.jasperdev.scaffold.api.ExceptionHandler;

public class Config {
	private JavaPlugin plugin;

	private String errorMessage = "§c§lERROR §8|§r§7 %s";
	private String noPermissionMessage = "You don't have permission to execute that command!";
	private String unknownSubcommandMessage = "Unknown subcommand: %s";
	private String incompleteCommandMessage = "Incomplete command usage";
	private String senderNotConsoleMessage = "This command can only be run from the console!";
	private String senderNotPlayerMessage = "This command can only be run by a player!";
	private String unknownOptionMessage = "Unknown option. Please choose from the following: %s";

	private boolean autoInjectHelp = true;

	private ExceptionHandler exceptionHandler = ((sender, e) -> {
		if(sender != null) sender.sendMessage("An internal error occurred.");
		plugin.getLogger().severe(e.getMessage());
	});

	public Config(JavaPlugin plugin){
		this.plugin = plugin;
	}

	public String parse(String base, String input){
		return String.format(base, input);
	}

	public String formatError(String input){
		return parse(errorMessage, input);
	}

	public String getErrorMessage(){
		return errorMessage;
	}

	public Config setErrorMessage(String errorMessage){
		this.errorMessage = errorMessage;
		return this;
	}

	public String getNoPermissionMessage(){
		return noPermissionMessage;
	}

	public Config setNoPermissionMessage(String noPermissionMessage){
		this.noPermissionMessage = noPermissionMessage;
		return this;
	}

	public String getUnknownSubcommandMessage(){
		return unknownSubcommandMessage;
	}

	public Config setUnknownSubcommandMessage(String unknownSubcommandMessage){
		this.unknownSubcommandMessage = unknownSubcommandMessage;
		return this;
	}

	public String getIncompleteCommandMessage(){
		return incompleteCommandMessage;
	}

	public Config setIncompleteCommandMessage(String incompleteCommandMessage){
		this.incompleteCommandMessage = incompleteCommandMessage;
		return this;
	}

	public String getSenderNotConsoleMessage(){
		return senderNotConsoleMessage;
	}

	public Config setSenderNotConsoleMessage(String senderNotConsoleMessage){
		this.senderNotConsoleMessage = senderNotConsoleMessage;
		return this;
	}

	public String getSenderNotPlayerMessage(){
		return senderNotPlayerMessage;
	}

	public Config setSenderNotPlayerMessage(String senderNotPlayerMessage){
		this.senderNotPlayerMessage = senderNotPlayerMessage;
		return this;
	}

	public boolean canAutoInjectHelp(){
		return autoInjectHelp;
	}

	public Config setAutoInjectHelp(boolean autoInjectHelp){
		this.autoInjectHelp = autoInjectHelp;
		return this;
	}

	public String getUnknownOptionMessage(){
		return unknownOptionMessage;
	}

	public Config setUnknownOptionMessage(String unknownOptionMessage){
		this.unknownOptionMessage = unknownOptionMessage;
		return this;
	}

	public ExceptionHandler getExceptionHandler(){
		return exceptionHandler;
	}

	public void setExceptionHandler(ExceptionHandler exceptionHandler){
		this.exceptionHandler = exceptionHandler;
	}
}
