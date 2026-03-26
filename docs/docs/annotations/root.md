---
sidebar_position: 2
---

# @Root

Marks a method as the root executor of a command - invoked when the base command is run with no subcommand arguments.

## Usage

```java
@Command(value = "mycommand", description = "My command")
public class MyCommand extends CommandBase {
    @Root
    public void root(CommandContext ctx) {
        ctx.sender().sendMessage("Usage: /mycommand <subcommand>");
    }
}
```

## Notes

- Only **one** method per `@Command` class may be annotated with `@Root`. A second `@Root` method will throw an
  `IllegalArgumentException` at registration time.
- Arguments via `@Arg` are supported on a `@Root` method the same as on `@Sub`.
