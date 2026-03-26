---
sidebar_position: 1
---

# ScaffoldCommandManager

The entry point for the framework. Handles command registration, execution routing, and tab completion. Only one of
these should exist throughout your entire plugin.

## Constructor

```java
ScaffoldCommandManager manager = new ScaffoldCommandManager(JavaPlugin plugin);
```

## Methods

### `setConfig`

```java
manager.setConfig(Config config);
```

Sets the configuration for the manager. Must be called before registering commands if you want custom messages or
behaviour. Returns `this` for chaining.

### `registerCommand`

```java
manager.registerCommand(CommandBase command);
```

Registers a command class with Bukkit. The command is built from the class's annotations and registered to the server's
command map at runtime — no `plugin.yml` entry required.

Throws `IllegalArgumentException` if:

- The command class is not `public`.
- A `@Root` method is duplicated in the class.
- A required `@Arg` follows an optional `@Arg`.
- An optional `@Arg` is not annotated `@Nullable`.