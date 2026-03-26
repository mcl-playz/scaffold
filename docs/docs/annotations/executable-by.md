---
sidebar_position: 6
---

# @ExecutableBy

Restricts which type of sender can execute a command or subcommand.

## Usage

```java

@Sub(value = "reload", description = "Reload config")
@ExecutableBy(SenderType.CONSOLE)
public void reload(CommandContext ctx){
	// only the console can run this
}
```

## Sender Types

| Value                | Description                                                      |
|----------------------|------------------------------------------------------------------|
| `SenderType.ALL`     | Any sender can run this command. This is the default.            |
| `SenderType.PLAYER`  | Only players can run this command. Console senders are rejected. |
| `SenderType.CONSOLE` | Only the console can run this command. Players are rejected.     |

## Class-Level Usage

Applying `@ExecutableBy` to the class sets a default for all subcommands:

```java

@Command(value = "play", description = "Player commands")
@ExecutableBy(SenderType.PLAYER)
public class PlayCommand extends CommandBase {
	// all subcommands require a player sender unless overridden
}
```

## Resolution Order

Method-level `@ExecutableBy` takes precedence over class-level. If neither is present, `SenderType.ALL` is used.