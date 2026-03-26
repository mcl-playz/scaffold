---
sidebar_position: 3
---

# Configuration

`Config` lets you customise error messages, exception handling, and framework behaviour. All setters return `this` for
chaining.

## Usage

```java
Config config = new Config(this)
		.setErrorMessage("§c§lERROR §8|§r§7 %s")
		.setNoPermissionMessage("You don't have permission to execute that command!")
		.setUnknownSubcommandMessage("Unknown subcommand: %s")
		.setIncompleteCommandMessage("Incomplete command usage")
		.setSenderNotPlayerMessage("This command can only be run by a player!")
		.setSenderNotConsoleMessage("This command can only be run from the console!")
		.setUnknownOptionMessage("Unknown option: %s")
		.setAutoInjectHelp(true)
		.setExceptionHandler((sender, exception) -> {
			if(sender != null){
				sender.sendMessage("§cAn error occurred: " + exception.getMessage());
			}
		});

ScaffoldCommandManager manager = new ScaffoldCommandManager(this).setConfig(config);
```

## Options

| Option                     | Default                                                  | Description                                                                       |
|----------------------------|----------------------------------------------------------|-----------------------------------------------------------------------------------|
| `errorMessage`             | `"§c§lERROR §8\|§r§7 %s"`                                | Wrapper format for all error messages. `%s` is replaced with the message content. |
| `noPermissionMessage`      | `"You don't have permission to execute that command!"`   | Sent when the sender lacks the required permission.                               |
| `unknownSubcommandMessage` | `"Unknown subcommand: %s"`                               | Sent when an unrecognised subcommand is entered. `%s` is replaced with the input. |
| `incompleteCommandMessage` | `"Incomplete command usage"`                             | Sent when a command path has no executor.                                         |
| `senderNotPlayerMessage`   | `"This command can only be run by a player!"`            | Sent when a console sender runs a `@ExecutableBy(PLAYER)` command.                |
| `senderNotConsoleMessage`  | `"This command can only be run from the console!"`       | Sent when a player runs a `@ExecutableBy(CONSOLE)` command.                       |
| `unknownOptionMessage`     | `"Unknown option. Please choose from the following: %s"` | Sent when an invalid choice is entered. `%s` is replaced with the input.          |
| `autoInjectHelp`           | `true`                                                   | Automatically adds a `help` subcommand to every registered command.               |
| `exceptionHandler`         | Default error logger                                     | Custom handler for exceptions during command execution or registration.           |