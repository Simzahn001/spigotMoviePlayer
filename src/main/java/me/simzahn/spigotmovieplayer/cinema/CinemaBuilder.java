package me.simzahn.spigotmovieplayer.cinema;

import me.simzahn.spigotmovieplayer.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CinemaBuilder implements Listener {

    private String name;
    private Block corner1;
    private Block corner2;
    private BlockFace front;

    private final String uuid;

    public CinemaBuilder(Player player, String name) {

        this.uuid = java.util.UUID.randomUUID().toString();

        this.name = name;

        player.sendMessage(
                Component.text(
                        "Created cinema " + name + ". ",
                        TextColor.color(0, 122, 3)
                ).append(Component.text(
                        "Please select the first corner of the cinema by looking at it and pressing the button.",
                        TextColor.color(255, 249, 246)
                )).append(Component.text(
                        "[Select]",
                        TextColor.color(0, 230, 255)
                ).clickEvent(ClickEvent.runCommand("/cinema" + this.uuid + "select1")))
        );

        Main.getPlugin().getPluginManager().registerEvents(this, Main.getPlugin());
    }


    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {

        if (event.getMessage().startsWith("/cinema" + this.uuid)) {

            event.setCancelled(true);

            String command = event.getMessage().replace("/cinema" + this.uuid, "");

            switch (command) {
                case "select1" -> {
                    this.corner1 = event.getPlayer().getTargetBlock(null, 50);
                    //set the corner one to a green concrete block
                    this.corner1.setType(Material.LIME_CONCRETE);
                    event.getPlayer().sendMessage(
                            Component.text(
                                    "Selected corner 1. It's marked green.",
                                    TextColor.color(0, 122, 3)
                            ).append(Component.text(
                                    "Please select the second corner of the cinema by looking at it and pressing the button.",
                                    TextColor.color(255, 249, 246)
                            )).append(Component.text(
                                    "[Select]",
                                    TextColor.color(0, 230, 255)
                            ).clickEvent(ClickEvent.runCommand("/cinema" + this.uuid + "select2")))
                    );
                }
                case "select2" -> {
                    this.corner2 = event.getPlayer().getTargetBlock(null, 50);

                    //check if this block is in one layer with the first corner
                    if (!(this.corner1.getX() == this.corner2.getX() || this.corner1.getZ() == this.corner2.getZ())) {
                        event.getPlayer().sendMessage(
                                Component.text(
                                        "The second corner must be in the same layer as the first one. " +
                                                "Please select the second corner again.",
                                        TextColor.color(255, 0, 0)
                                )
                        );
                        return;
                    }

                    System.out.println("Cinema size:");
                    System.out.println(Math.abs(this.corner1.getX() - this.corner2.getX()));
                    System.out.println(Math.abs(this.corner1.getZ() - this.corner2.getZ()));

                    //check if the cinema is bigger than 10x10 and smaller than 100x100
                    if (Math.abs(this.corner1.getY() - this.corner2.getY()) < 10 ) {
                        event.getPlayer().sendMessage(
                                Component.text(
                                        "The cinema must be at least 10 blocks high. " +
                                                "Please select the second corner again.",
                                        TextColor.color(255, 0, 0)
                                )
                        );
                        return;
                    }
                    if (Math.abs(this.corner1.getX() - this.corner2.getX()) > 100) {
                        event.getPlayer().sendMessage(
                                Component.text(
                                        "The cinema must be at most 100 blocks high. " +
                                                "Please select the second corner again.",
                                        TextColor.color(255, 0, 0)
                                )
                        );
                        return;
                    }

                    //set the corner two to a green concrete block
                    this.corner2.setType(Material.LIME_CONCRETE);
                    event.getPlayer().sendMessage(
                            Component.text(
                                            "Selected corner 2. It's marked red.",
                                            TextColor.color(0, 122, 3)
                                    ).append(Component.text(
                                            "Are you standing in front of, or behind the cinema screen? (Can you look at" +
                                                    "it, or are you looking at the back side of it?)",
                                            TextColor.color(255, 249, 246)
                                    )).append(Component.text(
                                            "[Front]",
                                            TextColor.color(0, 230, 255)
                                    ).clickEvent(ClickEvent.runCommand("/cinema" + this.uuid + "front")))
                                    .append(Component.text(
                                            "[Back]",
                                            TextColor.color(0, 230, 255)
                                    ).clickEvent(ClickEvent.runCommand("/cinema" + this.uuid + "back")))
                    );
                }
                case "front" -> {
                    if (this.isScreenNorthSound()) {

                        if (corner1.getX() > event.getPlayer().getLocation().getBlockX()) {
                            this.front = BlockFace.WEST;
                        } else {
                            this.front = BlockFace.EAST;
                        }

                    } else {

                        if (corner1.getZ() > event.getPlayer().getLocation().getBlockZ()) {
                            this.front = BlockFace.NORTH;
                        } else {
                            this.front = BlockFace.SOUTH;
                        }

                    }
                    new Cinema(this.name, this.corner1, this.corner2, this.front);

                    //send a player a message, that the cinema was created successfully
                    event.getPlayer().sendMessage(
                            Component.text(
                                    "Created cinema " + this.name + ".",
                                    TextColor.color(0, 122, 3)
                            )
                    );
                    fillScreen();
                    HandlerList.unregisterAll(this);
                }
                case "back" -> {
                    if (this.isScreenNorthSound()) {

                        if (corner1.getX() > event.getPlayer().getLocation().getBlockX()) {
                            this.front = BlockFace.EAST;
                        } else {
                            this.front = BlockFace.WEST;
                        }

                    } else {

                        if (corner1.getZ() > event.getPlayer().getLocation().getBlockZ()) {
                            this.front = BlockFace.SOUTH;
                        } else {
                            this.front = BlockFace.NORTH;
                        }

                    }
                    new Cinema(this.name, this.corner1, this.corner2, this.front);

                    //send a player a message, that the cinema was created successfully
                    event.getPlayer().sendMessage(
                            Component.text(
                                    "Created cinema " + this.name + ".",
                                    TextColor.color(0, 122, 3)
                            )
                    );
                    fillScreen();
                    HandlerList.unregisterAll(this);
                }
            }

        }
    }

    /**
     * Fills the space between the corners of the cinema randomly with black and white concrete blocks.
     */
    private void fillScreen() {

        if (corner1 != null && corner2 != null) {

            int x1 = this.corner1.getX();
            int x2 = this.corner2.getX();
            int y1 = this.corner1.getY();
            int y2 = this.corner2.getY();
            int z1 = this.corner1.getZ();
            int z2 = this.corner2.getZ();

            //sort the corners of the cinema
            if (x1 > x2) {
                int temp = x1;
                x1 = x2;
                x2 = temp;
            }
            if (y1 > y2) {
                int temp = y1;
                y1 = y2;
                y2 = temp;
            }
            if (z1 > z2) {
                int temp = z1;
                z1 = z2;
                z2 = temp;
            }

            //fill the space between the corners with black and white concrete blocks randomly
            for (int y = y1; y <= y2; y++) {
                for (int x = x1; x <= x2; x++) {
                    for (int z = z1; z <= z2; z++) {
                        Block block = this.corner1.getWorld().getBlockAt(x, y, z);
                        if (Math.random() > 0.5) {
                            block.setType(Material.WHITE_CONCRETE);
                        } else {
                            block.setType(Material.BLACK_CONCRETE);
                        }
                    }
                }
            }

        } else {
        }

    }

    /**
     * Checks if the screen of the cinema is north-south oriented. This is true, if you can look at the screen from the
     * east or west side.
     * @return true if the screen is north-south oriented, false if it is east-west oriented
     * @throws IllegalStateException if the corners of the cinema are not set yet
     */
    private boolean isScreenNorthSound() {

        if (corner1 != null && corner2 != null) {
            //in minecraft the x-axis is east-west and the z-axis is north-south
            return this.corner1.getX() == this.corner2.getX();
        } else {
            throw new IllegalStateException("The corners of the cinema are not set yet.");
        }

    }
}
