package serviceTests;

import com.example.catalog.model.Artist;
import com.example.catalog.services.JSONDataSourceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class ArtistControllerTest {

    @Autowired
    private JSONDataSourceService jsonDataSourceService;

    @Test
    void testGetArtistById() throws IOException {
        Artist artist = jsonDataSourceService.getArtistById("1Xyo4u8uXC1ZmMpatF05PJ");
        assertNotNull(artist);
        assertEquals("The Weeknd", artist.getName());
    }

    @Test
    void testGetAllArtists() throws IOException {
        List<Artist> artists = jsonDataSourceService.getAllArtists();
        assertNotNull(artists);
        assertFalse(artists.isEmpty());
    }

    @Test
    void testAddArtist() throws IOException {
        Artist artist = new Artist();
        artist.setId("test-artist-1");
        artist.setName("Test Artist");

        jsonDataSourceService.createArtist(artist);
        Artist fetchedArtist = jsonDataSourceService.getArtistById("test-artist-1");

        assertNotNull(fetchedArtist);
        assertEquals("Test Artist", fetchedArtist.getName());
    }

    @Test
    void testUpdateArtist() throws IOException {
        // Ensure the artist exists
        Artist artist = new Artist();
        artist.setId("test-artist-1");
        artist.setName("Original Artist Name");
        jsonDataSourceService.createArtist(artist);

        // Now update the artist
        artist.setName("Updated Artist Name");
        jsonDataSourceService.updateArtist("test-artist-1", artist);

        // Fetch the updated artist
        Artist updatedArtist = jsonDataSourceService.getArtistById("test-artist-1");

        // Assert the update worked
        assertNotNull(updatedArtist);
        assertEquals("Updated Artist Name", updatedArtist.getName());
    }

    @Test
    void testDeleteArtist() throws IOException {
        jsonDataSourceService.deleteArtist("test-artist-1");
        Artist deletedArtist = jsonDataSourceService.getArtistById("test-artist-1");

        assertNull(deletedArtist);
    }
}
