---
sidebar_position: 7
---

# @Cooldown

Prevents a sender from executing a subcommand more frequently than the specified interval.

## Usage

```java

@Sub(value = "teleport", description = "Teleport to a location")
@Cooldown(value = 1, unit = TimeUnit.MINUTES)
public void teleport(CommandContext ctx){
	// This command can only be executed once every 1 minute per sender
}
```

## Attributes

| Attribute | Required | Description                                                     |
|-----------|----------|-----------------------------------------------------------------|
| `value`   | ✅        | The cooldown duration.                                          |
| `unit`    | ❌        | The time unit for the cooldown. Defaults to `TimeUnit.SECONDS`. |

## Notes

- Cooldowns are per-sender. Each player has their own timer.
- The timer resets after each successful execution.
- Cooldowns are not persisted across server restarts.
