---
sidebar_position: 3
---

# @Sub

Marks a method as a subcommand handler. The path is defined by the annotation's value.

## Usage

```java

@Sub(value = "info", description = "Show info")
public void info(CommandContext ctx){
	ctx.sender().sendMessage("Info!");
}
```

## Nested Paths

Use spaces to define multi-level subcommand paths:

```java

@Sub(value = "settings reset", description = "Reset settings")
public void resetSettings(CommandContext ctx){
	// handles /mycommand settings reset
}
```

## Attributes

| Attribute     | Required | Description                                        |
|---------------|----------|----------------------------------------------------|
| `value`       | ✅        | The subcommand path. Spaces define nesting levels. |
| `description` | ❌        | A short description shown in help output.          |

## Notes

- Required `@Arg` parameters must always come before optional ones. Placing a required argument after an optional one
  throws an `IllegalArgumentException` at registration.
- Optional `@Arg` parameters must also be annotated `@Nullable`.
