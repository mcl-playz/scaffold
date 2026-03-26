---
sidebar_position: 1
---

# @Command

Marks a class as a command and defines its name and description.

## Usage

```java

@Command(value = "mycommand", description = "Does something useful")
public class MyCommand extends CommandBase {
	// ...
}
```

## Attributes

| Attribute     | Required | Description                                  |
|---------------|----------|----------------------------------------------|
| `value`       | ✅        | The name of the command (e.g. `/mycommand`). |
| `description` | ❌        | A short description shown in help output.    |

## Notes

- The class must be `public`.
- The class must extend `CommandBase`.
- You do not need to declare the command in `plugin.yml`.
