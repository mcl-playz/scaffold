---
sidebar_position: 5
---

# @Permission

Restricts a command or subcommand to senders with a specific permission node.

## Usage

### On a method

```java
@Sub(value = "reload", description = "Reload config")
@Permission("myplugin.admin.reload")
public void reload(CommandContext ctx) {
    // only senders with myplugin.admin.reload can run this
}
```

### On a class

Applying `@Permission` to the class sets a default for all subcommands in that class:

```java
@Command(value = "admin", description = "Admin commands")
@Permission("myplugin.admin")
public class AdminCommand extends CommandBase {
    // all subcommands require myplugin.admin unless overridden
}
```

## Resolution Order

Method-level `@Permission` takes precedence over class-level. If neither is present, no permission check is performed.
