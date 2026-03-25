package org.jasperdev.mcommandframework.models;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public final class OptionData {
	private String name;
	private String description;
	private OptionType type;
	private boolean optional;
	private List<String> choices;
	private ChoicesProvider dynamicChoices;

	public enum OptionType {
		INTEGER,
		DOUBLE,
		FLOAT,
		STRING,
		BOOLEAN,
		PLAYER,
		OFFLINE_PLAYER,
		CHOICE
	}

	public OptionData(@Nonnull String name, @Nonnull String description, @Nonnull OptionType type){
		this.setName(name);
		this.setDescription(description);
		this.setType(type);
	}

	public OptionData(@Nonnull String name, @Nonnull String description, @Nonnull List<String> choices){
		this(name, description, OptionType.CHOICE);
		this.choices = choices;
	}

	public OptionData(@Nonnull String name, @Nonnull String description, @Nonnull ChoicesProvider dynamicChoices){
		this(name, description, OptionType.CHOICE);
		this.dynamicChoices = dynamicChoices;
	}

	@Nonnull
	public OptionData setName(@Nonnull String name){
		this.name = name.toLowerCase();
		return this;
	}

	@Nonnull
	public String getName(){
		return name.toLowerCase();
	}

	@Nonnull
	public OptionData setDescription(@Nonnull String description){
		this.description = description;
		return this;
	}

	@Nonnull
	public String getDescription(){
		return description;
	}

	@Nonnull
	public OptionData setType(@Nonnull OptionType type){
		this.type = type;
		return this;
	}

	@Nonnull
	public OptionType getType(){
		return type;
	}

	public boolean isOptional(){
		return optional;
	}

	public OptionData setOptional(boolean optional){
		this.optional = optional;
		return this;
	}

	@Nullable
	public List<String> getChoices(){
		if(dynamicChoices != null){
			return dynamicChoices.get();
		}
		return choices;
	}

	public static OptionType inferType(Class<?> paramType){
		if(paramType == int.class || paramType == Integer.class) return OptionType.INTEGER;
		if(paramType == double.class || paramType == Double.class) return OptionType.DOUBLE;
		if(paramType == float.class || paramType == Float.class) return OptionType.FLOAT;
		if(paramType == boolean.class || paramType == Boolean.class) return OptionType.BOOLEAN;
		if(paramType == Player.class) return OptionType.PLAYER;
		if(paramType == OfflinePlayer.class) return OptionType.OFFLINE_PLAYER;
		return OptionType.STRING; // default
	}

	/**
	 * A functional interface that supplies a list of valid choices for a command argument.
	 * <p>
	 * Register providers by returning them from {@link org.jasperdev.mcommandframework.api.MCommand#choices()},
	 * keyed by the corresponding {@link org.jasperdev.mcommandframework.annotations.Arg#value()}.
	 * The returned values are used for both tab-completion and input validation.
	 *
	 * <pre>{@code
	 * // Example usage in an MCommand:
	 * public Map<String, ChoicesProvider> choices() {
	 *     return Map.of("gamemode", () -> List.of("survival", "creative", "adventure"));
	 * }
	 * }</pre>
	 *
	 * @see org.jasperdev.mcommandframework.api.MCommand#choices()
	 * @see org.jasperdev.mcommandframework.annotations.Arg
	 */
	@FunctionalInterface
	public interface ChoicesProvider {
		List<String> get();
	}
}