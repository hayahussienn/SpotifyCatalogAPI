package serviceTests;

import com.example.catalog.Application;
import com.example.catalog.model.Album;
import com.example.catalog.model.Artist;
import com.example.catalog.model.Song;
import com.example.catalog.services.SpotifyAPIDataSourceService;
import com.example.catalog.services.SpotifyAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class SpotifyAPIDataSourceServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SpotifyAPIDataSourceService dataSourceService;

    @MockBean
    private SpotifyAuthService spotifyAuthService;

    private final String dummyAccessToken = "dummy_access_token";

    @BeforeEach
    void setup() {
        doReturn(dummyAccessToken).when(spotifyAuthService).getAccessToken();
    }


    @Test
    void getArtistById_Success() throws Exception {
        Artist artist = new Artist();
        artist.setId("3TVXtAsR1Inumwj472S9r4");
        artist.setName("Drake");
        artist.setPopularity(96);
        artist.setUri("spotify:artist:3TVXtAsR1Inumwj472S9r4");

        doReturn(artist).when(dataSourceService).getArtistById("3TVXtAsR1Inumwj472S9r4");

        mockMvc.perform(get("/artists/3TVXtAsR1Inumwj472S9r4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Drake"))
                .andExpect(jsonPath("$.id").value("3TVXtAsR1Inumwj472S9r4"))
                .andExpect(jsonPath("$.popularity").value(96));
    }

    @Test
    void getArtistById_NotFound() throws Exception {
        doReturn(null).when(dataSourceService).getArtistById("invalid_id");

        mockMvc.perform(get("/artists/invalid_id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAlbumById_NotFound() throws Exception {
        doReturn(null).when(dataSourceService).getAlbumById("invalid_album_id");

        mockMvc.perform(get("/albums/invalid_album_id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    void getSongById_NotFound() throws Exception {
        doReturn(null).when(dataSourceService).getSongById("invalid_song_id");

        mockMvc.perform(get("/songs/invalid_song_id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
