package org.jasperdev.scaffold.models;

public class ScaffoldConfig {
	private String errorMessage = "§c§lERROR §8|§r§7 %s";
	private String noPermissionMessage = "You don't have permission to execute that command!";
	private String unknownSubcommandMessage = "Unknown subcommand: %s";
	private String incompleteCommandMessage = "Incomplete command usage";
	private String senderNotConsoleMessage = "This command can only be run from the console!";
	private String senderNotPlayerMessage = "This command can only be run by a player!";
	private String unknownOptionMessage = "Unknown option. Please choose from the following: %s";
	private boolean autoInjectHelp = true;

	public String parse(String base, String input){
		return String.format(base, input);
	}

	public String formatError(String input){
		return parse(errorMessage, input);
	}

	public String getErrorMessage(){
		return errorMessage;
	}

	public ScaffoldConfig setErrorMessage(String errorMessage){
		this.errorMessage = errorMessage;
		return this;
	}

	public String getNoPermissionMessage(){
		return noPermissionMessage;
	}

	public ScaffoldConfig setNoPermissionMessage(String noPermissionMessage){
		this.noPermissionMessage = noPermissionMessage;
		return this;
	}

	public String getUnknownSubcommandMessage(){
		return unknownSubcommandMessage;
	}

	public ScaffoldConfig setUnknownSubcommandMessage(String unknownSubcommandMessage){
		this.unknownSubcommandMessage = unknownSubcommandMessage;
		return this;
	}

	public String getIncompleteCommandMessage(){
		return incompleteCommandMessage;
	}

	public ScaffoldConfig setIncompleteCommandMessage(String incompleteCommandMessage){
		this.incompleteCommandMessage = incompleteCommandMessage;
		return this;
	}

	public String getSenderNotConsoleMessage(){
		return senderNotConsoleMessage;
	}

	public ScaffoldConfig setSenderNotConsoleMessage(String senderNotConsoleMessage){
		this.senderNotConsoleMessage = senderNotConsoleMessage;
		return this;
	}

	public String getSenderNotPlayerMessage(){
		return senderNotPlayerMessage;
	}

	public ScaffoldConfig setSenderNotPlayerMessage(String senderNotPlayerMessage){
		this.senderNotPlayerMessage = senderNotPlayerMessage;
		return this;
	}

	public boolean canAutoInjectHelp(){
		return autoInjectHelp;
	}

	public ScaffoldConfig setAutoInjectHelp(boolean autoInjectHelp){
		this.autoInjectHelp = autoInjectHelp;
		return this;
	}

	public String getUnknownOptionMessage(){
		return unknownOptionMessage;
	}

	public ScaffoldConfig setUnknownOptionMessage(String unknownOptionMessage){
		this.unknownOptionMessage = unknownOptionMessage;
		return this;
	}
}
