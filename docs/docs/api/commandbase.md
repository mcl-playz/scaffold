---
sidebar_position: 2
---

# CommandBase

The base class for all commands. Extend this and annotate your class with `@Command`.

## Usage

```java
@Command(value = "mycommand", description = "My command")
public class MyCommand extends CommandBase {
    @Root
    public void root(CommandContext ctx) {
        ctx.sender().sendMessage("Usage: /mycommand <subcommand>");
    }

    @Sub(value = "hello", description = "Say hello")
    public void hello(CommandContext ctx) {
        ctx.sender().sendMessage("Hello!");
    }
}
```

## Overridable Methods

### `choices`

```java
@Override
public Map<String, ChoicesProvider> choices() {
    return Map.of(
        "mode", () -> List.of("fast", "slow", "normal")
    );
}
```

Returns a map of argument name to choices provider. When an `@Arg` name matches a key in this map, the framework uses
the provider for tab-completion and validation instead of the parameter type. Defaults to an empty map.