package me.simzahn.spigotmovieplayer.cinema;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CinemaCommand implements CommandExecutor, TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!sender.hasPermission("spigotmovieplayer.cinema")) {
            sender.sendMessage(Component.text(
                    "You don't have the permission to use this command.",
                    TextColor.color(122, 0, 3)
            ));
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(Component.text(
                    "Usage: /cinema <create> <name of cinema>",
                    TextColor.color(122, 0, 3)
            ));
            return true;
        }else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("create")) {
                sender.sendMessage(Component.text(
                        "Usage: /cinema create <name of cinema>",
                        TextColor.color(122, 0, 3)
                ));
                return true;
            }
        }else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("create")) {
                String name = args[1];

                //check if the sender is a player
                if (!(sender instanceof org.bukkit.entity.Player)) {
                    sender.sendMessage(Component.text(
                            "You must be a player to use this command.",
                            TextColor.color(122, 0, 3)
                    ));
                    return true;
                }

                new CinemaBuilder((Player) sender, name);
                return true;
            }
        }


        return false;

    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        List<String> list = new ArrayList<>();

        if (args.length == 2) {

            if (args[1].trim().length() == 0) {
                list.add("<name of cinema>");
            }

        } else if (args.length == 1) {

            list.add("create");

        }

        return list;
    }
}
