package me.simzahn.spigotmovieplayer.cinema;

import me.simzahn.spigotmovieplayer.Main;
import me.simzahn.spigotmovieplayer.Resolution;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Represents a cinema *  is a cuboid with a screen on one sidees
 */
public class Cinema {

    private final String name;

    private final Block corner1;
    private final Block corner2;

    private final BlockFace front;
    private final BlockFace[] faces = new BlockFace[] {
            BlockFace.NORTH,
            BlockFace.EAST,
            BlockFace.SOUTH,
            BlockFace.WEST
    };

    private static final List<Cinema> cinemas = new ArrayList<>();

    /**
     * Creates a new cinema
     * @param name Name of the cinema
     * @param corner1 One corner of the cinema
     * @param corner2 The other corner of the cinema
     * @param front The front of the screen
     *              (The opposite direction when the player is looking at the screen)
     *              Must be one of the faces
     *              {@link Cinema#getFaces() Cinema.getFaces()}
     * @throws IllegalArgumentException If the front is not one of the faces
     */
    public Cinema(String name, Block corner1, Block corner2, BlockFace front) {

        if (isNameOccupied(name)) {
            throw new IllegalArgumentException("Cinema Name is already occupied");
        }

        this.name = name;
        this.corner1 = corner1;
        this.corner2 = corner2;

        if (!Arrays.asList(faces).contains(front)) throw new IllegalArgumentException("Front must be one of the faces");
        this.front = front;

        cinemas.add(this);
    }

    /**
     * Returns the name of the cinema
     * @return Name of the cinema
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the corner of the cinema. There is no order to the corners.
     * @see Cinema#getCorner2()
     * @return Corner of the cinema
     */
    public Block getCorner1() {
        return corner1;
    }

    /**
     * Returns the other corner of the cinema. There is no order to the corners.
     * @see Cinema#getCorner1()
     * @return Other corner of the cinema
     */
    public Block getCorner2() {
        return corner2;
    }

    /**
     * Returns the front of the cinema. The front of the cinema is the side of the screen. If the player is looking at
     * the screen, the front is the opposite direction.
     * @return Front of the cinema
     */
    public BlockFace getFront() {
        return front;
    }

    /**
     * Returns all possible faces a cinema can have.
     * @return All possible faces a cinema can have.
     */
    public BlockFace[] getFaces() {
        return faces;
    }

    /**
     * Returns the resolution of the cinema.
     * @return Resolution of the cinema.
     */
    public Resolution getResolution() {

        int height = corner1.getY() - corner2.getY();

        int width;
        if (front == BlockFace.NORTH || front == BlockFace.SOUTH) {
            width = corner1.getZ() - corner2.getZ();
        } else {
            width = corner1.getX() - corner2.getX();
        }

        height = Math.abs(height);
        width = Math.abs(width);

        return new Resolution(width, height);

    }

    public Location getTeleportLocation() {

        int x = (corner1.getX() + corner2.getX()) / 2;
        int y = (corner1.getY() + corner2.getY()) / 2;
        int z = (corner1.getZ() + corner2.getZ()) / 2;

        switch (front) {
            case NORTH -> z = corner1.getZ() + 3;
            case EAST -> x = corner1.getX() + 3;
            case SOUTH -> z = corner1.getZ() - 3;
            case WEST -> x = corner1.getX() - 3;
        }

        return new Location(corner1.getWorld(), x, y, z);

    }



    /**
     *
     * @return
     */
    public static List<Cinema> getCinemas() {
        return new ArrayList<>(cinemas);
    }

    public static boolean isNameOccupied(String nameToCheck) {

        for (Cinema cinema : cinemas) {
            if (cinema.getName().equals(nameToCheck)) return true;
        }

        return false;

    }

    public static Optional<Cinema> getCinemaByName(String name) {

        for (Cinema cinema : cinemas) {
            if (cinema.getName().equals(name)) return Optional.of(cinema);
        }

        return Optional.empty();

    }

    public static void safeAllCinemasToConfig() {

        List<String> names = new ArrayList<>();

        for (Cinema cinema : cinemas) {
            names.add(cinema.getName());

            Main.getPlugin().getConfig().set("cinemas." + cinema.getName() + ".world", cinema.getCorner1().getWorld().getName());
            Main.getPlugin().getConfig().set("cinemas." + cinema.getName() + ".x1", cinema.getCorner1().getX());
            Main.getPlugin().getConfig().set("cinemas." + cinema.getName() + ".y1", cinema.getCorner1().getY());
            Main.getPlugin().getConfig().set("cinemas." + cinema.getName() + ".z1", cinema.getCorner1().getZ());
            Main.getPlugin().getConfig().set("cinemas." + cinema.getName() + ".x2", cinema.getCorner2().getX());
            Main.getPlugin().getConfig().set("cinemas." + cinema.getName() + ".y2", cinema.getCorner2().getY());
            Main.getPlugin().getConfig().set("cinemas." + cinema.getName() + ".z2", cinema.getCorner2().getZ());
            Main.getPlugin().getConfig().set("cinemas." + cinema.getName() + ".front", cinema.getFront().name());

        }

        Main.getPlugin().getConfig().set("cinemaNames", names);

        Main.getPlugin().saveConfig();
    }

    public static void loadAllCinemasFromConfig() {

        FileConfiguration config = Main.getPlugin().getConfig();

        if (!config.contains("cinemas")) return;

        for (String name : config.getStringList("cinemaNames")) {

            BlockFace front = BlockFace.valueOf(config.getString("cinemas." + name + ".front"));

            World world = Bukkit.getWorld(config.getString("cinemas." + name + ".world", "world"));
            if (world == null) continue;

            int x1 = config.getInt("cinemas." + name + ".x1");
            int y1 = config.getInt("cinemas." + name + ".y1");
            int z1 = config.getInt("cinemas." + name + ".z1");
            int x2 = config.getInt("cinemas." + name + ".x2");
            int y2 = config.getInt("cinemas." + name + ".y2");
            int z2 = config.getInt("cinemas." + name + ".z2");

            Block corner1 = world.getBlockAt(x1, y1, z1);
            Block corner2 = world.getBlockAt(x2, y2, z2);

            new Cinema(name, corner1, corner2, front);
        }

    }


}
