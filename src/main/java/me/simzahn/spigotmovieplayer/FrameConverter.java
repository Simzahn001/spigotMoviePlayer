package me.simzahn.spigotmovieplayer;

import me.simzahn.spigotmovieplayer.pixelStackerRequests.ImageRequest;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameUtils;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

/**
 * Converts a frame to a schematic.
 */
public class FrameConverter {

    private final int width;
    private final int height;

    /**
     * Creates a new FrameConverter.
     * @param width The maximum width of the schematic.
     * @param height The maximum height of the schematic.
     */
    public FrameConverter(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Converts a frame to a schematic and returns the path to the schematic.
     * @param frame The frame to convert.
     * @param schematicPath The path to the schematic.
     * @return The path to the schematic. Empty if the conversion failed.
     */
    public Optional<Path> convertFrameToSchematic(Frame frame, Path schematicPath) {

        //create the temporary file with unique name
        String uuid = java.util.UUID.randomUUID().toString();
        File tempFile = new File(Main.getPlugin().getDataFolder(), "TEMP/" + uuid + ".png");
        try {
            tempFile.createNewFile();
        } catch (IOException e) {
            return Optional.empty();
        }

        //safe the frame to a png file
        try {
            ImageIO.write(Java2DFrameUtils.toBufferedImage(frame), "png", tempFile);
        } catch (IOException e) {
            return Optional.empty();
        }

        //send the request to the PixelStacker API
        ImageRequest request = new ImageRequest(tempFile, width, height);
        if (!request.sendRequest()) {
            return Optional.empty();
        }

        //safe the schematic to the schematic path
        File schematicFile = schematicPath.toFile();
        return request.retrieveSchematic(schematicFile);

    }

}
