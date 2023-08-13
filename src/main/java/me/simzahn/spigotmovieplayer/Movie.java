package me.simzahn.spigotmovieplayer;

import me.simzahn.spigotmovieplayer.util.ConfigUtils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Movie {

    private final String name;
    private final File file;
    private final YamlConfiguration config;

    public Movie(@NotNull String name, @NotNull File file) {

        if (!file.exists()) {
            throw new IllegalArgumentException("The given file does not exist.");
        }
        if (!file.isFile()) {
            throw new IllegalArgumentException("The given file is not a file.");
        }
        if (!file.getName().endsWith(".mp4")) {
            throw new IllegalArgumentException("The given file is not a mp4 file.");
        }

        this.file = file;

        this.config = ConfigUtils.loadNewConfig(Path.of(file.getPath() + ".yml"));

        String configName = config.getString("name");
        if (configName != null && !configName.equals("")) {
            this.name = config.getString("name");
        } else {
            this.name = name;
        }
    }

    public String getName() {
        return name;
    }

    public File getFile() {
        return file;
    }

    public YamlConfiguration getConfig() {
        return config;
    }

    /**
     * Get a list with all resolutions, which are prepared for this movie.
     * The resolutions are stored in the config file. The folder containing the .schematic files is
     * {pluginFolder}/movies/{movieName}/{resolution}
     * @return A list with all resolutions, which are prepared for this movie.
     */
    public List<Resolution> getPreparedResolutions() {

        return this.config.getStringList("resolutions").stream()
                .map(Resolution::new)
                .toList();

    }

    /**
     * Add a resolution to the list of prepared resolutions. <b>Only do this if the preparation of this movie is finished
     * and the folder with all the files exists!</b>
     * @param resolution The resolution to add.
     */
    public void addResolution(Resolution resolution) {

        List<String> resolutions = this.config.getStringList("resolutions");
        resolutions.add(resolution.toString());
        this.config.set("resolutions", resolutions);

        ConfigUtils.saveConfig(this.config);

    }

    /**
     * Get a list with all movies contained in a directory, which is not searched recursive.
     * @param directory The directory, which should be searched.
     * @return An optional of a list of movies.
     *         The list may be empty, if no movies are contained in the directory. The directory is not searched
     *         recursively.
     *         The Optional may be empty, if the given file is not a directory or does not exist.
     */
    public static Optional<List<Movie>> loadAllMovies(File directory) {

        // Check if the given file is a directory and exists.
        if (!directory.exists() || !directory.isDirectory()) {
            return Optional.empty();
        }

        // Get all files in the directory.
        File[] files = directory.listFiles();

        // Check if the directory is empty.
        if (files == null) {
            return Optional.of(new ArrayList<>());
        }

        List<Movie> movies = new java.util.ArrayList<>();

        // Add all files to the list, which end with .mp4
        for (File file : files) {
            if (file.isFile()) {
                if (file.getName().endsWith(".mp4")) {
                    movies.add(new Movie(file.getName().replace(".mp4", ""), file));
                }
            }
        }

        return Optional.of(movies);
    }



}
