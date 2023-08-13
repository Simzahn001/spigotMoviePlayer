package me.simzahn.spigotmovieplayer.cinema;

import me.simzahn.spigotmovieplayer.FrameConverter;
import org.bukkit.scheduler.BukkitRunnable;
import org.bytedeco.javacv.Frame;

import java.nio.file.Path;

public class AsynchronousFrameTransposer extends BukkitRunnable {

    private final FrameConverter frameConverter;
    private final int frameNumber;
    private final Frame frame;
    private final Path destination;

    public AsynchronousFrameTransposer(FrameConverter frameConverter, int frameNumber, Frame frame, Path destination) {
        this.frameConverter = frameConverter;
        this.frameNumber = frameNumber;
        this.frame = frame;
        this.destination = destination;
    }

    @Override
    public void run() {

        Path schematicFile = this.destination.resolve(this.frameNumber + ".schematic");

        //convert the frame
        this.frameConverter.convertFrameToSchematic(this.frame, schematicFile);

    }
}
