package me.simzahn.spigotmovieplayer;

import me.simzahn.spigotmovieplayer.cinema.CinemaCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private static Main plugin;
    private PluginManager pluginManager;

    @Override
    public void onEnable() {
        plugin = this;

        plugin.getCommand("cinema").setExecutor(new CinemaCommand());
        plugin.getCommand("cinema").setTabCompleter(new CinemaCommand());

        this.pluginManager = getServer().getPluginManager();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Main getPlugin() {
        return plugin;
    }

    public PluginManager getPluginManager() {
        return pluginManager;
    }
}
