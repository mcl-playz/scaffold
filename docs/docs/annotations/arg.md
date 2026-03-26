---
sidebar_position: 4
---

# @Arg

Marks a method parameter as a command argument. The framework parses and validates the value automatically based on the
parameter's type.

## Usage

```java

@Sub(value = "give", description = "Give items")
public void give(CommandContext ctx, @Arg("player") Player target, @Arg("amount") int amount){
	// ...
}
```

## Supported Types

| Java Type             | Behaviour                                            |
|-----------------------|------------------------------------------------------|
| `String`              | Passed through as-is.                                |
| `int` / `Integer`     | Parsed as an integer.                                |
| `double` / `Double`   | Parsed as a double.                                  |
| `float` / `Float`     | Parsed as a float.                                   |
| `boolean` / `Boolean` | Parsed as a boolean.                                 |
| `Player`              | Resolved to an online player by exact name.          |
| `OfflinePlayer`       | Resolved from the offline player list by exact name. |

## Choice Arguments

If the argument name matches a key in your command's `choices()` map, the framework will use that provider for
tab-completion and validation instead of the type:

```java

@Override
public Map<String, ChoicesProvider> choices(){
	return Map.of("mode", () -> List.of("fast", "slow", "normal"));
}
```

## Optional Arguments

Mark an argument as optional with `optional = true`. Optional arguments must be trailing and must also be annotated
`@Nullable`:

```java

@Sub(value = "list", description = "List items")
public void list(CommandContext ctx, @Nullable @Arg(value = "page", optional = true) Integer page){
	int p = page != null ? page : 1;
}
```

## Attributes

| Attribute  | Required | Description                                                    |
|------------|----------|----------------------------------------------------------------|
| `value`    | ✅        | The argument name. Used as the key in `CommandContext.args()`. |
| `optional` | ❌        | Whether the argument is optional. Defaults to `false`.         |
