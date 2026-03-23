package org.jasperdev.mcommandframework.models;

import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class OptionData {
    protected String name;
    protected String description;
    protected OptionType type;
    protected List<String> choices;
    protected ChoicesProvider dynamicChoices;

    public enum OptionType {
        INTEGER,
		DOUBLE,
        FLOAT,
        STRING,
        PLAYER,
        CHOICE
    }

    public OptionData(@Nonnull String name, @Nonnull String description, @Nonnull OptionType type){
        this.setName(name);
        this.setDescription(description);
        this.setType(type);
    }

    public OptionData(@Nonnull String name, @Nonnull String description, @Nonnull List<String> choices) {
        this(name, description, OptionType.CHOICE);
        this.choices = choices;
    }

    public OptionData(@Nonnull String name, @Nonnull String description, @Nonnull ChoicesProvider dynamicChoices) {
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

    @Nullable
    public List<String> getChoices() {
		if (dynamicChoices != null) {
			return dynamicChoices.get();
		}
        return choices;
    }

    public static OptionType inferType(Class<?> paramType) {
        if (paramType == int.class || paramType == Integer.class) return OptionType.INTEGER;
        if (paramType == double.class || paramType == Double.class) return OptionType.DOUBLE;
        if (paramType == float.class || paramType == Float.class) return OptionType.FLOAT;
        if (paramType == Player.class) return OptionType.PLAYER;
        return OptionType.STRING; // default
    }

    @FunctionalInterface
    public interface ChoicesProvider {
        List<String> get();
    }
}