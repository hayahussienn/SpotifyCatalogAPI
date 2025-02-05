package serviceTests;

import com.example.catalog.Application;
import com.example.catalog.model.Song;
import com.example.catalog.services.JSONDataSourceService;
import org.junit.jupiter.api.AfterEach;
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
class SongControllerTest {

    @Autowired
    private JSONDataSourceService jsonDataSourceService;

    private final String testSongId = "test-song-1";

    @BeforeEach
    void setUp() throws IOException {
        // Ensure a clean state by deleting any existing test data
        jsonDataSourceService.deleteSong(testSongId);

        // Create a song for testing
        Song song = new Song(testSongId, "Initial Test Song", 200000, 85, "spotify:song:" + testSongId, null, null);
        jsonDataSourceService.createSong(song);
    }

    @AfterEach
    void tearDown() throws IOException {
        // Clean up after each test
        jsonDataSourceService.deleteSong(testSongId);
    }

    @Test
    void testGetSongById() throws IOException {
        Song song = jsonDataSourceService.getSongById(testSongId);
        assertNotNull(song, "The song should exist.");
        assertEquals("Initial Test Song", song.getName(), "Song name should match the initial value.");
    }

    @Test
    void testGetAllSongs() throws IOException {
        List<Song> songs = jsonDataSourceService.getAllSongs();
        assertNotNull(songs, "The song list should not be null.");
        assertFalse(songs.isEmpty(), "The song list should not be empty.");
    }

    @Test
    void testAddSong() throws IOException {
        String uniqueId = "test-song-" + System.currentTimeMillis(); // Unique ID for each run
        Song song = new Song(uniqueId, "New Test Song", 180000, 90, "spotify:song:" + uniqueId, null, null);
        jsonDataSourceService.createSong(song);

        Song fetchedSong = jsonDataSourceService.getSongById(uniqueId);
        assertNotNull(fetchedSong, "The newly added song should exist.");
        assertEquals("New Test Song", fetchedSong.getName(), "Song name should match the added value.");

        // Clean up
        jsonDataSourceService.deleteSong(uniqueId);
    }

    @Test
    void testUpdateSong() throws IOException {
        Song song = jsonDataSourceService.getSongById(testSongId);
        assertNotNull(song, "The song should exist before updating.");

        song.setName("Updated Test Song");
        jsonDataSourceService.updateSong(testSongId, song);

        Song updatedSong = jsonDataSourceService.getSongById(testSongId);
        assertNotNull(updatedSong, "The updated song should exist.");
        assertEquals("Updated Test Song", updatedSong.getName(), "The song name should reflect the updated value.");
    }

    @Test
    void testDeleteSong() throws IOException {
        // Ensure the song exists before deletion
        Song song = jsonDataSourceService.getSongById(testSongId);
        assertNotNull(song, "The song should exist before deletion.");

        // Delete the song
        jsonDataSourceService.deleteSong(testSongId);

        // Verify deletion
        Song deletedSong = jsonDataSourceService.getSongById(testSongId);
        assertNull(deletedSong, "The song should not exist after deletion.");
    }
}
