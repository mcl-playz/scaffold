---
sidebar_position: 4
---

# ExceptionHandler

A functional interface for handling exceptions that occur during command execution or registration.

## Usage

Set a custom exception handler via `Config.setExceptionHandler()`:

```java
Config config = new Config(this)
    .setExceptionHandler((sender, exception) -> {
        // Handle the exception
        if (sender != null) {
            sender.sendMessage("§cAn error occurred: " + exception.getMessage());
        } else {
            // Null sender means exception occurred during registration
            getLogger().severe("Failed to register command: " + exception.getMessage());
        }
    });

ScaffoldCommandManager manager = new ScaffoldCommandManager(this).setConfig(config);
```

## Signature

```java
@FunctionalInterface
public interface ExceptionHandler {
    void handle(@Nullable CommandSender sender, Exception exception);
}
```

## Parameters

- **sender**: The command sender who triggered the exception. `null` if the exception occurred during command
  registration (not during execution).
- **exception**: The exception that was thrown.

## Default Behavior

If no custom exception handler is set, exceptions are logged to the server logger.
