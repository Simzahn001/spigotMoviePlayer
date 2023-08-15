package me.simzahn.spigotmovieplayer.moviePreparing;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bytedeco.javacv.Frame;

import java.nio.file.Path;

public class AsynchronousFrameTransposer extends BukkitRunnable {

    private final FrameConverter frameConverter;
    private final int frameNumber;
    private final Frame frame;
    private final Path destination;
    private final int totalFrames;

    public AsynchronousFrameTransposer(FrameConverter frameConverter, int frameNumber, int totalFrames, Frame frame, Path destination) {
        this.frameConverter = frameConverter;
        this.frameNumber = frameNumber;
        this.totalFrames = totalFrames;
        this.frame = frame;
        this.destination = destination;
    }

    @Override
    public void run() {

        Path schematicFile = this.destination.resolve(this.frameNumber + ".schematic");

        //convert the frame
        this.frameConverter.convertFrameToSchematic(this.frame, schematicFile);

        Bukkit.broadcast(Component.text(

                "Done frame " + this.frameNumber + "/" + this.totalFrames +
                " (" + (int) (((double) this.frameNumber / (double) this.totalFrames) * 100) + "%)",
                TextColor.color(0, 230, 255),
                TextDecoration.BOLD

        ));

    }
}
