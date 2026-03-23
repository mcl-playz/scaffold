package org.jasperdev.mcommandframework.models;

public class MCommandManagerConfig {
    private String errorMessage = "§c§lERROR §8|§r§7 %s";
    private String noPermissionMessage = "You don't have permission to execute that command!";
    private String unknownArgumentMessage = "Unknown argument: %s";
    private String incompleteCommandMessage = "Incomplete command usage";
    private String senderNotConsoleMessage = "This command can only be run from the console!";
    private String senderNotPlayerMessage = "This command can only be run by a player!";

    public String parse(String base, String input){
        return String.format(base, input);
    }

    public String formatError(String input) {
        return parse(errorMessage, input);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public MCommandManagerConfig setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    public String getNoPermissionMessage() {
        return noPermissionMessage;
    }

    public MCommandManagerConfig setNoPermissionMessage(String noPermissionMessage) {
        this.noPermissionMessage = noPermissionMessage;
        return this;
    }

    public String getUnknownArgumentMessage() {
        return unknownArgumentMessage;
    }

    public MCommandManagerConfig setUnknownArgumentMessage(String unknownArgumentMessage) {
        this.unknownArgumentMessage = unknownArgumentMessage;
        return this;
    }

    public String getIncompleteCommandMessage() {
        return incompleteCommandMessage;
    }

    public MCommandManagerConfig setIncompleteCommandMessage(String incompleteCommandMessage) {
        this.incompleteCommandMessage = incompleteCommandMessage;
        return this;
    }

    public String getSenderNotConsoleMessage() {
        return senderNotConsoleMessage;
    }

    public MCommandManagerConfig setSenderNotConsoleMessage(String senderNotConsoleMessage) {
        this.senderNotConsoleMessage = senderNotConsoleMessage;
        return this;
    }

    public String getSenderNotPlayerMessage() {
        return senderNotPlayerMessage;
    }

    public MCommandManagerConfig setSenderNotPlayerMessage(String senderNotPlayerMessage) {
        this.senderNotPlayerMessage = senderNotPlayerMessage;
        return this;
    }
}
