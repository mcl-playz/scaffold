package org.jasperdev.mcommandframework;

import javax.annotation.Nonnull;
import java.util.function.Function;

public class SubcommandData {
    protected String name;
    protected String description;
    protected Function<SubcommandContext, Boolean> execFunc;

    public SubcommandData(@Nonnull String name, @Nonnull String description, @Nonnull Function<SubcommandContext, Boolean> execFunc){
        this.setName(name);
        this.setDescription(description);
        this.setExecFunc(execFunc);
    }

    @Nonnull
    public SubcommandData setName(@Nonnull String name){
        this.name = name;
        return this;
    }

    @Nonnull
    public SubcommandData setDescription(@Nonnull String description){
        this.description = description;
        return this;
    }

    @Nonnull
    public SubcommandData setExecFunc(@Nonnull Function<SubcommandContext, Boolean> execFunc){
        this.execFunc = execFunc;
        return this;
    }

    @Nonnull
    public Function<SubcommandContext, Boolean> getExecFunc(){
        return this.execFunc;
    }
}
