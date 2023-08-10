import me.simzahn.spigotmovieplayer.Movie;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class MovieTest {

    @Test
    void testLoadAllMovies() {

        //load the movies from the test resources directory
        Optional<List<Movie>> movies = Movie.loadAllMovies(new File("src/test/resources"));

        //check if the optional is not empty
        assertFalse(movies.isEmpty());

        //check if the list contains one movie
        assertEquals(1, movies.get().size());

        //check if the list contains the expected movies
        assertEquals("test_video.mp4", movies.get().get(0).getName());

    }

}
