package serviceTests;

import com.example.catalog.Application;
import com.example.catalog.model.Artist;
import com.example.catalog.services.JSONDataSourceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
class ArtistControllerTest {

    @Autowired
    private JSONDataSourceService jsonDataSourceService;

    private final String testArtistId = "test-artist-1";

    @BeforeEach
    void setUp() throws IOException {
        // Ensure the artist exists before running each test
        Artist artist = new Artist();
        artist.setId(testArtistId);
        artist.setName("Test Artist");
        jsonDataSourceService.createArtist(artist);
    }

    @Test
    void testGetArtistById() throws IOException {
        Artist artist = jsonDataSourceService.getArtistById(testArtistId);
        assertNotNull(artist);
        assertEquals("Test Artist", artist.getName());
    }

    @Test
    void testGetAllArtists() throws IOException {
        List<Artist> artists = jsonDataSourceService.getAllArtists();
        assertNotNull(artists);
        assertFalse(artists.isEmpty(), "The list of artists should not be empty.");
    }

    @Test
    void testAddArtist() throws IOException {
        Artist artist = new Artist();
        artist.setId("test-artist-2");
        artist.setName("Another Test Artist");

        jsonDataSourceService.createArtist(artist);
        Artist fetchedArtist = jsonDataSourceService.getArtistById("test-artist-2");

        assertNotNull(fetchedArtist);
        assertEquals("Another Test Artist", fetchedArtist.getName());
    }

    @Test
    void testUpdateArtist() throws IOException {
        Artist artist = jsonDataSourceService.getArtistById(testArtistId);
        assertNotNull(artist);

        artist.setName("Updated Artist Name");
        jsonDataSourceService.updateArtist(testArtistId, artist);

        Artist updatedArtist = jsonDataSourceService.getArtistById(testArtistId);
        assertNotNull(updatedArtist);
        assertEquals("Updated Artist Name", updatedArtist.getName());
    }

    @Test
    void testDeleteArtist() throws IOException {
        jsonDataSourceService.deleteArtist(testArtistId);

        Artist deletedArtist = jsonDataSourceService.getArtistById(testArtistId);
        assertNull(deletedArtist, "The artist should be null after deletion.");
    }
}
