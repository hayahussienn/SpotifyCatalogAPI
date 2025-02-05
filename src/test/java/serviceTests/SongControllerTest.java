package serviceTests;

import com.example.catalog.Application;
import com.example.catalog.model.Song;
import com.example.catalog.services.JSONDataSourceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
class SongControllerTest {

    @Autowired
    private JSONDataSourceService jsonDataSourceService;

    @Test
    void testGetSongById() throws IOException {
        Song song = jsonDataSourceService.getSongById("5PjdY0CKGZdEuoNab3yDmX");
        assertNotNull(song);
        assertEquals("STAY (with Justin Bieber)", song.getName());


    }

    @Test
    void testGetAllSongs() throws IOException {
        List<Song> songs = jsonDataSourceService.getAllSongs();
        assertNotNull(songs);
        assertFalse(songs.isEmpty());
    }

    @Test
    void testAddSong() throws IOException {
        String uniqueId = "test-song-" + System.currentTimeMillis(); // Unique ID
        Song song = new Song(uniqueId, "Test Song", 200000, 90, "spotify:song:" + uniqueId, null, null);
        jsonDataSourceService.createSong(song);

        Song fetchedSong = jsonDataSourceService.getSongById(uniqueId);
        assertNotNull(fetchedSong);
        assertEquals("Test Song", fetchedSong.getName());
    }

    @Test
    void testUpdateSong() throws IOException {
        // Clean up if the song exists to avoid duplicate ID error
        Song existingSong = jsonDataSourceService.getSongById("test-song-1");
        if (existingSong != null) {
            jsonDataSourceService.deleteSong("test-song-1");
        }

        // Create a fresh song
        Song song = new Song("test-song-1", "Original Song", 200000, 90, "spotify:song:test-song-1", null, null);
        jsonDataSourceService.createSong(song);

        // Update the song
        Song updatedSong = new Song("test-song-1", "Updated Song Name", 210000, 95, "spotify:song:test-song-1", null, null);
        jsonDataSourceService.updateSong("test-song-1", updatedSong);

        // Verify the update
        Song fetchedSong = jsonDataSourceService.getSongById("test-song-1");
        assertNotNull(fetchedSong);
        assertEquals("Updated Song Name", fetchedSong.getName());
    }

    @Test
    void testDeleteSong() throws IOException {
        // Clean up if the song already exists
        jsonDataSourceService.deleteSong("testifQCjDGFmkHkpNLD9h");

        // Add a song to ensure it exists before deletion
        Song song = new Song("testifQCjDGFmkHkpNLD9h", "Test Song2", 180000, 85, "spotify:song:test", null, null);
        jsonDataSourceService.createSong(song);

        // Verify song exists before deletion
        Song existingSong = jsonDataSourceService.getSongById("testifQCjDGFmkHkpNLD9h");
        assertNotNull(existingSong); // Confirm song exists

        // Perform deletion
        jsonDataSourceService.deleteSong("testifQCjDGFmkHkpNLD9h");

        // Verify deletion
        Song deletedSong = jsonDataSourceService.getSongById("testifQCjDGFmkHkpNLD9h");
        assertNull(deletedSong); // Confirm song was deleted
    }
}
