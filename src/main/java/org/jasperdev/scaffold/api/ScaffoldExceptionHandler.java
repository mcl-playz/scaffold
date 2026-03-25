package org.jasperdev.scaffold.api;

import org.bukkit.command.CommandSender;

import javax.annotation.Nullable;

@FunctionalInterface
public interface ScaffoldExceptionHandler {
	void handle(@Nullable CommandSender sender, Exception exception);
}
