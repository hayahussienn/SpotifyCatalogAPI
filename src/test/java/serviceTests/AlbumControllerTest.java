package serviceTests;

import com.example.catalog.Application;
import com.example.catalog.model.Album;
import com.example.catalog.model.Track;
import com.example.catalog.services.JSONDataSourceService;
import org.junit.jupiter.api.AfterEach;
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

    private final String testAlbumId = "test-album-unique";

    @AfterEach
    void cleanUp() throws IOException {
        jsonDataSourceService.deleteAlbum(testAlbumId);
    }

    @Test
    void testGetAlbumById() throws IOException {
        Album album = new Album(testAlbumId, "Test Album", "spotify:album:" + testAlbumId, "2024-02-04", 10, null, null);
        jsonDataSourceService.createAlbum(album);

        Album fetchedAlbum = jsonDataSourceService.getAlbumById(testAlbumId);
        assertNotNull(fetchedAlbum);
        assertEquals("Test Album", fetchedAlbum.getName());
    }

    /*
    @Test
    void testGetAllAlbums() throws IOException {
        List<Album> albums = jsonDataSourceService.getAllAlbums();
        assertNotNull(albums);
        assertFalse(albums.isEmpty());
    }

     */

    @Test
    void testAddAlbum() throws IOException {
        Album album = new Album(testAlbumId, "Test Album", "spotify:album:" + testAlbumId, "2024-02-04", 10, null, null);
        jsonDataSourceService.createAlbum(album);

        Album fetchedAlbum = jsonDataSourceService.getAlbumById(testAlbumId);
        assertNotNull(fetchedAlbum);
        assertEquals("Test Album", fetchedAlbum.getName());
    }

    @Test
    void testUpdateAlbum() throws IOException {
        Album album = new Album(testAlbumId, "Original Name", "spotify:album:" + testAlbumId, "2024-02-01", 10, null, null);
        jsonDataSourceService.createAlbum(album);

        album.setName("Updated Name");
        jsonDataSourceService.updateAlbum(testAlbumId, album);

        Album updatedAlbum = jsonDataSourceService.getAlbumById(testAlbumId);
        assertNotNull(updatedAlbum);
        assertEquals("Updated Name", updatedAlbum.getName());
    }

    @Test
    void testDeleteAlbum() throws IOException {
        Album album = new Album(testAlbumId, "To Delete", "spotify:album:" + testAlbumId, "2024-02-04", 10, null, null);
        jsonDataSourceService.createAlbum(album);

        jsonDataSourceService.deleteAlbum(testAlbumId);
        Album deletedAlbum = jsonDataSourceService.getAlbumById(testAlbumId);

        assertNull(deletedAlbum);
    }

}
