package org.jasperdev.scaffold.api;

import org.bukkit.command.CommandSender;

import javax.annotation.Nullable;

@FunctionalInterface
public interface ExceptionHandler {
	void handle(@Nullable CommandSender sender, Exception exception);
}
