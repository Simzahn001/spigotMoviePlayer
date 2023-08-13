package me.simzahn.spigotmovieplayer.moviePreparing;

import me.simzahn.spigotmovieplayer.Main;
import me.simzahn.spigotmovieplayer.Movie;
import me.simzahn.spigotmovieplayer.Resolution;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.opencv.videoio.VideoCapture;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Path;

public class MoviePreparer {

    private final Movie movie;

    private final Resolution resolution;

    private final int fps;

    private final VideoCapture videoCapture;

    private int baseFrameSkip;
    private int adjustFrameSkip;
    private int adjustFrameSkipCounter = 0;

    private FrameGrabber grabber;
    private final OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat();

    private Frame frame;


    public MoviePreparer(Movie movie, int width, int height, int fps) {
        this.movie = movie;
        this.resolution = new Resolution(width, height);
        this.fps = fps;

        //start the video capture
        this.videoCapture = new VideoCapture();
        this.videoCapture.open(this.movie.getFile().getAbsolutePath());
    }

    public void prepare() throws FrameGrabber.Exception {

        retrieveFPSRatios();

        if (!setupFrameGrabber()) {
            Main.getPlugin().getLogger().warning("The Frame Grabber was not set up correctly!");
            return;
        }

        FrameConverter frameConverter = new FrameConverter(this.resolution.getWidth(), this.resolution.getHeight());
        Path path = Path.of(
            Main.getPlugin().getDataFolder().getPath() +
            "/" + "converted" +
            "/" + this.movie.getName() +
            "/" + this.resolution.toString()
        );

        while (next()) {

            //this will create the .schematic file asynchronously
            new AsynchronousFrameTransposer(
                    frameConverter,
                    this.grabber.getFrameNumber(),
                    this.frame,
                    path
            ).runTaskAsynchronously(Main.getPlugin());

        }

        Bukkit.broadcast(Component.text(
               "Finished converting " + this.movie.getName() + " to Resolution " + this.resolution.toString() + "!",
                TextColor.color(33, 255, 0),
                TextDecoration.BOLD
        ));

        videoCapture.release();
        grabber.stop();

    }

    private boolean next() {

        //check how many frames to skip
        int framesToSkip = this.baseFrameSkip;
        if (adjustFrameSkipCounter > adjustFrameSkip) {
            framesToSkip += 1;
        }

        try {
            //skip the unnecessary frames
            for (int i = 0; i <= framesToSkip-1; i++) {

                if (isAtLastFrame()) {
                    return false;
                }

                grabber.grab();
            }

            if (isAtLastFrame()) {
                return false;
            }

            adjustFrameSkipCounter += 1;
            if (adjustFrameSkipCounter >= 1000) {
                adjustFrameSkipCounter = 0;
            }

            //save this frame
            this.frame = this.grabber.grab();

        } catch (FrameGrabber.Exception e) {
            Main.getPlugin().getLogger().warning("Could not grab a frame!");
        }

        return true;
    }

    private boolean setupFrameGrabber() {

        try (FrameGrabber grabber = FrameGrabber.createDefault(this.movie.getFile())) {

            grabber.start();

            return true;

        } catch (FrameGrabber.Exception e) {
            Main.getPlugin().getLogger().warning("The movie file " + this.movie.getFile().getAbsolutePath() +
                    " was not found!");
            return false;
        }
    }



    private void retrieveFPSRatios() {

        //retrieve fps of the video
        double mp4Fps = this.videoCapture.get(5);

        double fpsRatio = mp4Fps / fps;
        fpsRatio = round(fpsRatio, 3);

        //base frame skip
        //this means, for a 60fps video, only every 3rd frame is used, to make it 20 fps
        this.baseFrameSkip = (int) Math.floor(fpsRatio);

        //if the frame rates are not a cool number, like 57 / 20, the ratio is not a whole number (2.85). This means, we
        //have to grab the 2nd frame 17 times and then the 3rd frame 3 times to not manipulate the speed of the video.
        // We do this at base 1000 (rounded to 3 digits -> every of these numbers can be converted to a fractional number)
        this.adjustFrameSkip = (int) (fpsRatio - baseFrameSkip) * 1000;

    }

    private boolean isAtLastFrame() {
        return this.grabber.getLengthInFrames() >= this.grabber.getFrameNumber();
    }


    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
