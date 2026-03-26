# Scaffold

A lightweight, annotation-driven command framework for Bukkit/Spigot plugins.

[![License: GPL](https://img.shields.io/badge/License-GPL-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)
[![Maven](https://img.shields.io/badge/build-Maven-red.svg)](https://maven.apache.org/)
[![JitPack](https://jitpack.io/v/mcl-playz/scaffold.svg)](https://jitpack.io/#mcl-playz/scaffold)

---

📖 **[Read the full documentation](https://mcl-playz.github.io/Scaffold)**

---

## Quick Start

### Maven

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
    <version>LATEST</version>
</dependency>
```

### Gradle

Add Scaffold as a dependency in your `build.gradle`:

```groovy
repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.mcl-playz:scaffold:+'
}
```

Register a command in your plugin's `onEnable`:

```java
ScaffoldCommandManager manager = new ScaffoldCommandManager(this);
manager.registerCommand(new MyCommand());
```

For full usage, annotation reference, and configuration options, see
the [docs site](https://mcl-playz.github.io/Scaffold).
