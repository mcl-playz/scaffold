---
sidebar_position: 2
---

# Your First Command

## Creating a Command Class

Every command is a class that extends `CommandBase` and is annotated with `@Command`.

```java
@Command(value = "greet", description = "Greet a player")
public class GreetCommand extends CommandBase {
    @Root
    public void root(CommandContext ctx) {
        ctx.sender().sendMessage("Usage: /greet <player>");
    }

    @Sub(value = "player", description = "Greet a specific player")
    public void greetPlayer(CommandContext ctx, @Arg("player") String playerName) {
        ctx.sender().sendMessage("Hello, " + playerName + "!");
    }
}
```

## Registering the Command

```java
manager.registerCommand(new GreetCommand());
```

This registers `/greet` and `/greet player <player>` automatically.

## Nested Subcommands

Subcommand paths can be multi-level by using spaces in the `@Sub` value:

```java
@Sub(value = "settings reset", description = "Reset all settings")
public void resetSettings(MCommandContext ctx) {
    // handles /mycommand settings reset
}
```

## Optional Arguments

Arguments can be marked optional. Optional arguments must always be trailing and must also be annotated `@Nullable`:

```java
@Sub(value = "info", description = "Show info")
public void info(MCommandContext ctx, @Nullable @Arg(value = "page", optional = true) Integer page) {
    int p = page != null ? page : 1;
    // ...
}
```
