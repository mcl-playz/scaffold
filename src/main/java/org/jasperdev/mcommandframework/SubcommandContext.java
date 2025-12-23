package org.jasperdev.mcommandframework;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;

public record SubcommandContext(CommandSender sender, Command command, String label, String[] args) {
    public SubcommandContext(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        this.sender = sender;
        this.command = command;
        this.label = label;
        this.args = args;
    }
}