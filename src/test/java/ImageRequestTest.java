import me.simzahn.spigotmovieplayer.pixelStackerRequests.ImageRequest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;

public class ImageRequestTest {

    @Test
    void createImageRequest() throws IOException, InterruptedException {

        ImageRequest request = new ImageRequest(Path.of("C:\\Users\\simon\\IdeaProjects\\spigotMoviePlayer\\src\\test\\resources\\img.png").toFile(), 20, 20);

        request.sendRequest();

        request.retrieveSchematic(Path.of("C:\\Users\\simon\\IdeaProjects\\spigotMoviePlayer\\src\\test\\resources\\img.schematic").toFile());

    }


}
