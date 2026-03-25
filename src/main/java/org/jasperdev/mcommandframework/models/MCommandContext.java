package org.jasperdev.mcommandframework.models;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Map;

public record MCommandContext(
		@Nonnull CommandSender sender,
		@Nonnull Command command,
		@Nonnull String label,
		@Nonnull Map<String, Object> args
) {
	/**
	 * Gets the first {@link Player} argument, or falls back to the command sender.
	 *
	 * @return The first Player argument if present, otherwise the sender cast to Player.
	 */
	@Nonnull
	public Player getTarget(){
		return args.values().stream()
				.filter(Player.class::isInstance)
				.map(Player.class::cast)
				.findFirst()
				.orElseGet(() -> (Player) sender());
	}
}