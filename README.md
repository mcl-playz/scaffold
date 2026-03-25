# MCommandFramework

A lightweight, annotation-driven command framework for Bukkit/Spigot plugins.

[![License: GPL](https://img.shields.io/badge/License-GPL-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)
[![Maven](https://img.shields.io/badge/build-Maven-red.svg)](https://maven.apache.org/)

---

📖 **[Read the full documentation](https://mcl-playz.github.io/Scaffold)**

---

## Quick Start

Add the dependency to your `pom.xml`:

```xml

<dependency>
    <groupId>org.jasperdev</groupId>
    <artifactId>scaffold</artifactId>
    <version>LATEST</version>
</dependency>
```

Register a command in your plugin's `onEnable`:

```java
ScaffoldCommandManager manager = new ScaffoldCommandManager(this);
manager.

registerCommand(new MyCommand());
```

For full usage, annotation reference, and configuration options, see
the [docs site](https://mcl-playz.github.io/Scaffold).
