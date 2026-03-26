---
sidebar_position: 3
---

# CommandContext

Passed to every command executor method. Provides access to the sender, the raw Bukkit command, and the parsed
arguments.

## Methods

### `sender`

```java
CommandSender sender = ctx.sender();
```

The sender who executed the command. Cast to `Player` if you are inside an `@ExecutableBy(PLAYER)` command.

### `command`

```java
Command command = ctx.command();
```

The underlying Bukkit command object.

### `label`

```java
String label = ctx.label();
```

The alias used to invoke the command.

### `args`

```java
Map<String, Object> args = ctx.args();
```

The parsed arguments keyed by their `@Arg` name. For type-safe access, prefer using `@Arg`-annotated parameters directly
in your method signature rather than retrieving from this map manually.

### `getTarget`

```java
Player target = ctx.getTarget();
```

Returns the first `Player` argument in the args map, or falls back to the sender if they are a `Player`. Throws
`IllegalStateException` if neither condition is met — so this should only be called in commands restricted with
`@ExecutableBy(PLAYER)` or where a `Player` argument is guaranteed to be present.

```java
// Safe usage - sender is guaranteed to be a player
@Sub(value = "heal", description = "Heal yourself or another player")
@ExecutableBy(SenderType.PLAYER)
public void heal(CommandContext ctx, @Nullable @Arg(value = "target", optional = true) Player target) {
    ctx.getTarget().setHealth(20); // falls back to sender if target not provided
}
```