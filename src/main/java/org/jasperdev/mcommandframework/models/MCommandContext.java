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
	 * Gets the first {@link Player} argument, or falls back to the command sender if they are a player.
	 *
	 * @return The first Player argument if present, otherwise the sender cast to Player.
	 * @throws IllegalStateException if no Player argument is found and the sender is not a Player.
	 */
	@Nonnull
	public Player getTarget(){
		return args.values().stream()
				.filter(Player.class::isInstance)
				.map(Player.class::cast)
				.findFirst()
				.orElseGet(() -> {
					if(!(sender() instanceof Player player)){
						throw new IllegalStateException("No player argument found and sender is not a player.");
					}
					return player;
				});
	}
}