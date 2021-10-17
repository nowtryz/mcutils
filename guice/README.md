MCUtils' Guice package
======================

This package is designed to integrate [Guice](https://github.com/google/guice) et the bukkit plugin development. Other
packages in the project use this package as a base to integrate with Guice. Guice is a lightweight dependency-injection
framework that can be used to link together parts of a plugin such as managers.

## Installation

This package is part of `net.nowtryz.mcutils:mcutils`, so there is no need to proceed to any further installation if you
installed the global `mcutils` package

### Maven
```xml
<dependencies>
    <dependency>
        <groupId>net.nowtryz.mcutils</groupId>
        <artifactId>guice</artifactId>
        <version>0.2.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

### Gradle
```groovy
dependencies {
    // ...
    implementation('net.nowtryz.mcutils:guice:0.2.0-SNAPSHOT')
}
```

## Getting started

The following exemples will show you as to integrate this package with you plugin

### Enable it with your plugin

To enable basic bukkit dependency injection, you simply need to initialize Guice with a `BukkitModule` as follows:
```groovy
Guice.createInjector(
        new BukkitModule<>(this, JumpPlugin.class)
        // and any of your guice modules
);
```

These lines must be added to the `onEnalbe` method of your plugin. Here is a basic example:
```java
/**
 * Main class of your plugin
 */
public final class YourPlugin extends JavaPlugin {
    // Says you want to initialize a manage
    private @Inject AManager aManager;

    @Override
    public void onEnable() {

        // This will create the injector and inject all required objects to the plugin
        Guice.createInjector(
                new BukkitModule<>(this, JumpPlugin.class)
                // and any of your guice modules
        );
    }

    // and you also want to initialize a listenaer
    @Inject
    public void registerAListeners(AListener aListener) {
        aListener.register();
    }
}
```

You can then use dependency injection in your manager or listeners
```java
public class AListener extends AbstractListener {
    private final AManager aManager;

    @Inject
    public AListener(YourPlugin plugin, AManager aManager) {
        super(plugin);
        this.aManager = aManager;
    }

    @EventHandler
    public void onEvent(AnyBukkitEvent event) {
        this.aManager.doAThing(event.getAnything());
    }
}
```

### Use the provided annotations

#### Get the plugin's logger
```java
public class AManager {
    @Inject AManager(YourPlugin plugin, @PluginLogger Logger logger) {
        // store or do thins with your logger
    }
}
```

#### Get the plugin's data folder
```java
public class AManager {
    @Inject AManager(YourPlugin plugin, @DataFolder File folder) {
        // store or do thins with your data folder
    }
}
```

#### Get an executor that run task asynchronously
```java
public class AManager extends AbstractManager {
    @Inject AManager(YourPlugin plugin, @BukkitExecutor Executor executor) {
        // store or do thins with your executor
    }
}
```

#### Get the plugin configurations
You can either get it as a `FileConfiguration` or a `Configuration` object
```java
public class AManager extends AbstractManager {
    @Inject AManager(YourPlugin plugin, @DefaultConfig Configuration configuration,  @DefaultConfig FileConfiguration fileConfiguration) {
        // store or do thins with your configuration
    }
}
```

#### The `Nullable` annotation
You need to specify through a `Nullable` to Guice if a field can be null, otherwise it will always raise errors.
Nevertheless, not all annotations can be used for this purpose. Guice used to use the `javax.annotation.meta.Nullable`
annotation in the previous version witch can no longer be used due to license conflicts. For this purpose, this package
comes withe a `Nullable` annotation that is configured to be used by guice.

If you use Guice 4.2.0 or higher, you simply need to have an annotation called `Nullable`, no matter its configurations
or from where it came. Hence, our `Nullable` annotation can be used in any cases.

