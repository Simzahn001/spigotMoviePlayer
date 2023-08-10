package me.simzahn.spigotmovieplayer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Movie {

    private final String name;
    private final File file;

    public Movie(String name, File file) {
        this.name = name;
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public File getFile() {
        return file;
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
                    movies.add(new Movie(file.getName(), file));
                }
            }
        }

        return Optional.of(movies);
    }



}
