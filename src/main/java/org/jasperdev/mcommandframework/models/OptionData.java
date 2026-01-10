package org.jasperdev.mcommandframework.models;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class OptionData {
    protected String name;
    protected String description;
    protected OptionType type;
    protected List<String> choices;

    public enum OptionType {
        INTEGER,
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

    @Nonnull
    public OptionData setName(@Nonnull String name){
        this.name = name.toLowerCase();
        return this;
    }

    @Nonnull
    public String getName(){
        return name;
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
        return choices;
    }
}