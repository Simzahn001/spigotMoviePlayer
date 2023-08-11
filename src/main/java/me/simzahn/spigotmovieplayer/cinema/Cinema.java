package me.simzahn.spigotmovieplayer.cinema;

import me.simzahn.spigotmovieplayer.Main;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a cinema *  is a cuboid with a screen on one side
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
     *
     * @return
     */
    public static List<Cinema> getCinemas() {
        return new ArrayList<>(cinemas);
    }

}
