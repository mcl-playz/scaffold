---
sidebar_position: 1
---

# Installation

## Requirements

- Java 17+
- Bukkit / Spigot 1.13+
- Maven

## Maven

Add Scaffold as a dependency in your `pom.xml`:

```xml

<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

        ...

<dependency>
<groupId>com.github.mcl-playz</groupId>
<artifactId>scaffold</artifactId>
<version>v2.0.0</version>
</dependency>
```

## Gradle

Add Scaffold as a dependency in your `build.gradle`:

```groovy
repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.mcl-playz:scaffold:v2.0.0'
}
```

## Setup

Create a `ScaffoldCommandManager` instance in your plugin's `onEnable` and register your commands:

```java
public class MyPlugin extends JavaPlugin {
	@Override
	public void onEnable(){
		ScaffoldCommandManager manager = new ScaffoldCommandManager(this);
		manager.registerCommand(new MyCommand());
	}
}
```

You do **not** need to declare commands in `plugin.yml` — Scaffold registers them programmatically at runtime.
