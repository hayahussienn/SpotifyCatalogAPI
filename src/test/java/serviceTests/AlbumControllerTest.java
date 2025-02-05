package serviceTests;

import com.example.catalog.Application;
import com.example.catalog.model.Album;
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
class AlbumControllerTest {

    @Autowired
    private JSONDataSourceService jsonDataSourceService;

    /*
    @Test
    void testGetAlbumById() throws IOException {
        Album album = jsonDataSourceService.getAlbumById("4yP0hdKOZPNshxUOjY0cZj");
        assertNotNull(album);
        assertEquals("After Hours", album.getName());
    }

     */

    @Test
    void testGetAllAlbums() throws IOException {
        List<Album> albums = jsonDataSourceService.getAllAlbums();
        assertNotNull(albums);
        assertFalse(albums.isEmpty());
    }

    @Test
    void testAddAlbum() throws IOException {
        Album album = new Album("test-album-1", "Test Album", "spotify:album:test-album-1", "2024-02-04", 10, null, null);
        jsonDataSourceService.createAlbum(album);

        Album fetchedAlbum = jsonDataSourceService.getAlbumById("test-album-1");
        assertNotNull(fetchedAlbum);
        assertEquals("Test Album", fetchedAlbum.getName());
    }

    @Test
    void testUpdateAlbum() throws IOException {
        //Create the album to ensure it exists
        Album album = new Album("test-album-1", "Original Album Name", "spotify:album:test-album-1", "2024-02-01", 10, null, null);
        jsonDataSourceService.createAlbum(album);

        // Update the album
        album.setName("Updated Album Name");
        jsonDataSourceService.updateAlbum("test-album-1", album);

        // Fetch the updated album
        Album updatedAlbum = jsonDataSourceService.getAlbumById("test-album-1");

        // Assert that the update worked
        assertNotNull(updatedAlbum);
        assertEquals("Updated Album Name", updatedAlbum.getName());
    }

    @Test
    void testDeleteAlbum() throws IOException {
        jsonDataSourceService.deleteAlbum("test-album-1");
        Album deletedAlbum = jsonDataSourceService.getAlbumById("test-album-1");

        assertNull(deletedAlbum);
    }
}
