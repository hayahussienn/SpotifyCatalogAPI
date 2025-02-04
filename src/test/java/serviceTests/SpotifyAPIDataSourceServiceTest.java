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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        artist.setPopularity(95);
        artist.setUri("spotify:artist:3TVXtAsR1Inumwj472S9r4");

        doReturn(artist).when(dataSourceService).getArtistById("3TVXtAsR1Inumwj472S9r4");

        mockMvc.perform(get("/artists/3TVXtAsR1Inumwj472S9r4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Drake"));
    }

    @Test
    void getAlbumById_Success() throws Exception {
        Album album = new Album();
        album.setId("1ATL5GLyefJaxhQzSPVrLX");
        album.setName("Scorpion");
        album.setUri("spotify:album:1ATL5GLyefJaxhQzSPVrLX");
        album.setRelease_date("2018-06-29");
        album.setTotal_tracks(25);

        doReturn(album).when(dataSourceService).getAlbumById("1ATL5GLyefJaxhQzSPVrLX");

        mockMvc.perform(get("/albums/1ATL5GLyefJaxhQzSPVrLX")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Scorpion"));
    }

    @Test
    void getSongById_Success() throws Exception {
        Song song = new Song();
        song.setId("7wtfhZwyrccbfQf0H91uwJ");
        song.setName("God's Plan");
        song.setDurationMs(198000);
        song.setPopularity(92);
        song.setUri("spotify:track:7wtfhZwyrccbfQf0H91uwJ");

        doReturn(song).when(dataSourceService).getSongById("7wtfhZwyrccbfQf0H91uwJ");

        mockMvc.perform(get("/songs/7wtfhZwyrccbfQf0H91uwJ")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("God's Plan"));
    }

    @Test
    void getArtistById_NotFound() throws Exception {
        doReturn(null).when(dataSourceService).getArtistById("invalid_id");

        mockMvc.perform(get("/artists/invalid_id")
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

    @Test
    void getAlbumById_NotFound() throws Exception {
        doReturn(null).when(dataSourceService).getAlbumById("invalid_album_id");

        mockMvc.perform(get("/albums/invalid_album_id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
