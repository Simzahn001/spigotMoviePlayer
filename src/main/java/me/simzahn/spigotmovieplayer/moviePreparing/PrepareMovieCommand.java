package me.simzahn.spigotmovieplayer.moviePreparing;

import me.simzahn.spigotmovieplayer.movie.Movie;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PrepareMovieCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!sender.hasPermission("spigotmovieplayer.preparemovie")) {
            sender.sendMessage(Component.text(
                    "You don't have the permission to use this command.",
                    TextColor.color(122, 0, 3)
            ));
            return true;
        }

        switch (args.length) {

            case 3 -> {

                String movieName = args[0];
                try {
                    int width = Integer.parseInt(args[1]);
                    int height = Integer.parseInt(args[2]);

                    Optional<Movie> movie = Movie.getMovie(movieName);
                    if (movie.isEmpty()) {
                        sender.sendMessage(Component.text(
                                "There is no movie with the name " + movieName + ".",
                                TextColor.color(122, 0, 3)
                        ));
                        return true;
                    }

                    MoviePreparer preparer = new MoviePreparer(movie.get(), width, height, 20);

                    preparer.prepare();

                    sender.sendMessage(Component.text(
                            "The movie " + movieName + " is now being prepared!. Depending on the lenght of the video," +
                                    " and selected resolution, this may take a while.",
                            TextColor.color(0, 255, 0)
                    ));

                } catch (NumberFormatException e) {
                    sender.sendMessage(Component.text(
                            "The width and height must be numbers.",
                            TextColor.color(122, 0, 3)
                    ));
                    return true;
                }


            }

            default -> {
                sendHelpMessage(sender);
            }

        }

        return false;
    }

    private void sendHelpMessage(CommandSender sender) {

        sender.sendMessage(Component.text(
            "Useage: /preaparemovie <movie to prepare> <width> <height>",
            TextColor.color(122, 0, 3)
        ));

    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        List<String> completions = new ArrayList<>();

        switch (args.length) {

            case 1 -> {
                completions.add("<movie to prepare>");
                for (Movie movie : Movie.getMovies()) {
                    completions.add(movie.getName());
                }
            }
            case 2 -> {
                completions.add("<width>");
            }
            case 3 -> {
                completions.add("<height>");
            }

        }

        return completions;

    }
}
