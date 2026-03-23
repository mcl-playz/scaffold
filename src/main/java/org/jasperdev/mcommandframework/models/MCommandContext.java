package org.jasperdev.mcommandframework.models;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

public record MCommandContext(
		@Nonnull CommandSender sender,
		@Nonnull Command command,
		@Nonnull String label,
		@Nonnull Map<String, Object> args // Map name -> value
) {
	@Nonnull
	public <T> T getArg(String name, Class<T> clazz) {
		// Use the optional logic internally, but throw if empty
		return getOptionalArg(name.toLowerCase(), clazz)
				.orElseThrow(() -> new NoSuchElementException("Argument '" + name + "' missing or wrong type"));
	}

	@Nonnull
	public <T> Optional<T> getOptionalArg(String name, Class<T> clazz) {
		Object val = args.get(name.toLowerCase());
		if (clazz.isInstance(val)) {
			return Optional.of(clazz.cast(val));
		}
		return Optional.empty();
	}

	/**
	 * Gets the first {@link Player} argument, or falls back to the command sender.
	 *
	 * @return The first Player argument if present, otherwise the sender cast to Player.
	 */
	@Nonnull
	public Player getTarget() {
		return args.values().stream()
				.filter(Player.class::isInstance)
				.map(Player.class::cast)
				.findFirst()
				.orElseGet(() -> (Player) sender());
	}
}