package me.simzahn.spigotmovieplayer;

import me.simzahn.spigotmovieplayer.cinema.Cinema;
import me.simzahn.spigotmovieplayer.cinema.CinemaCommand;
import me.simzahn.spigotmovieplayer.movie.Movie;
import me.simzahn.spigotmovieplayer.movie.MovieCommand;
import me.simzahn.spigotmovieplayer.moviePreparing.PrepareMovieCommand;
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

        plugin.getCommand("movie").setExecutor(new MovieCommand());
        plugin.getCommand("movie").setTabCompleter(new MovieCommand());

        plugin.getCommand("preparemovie").setExecutor(new PrepareMovieCommand());
        plugin.getCommand("preparemovie").setTabCompleter(new PrepareMovieCommand());

        this.pluginManager = getServer().getPluginManager();

        Cinema.loadAllCinemasFromConfig();

        Movie.loadAllMovies(Main.getPlugin().getDataFolder().toPath().resolve("movies").toFile());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Cinema.safeAllCinemasToConfig();
        System.out.println("Cinemas saved");
    }

    public static Main getPlugin() {
        return plugin;
    }

    public PluginManager getPluginManager() {
        return pluginManager;
    }
}
