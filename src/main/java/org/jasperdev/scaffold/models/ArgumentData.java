package org.jasperdev.scaffold.models;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jasperdev.scaffold.api.CommandBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public final class ArgumentData {
	private String name;
	private String description;
	private ArgumentType type;
	private boolean optional;
	private List<String> choices;
	private ChoicesProvider dynamicChoices;

	public enum ArgumentType {
		INTEGER,
		DOUBLE,
		FLOAT,
		STRING,
		BOOLEAN,
		PLAYER,
		OFFLINE_PLAYER,
		CHOICE
	}

	public ArgumentData(@Nonnull String name, @Nonnull String description, @Nonnull ArgumentType type){
		this.setName(name);
		this.setDescription(description);
		this.setType(type);
	}

	public ArgumentData(@Nonnull String name, @Nonnull String description, @Nonnull List<String> choices){
		this(name, description, ArgumentType.CHOICE);
		this.choices = choices;
	}

	public ArgumentData(@Nonnull String name, @Nonnull String description, @Nonnull ChoicesProvider dynamicChoices){
		this(name, description, ArgumentType.CHOICE);
		this.dynamicChoices = dynamicChoices;
	}

	@Nonnull
	public ArgumentData setName(@Nonnull String name){
		this.name = name.toLowerCase();
		return this;
	}

	@Nonnull
	public String getName(){
		return name.toLowerCase();
	}

	@Nonnull
	public ArgumentData setDescription(@Nonnull String description){
		this.description = description;
		return this;
	}

	@Nonnull
	public String getDescription(){
		return description;
	}

	@Nonnull
	public ArgumentData setType(@Nonnull ArgumentType type){
		this.type = type;
		return this;
	}

	@Nonnull
	public ArgumentType getType(){
		return type;
	}

	public boolean isOptional(){
		return optional;
	}

	public ArgumentData setOptional(boolean optional){
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

	public static ArgumentType inferType(Class<?> paramType){
		if(paramType == int.class || paramType == Integer.class) return ArgumentType.INTEGER;
		if(paramType == double.class || paramType == Double.class) return ArgumentType.DOUBLE;
		if(paramType == float.class || paramType == Float.class) return ArgumentType.FLOAT;
		if(paramType == boolean.class || paramType == Boolean.class) return ArgumentType.BOOLEAN;
		if(paramType == Player.class) return ArgumentType.PLAYER;
		if(paramType == OfflinePlayer.class) return ArgumentType.OFFLINE_PLAYER;
		return ArgumentType.STRING; // default
	}

	/**
	 * A functional interface that supplies a list of valid choices for a command argument.
	 * <p>
	 * Register providers by returning them from {@link CommandBase#choices()},
	 * keyed by the corresponding {@link org.jasperdev.scaffold.annotations.Arg#value()}.
	 * The returned values are used for both tab-completion and input validation.
	 *
	 * <pre>{@code
	 * // Example usage in an MCommand:
	 * public Map<String, ChoicesProvider> choices() {
	 *     return Map.of("gamemode", () -> List.of("survival", "creative", "adventure"));
	 * }
	 * }</pre>
	 *
	 * @see CommandBase#choices()
	 * @see org.jasperdev.scaffold.annotations.Arg
	 */
	@FunctionalInterface
	public interface ChoicesProvider {
		List<String> get();
	}
}