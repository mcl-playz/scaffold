package org.jasperdev.scaffold.api;

import org.jasperdev.scaffold.models.ArgumentData.ChoicesProvider;

import java.util.Map;

/**
 * Represents an executable command within the MCommandFramework.
 *
 * @see org.jasperdev.scaffold.annotations.Command
 * @see org.jasperdev.scaffold.annotations.Sub
 */
public interface CommandBase {
	/**
	 * Provides a mapping of selection keys to their corresponding {@link ChoicesProvider}.
	 * <p>
	 * These choices are typically used to populate auto-complete suggestions or
	 * validation logic for command arguments.
	 *
	 * @return a non-null {@link Map} where the key is the provider's identifier
	 * (e.g., "players", "colors") and the value is the provider logic.
	 * Returns an empty map by default.
	 */
	default Map<String, ChoicesProvider> choices(){
		return Map.of();
	}
}