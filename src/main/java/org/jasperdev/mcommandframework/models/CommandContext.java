package org.jasperdev.mcommandframework.models;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

public record CommandContext(
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
}