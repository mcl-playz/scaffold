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
		return getOptionalArg(name, clazz)
				.orElseThrow(() -> new NoSuchElementException("Argument '" + name + "' missing or wrong type"));
	}

	@Nonnull
	public <T> Optional<T> getOptionalArg(String name, Class<T> clazz) {
		Object val = args.get(name);
		if (clazz.isInstance(val)) {
			return Optional.of(clazz.cast(val));
		}
		return Optional.empty();
	}

	/**
	 * Gets either the command sender or the 'player' argument.
	 *
	 * @implNote You need to create an argument named 'player' for this to work.
	 * @return Sender of the command OR 'player' argument
	 */
	@Nonnull
	private Player getTargetOrSender(MCommandContext ctx) {
		return ctx.getOptionalArg("player", Player.class)
				.orElseGet(() -> (Player) ctx.sender());
	}
}