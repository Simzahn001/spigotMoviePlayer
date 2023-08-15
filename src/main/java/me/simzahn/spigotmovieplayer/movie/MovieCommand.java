package me.simzahn.spigotmovieplayer.movie;

import me.simzahn.spigotmovieplayer.cinema.Cinema;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MovieCommand implements CommandExecutor, TabCompleter {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {


        if (!sender.hasPermission("spigotmovieplayer.movie")) {
            sender.sendMessage(Component.text(
                    "You don't have the permission to use this command.",
                    TextColor.color(122, 0, 3)
            ));
            return true;
        }


        switch (args.length) {

            case 1 -> {
                if (args[0].equalsIgnoreCase("list")) {

                    sender.sendMessage(Component.text(
                            "-----------<",
                            TextColor.color(83, 117, 223)
                    ).append(Component.text(
                            "Movies",
                            TextColor.color(0, 12, 223),
                            TextDecoration.BOLD
                    )).append(Component.text(
                            ">-----------",
                            TextColor.color(83, 117, 223)
                    )));

                    for (Movie movie : Movie.getMovies()) {
                        sender.sendMessage(Component.text(
                                " - " + movie.getName(),
                                TextColor.color(255, 179, 37),
                                TextDecoration.BOLD
                        ));

                        for (Resolution resolution : movie.getPreparedResolutions()) {
                            sender.sendMessage(Component.text(
                                    "   - " + resolution.toString(),
                                    TextColor.color(255, 255, 255)
                            ));
                        }

                    }

                    sender.sendMessage(Component.text(
                            "-------------------------------",
                            TextColor.color(83, 117, 223)
                    ));

                    return true;
                }
            }

        }

        return false;
    }



    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        ArrayList<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("list");
        }

        return completions;
    }
}
