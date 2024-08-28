package dev.arctic.exampleMenu;

import dev.arctic.exampleMenu.commands.MenuCommand;
import dev.arctic.exampleMenu.events.PlayerInteractatEntityListener;
import dev.arctic.interactivemenuapi.objects.MenuManager;
import lombok.Getter;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class ExampleMenu extends JavaPlugin {

    @Getter
    public static ExampleMenu plugin;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;

        //Establish Commands
        Objects.requireNonNull(getCommand("menu")).setExecutor(new MenuCommand());

        //Establish Listeners
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerInteractatEntityListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        /*
        * Cleanup all menus
        * This is important! If you don't clean up the menus, they will remain in the world if they are set to persist.
        * You don't want this... trust me...
         */
        MenuManager.cleanupMenus();
    }
}
