package com.example.catalog.services;

import com.example.catalog.model.Album;
import com.example.catalog.model.Artist;
import com.example.catalog.model.Image;
import com.example.catalog.model.Song;
import com.example.catalog.model.Track;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class SpotifyAPIDataSourceService implements DataSourceService {

    @Value("${spotify.api.base_url}")
    private String spotifyApiBaseUrl;

    @Value("${spotify.api.token}")
    private String spotifyApiToken;

    private final RestTemplate restTemplate = new RestTemplate();

    // ======== HELPER METHOD FOR HEADERS ========
    private HttpEntity<String> createHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + spotifyApiToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(headers);
    }

    // ======== ARTIST OPERATIONS ========
    @Override
    public Artist getArtistById(String id) throws IOException {
        String url = spotifyApiBaseUrl + "/artists/" + id;
        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, createHttpEntity(), Map.class);
            return mapToArtist(response.getBody());
        } catch (HttpClientErrorException e) {
            throw new IOException("Error fetching artist: " + e.getMessage());
        }
    }

    @Override
    public List<Artist> getAllArtists() {
        throw new UnsupportedOperationException("Fetching all artists is not supported by Spotify API.");
    }

    @Override
    public void createArtist(Artist artist) {
        throw new UnsupportedOperationException("Creating an artist is not supported by Spotify API.");
    }

    @Override
    public void updateArtist(String id, Artist artist) {
        throw new UnsupportedOperationException("Updating an artist is not supported by Spotify API.");
    }

    @Override
    public void deleteArtist(String id) {
        throw new UnsupportedOperationException("Deleting an artist is not supported by Spotify API.");
    }

    // ======== ALBUM OPERATIONS ========
    @Override
    public Album getAlbumById(String id) throws IOException {
        String url = spotifyApiBaseUrl + "/albums/" + id;
        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, createHttpEntity(), Map.class);
            return mapToAlbum(response.getBody());
        } catch (HttpClientErrorException e) {
            throw new IOException("Error fetching album: " + e.getMessage());
        }
    }

    @Override
    public List<Album> getAllAlbums() {
        throw new UnsupportedOperationException("Fetching all albums is not supported by Spotify API.");
    }

    @Override
    public void createAlbum(Album album) {
        throw new UnsupportedOperationException("Creating an album is not supported by Spotify API.");
    }

    @Override
    public void updateAlbum(String id, Album album) {
        throw new UnsupportedOperationException("Updating an album is not supported by Spotify API.");
    }

    @Override
    public void deleteAlbum(String id) {
        throw new UnsupportedOperationException("Deleting an album is not supported by Spotify API.");
    }

    @Override
    public Song getSongById(String id) throws IOException {
        String url = spotifyApiBaseUrl + "/tracks/" + id;

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    createHttpEntity(),
                    (Class<Map<String, Object>>) (Class<?>) Map.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return mapToSong(response.getBody()); // Map the API response to the Song model
            } else {
                throw new IOException("Unexpected response from Spotify API: " + response.getStatusCode());
            }

        } catch (HttpClientErrorException.NotFound e) {
            throw new IOException("Song not found with ID: " + id, e);
        } catch (HttpClientErrorException.Unauthorized e) {
            throw new IOException("Unauthorized request. Check your Bearer token.", e);
        } catch (HttpClientErrorException.BadRequest e) {
            throw new IOException("Invalid song ID format: " + id, e);
        } catch (HttpClientErrorException e) {
            throw new IOException("Error fetching song: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            throw new IOException("Unexpected error occurred: " + e.getMessage(), e);
        }
    }




    @Override
    public List<Song> getAllSongs() {
        throw new UnsupportedOperationException("Fetching all songs is not supported by Spotify API.");
    }

    @Override
    public void createSong(Song song) {
        throw new UnsupportedOperationException("Creating a song is not supported by Spotify API.");
    }

    @Override
    public void updateSong(String id, Song song) {
        throw new UnsupportedOperationException("Updating a song is not supported by Spotify API.");
    }

    @Override
    public void deleteSong(String id) {
        throw new UnsupportedOperationException("Deleting a song is not supported by Spotify API.");
    }

    // ======== TRACK OPERATIONS ========
    @Override
    public List<Track> getAlbumTracks(String albumId) throws IOException {
        String url = spotifyApiBaseUrl + "/albums/" + albumId + "/tracks";

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    createHttpEntity(),
                    (Class<Map<String, Object>>) (Class<?>) Map.class
            );

            List<Map<String, Object>> tracksData = (List<Map<String, Object>>) response.getBody().get("items");
            List<Track> tracks = new ArrayList<>();

            if (tracksData != null) {
                for (Map<String, Object> trackData : tracksData) {
                    tracks.add(mapToTrack(trackData));
                }
            }

            return tracks;

        } catch (HttpClientErrorException.NotFound e) {
            throw new IOException("Album not found with ID: " + albumId, e);
        } catch (HttpClientErrorException.Unauthorized e) {
            throw new IOException("Unauthorized request. Check your Bearer token.", e);
        } catch (HttpClientErrorException.BadRequest e) {
            throw new IOException("Invalid album ID format: " + albumId, e);
        } catch (HttpClientErrorException e) {
            throw new IOException("Error fetching album tracks: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            throw new IOException("Unexpected error occurred: " + e.getMessage(), e);
        }
    }

    @Override
    public Track addTrackToAlbum(String albumId, Track track) {
        throw new UnsupportedOperationException("Adding a track to an album is not supported by Spotify API.");
    }

    @Override
    public Track updateAlbumTrack(String albumId, Track track) {
        throw new UnsupportedOperationException("Updating an album track is not supported by Spotify API.");
    }

    @Override
    public boolean deleteAlbumTrack(String albumId, String trackId) {
        throw new UnsupportedOperationException("Deleting an album track is not supported by Spotify API.");
    }


    @Override
    public List<Album> getArtistAlbums(String artistId) throws IOException {
        String url = spotifyApiBaseUrl + "/artists/" + artistId + "/albums";
        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, createHttpEntity(), Map.class);
            List<Map<String, Object>> albumsData = (List<Map<String, Object>>) response.getBody().get("items");

            List<Album> albums = new ArrayList<>();
            if (albumsData != null) {
                for (Map<String, Object> albumData : albumsData) {
                    albums.add(mapToAlbum(albumData));
                }
            }
            return albums;
        } catch (HttpClientErrorException e) {
            throw new IOException("Error fetching artist albums: " + e.getMessage());
        }
    }

    @Override
    public List<Song> getArtistSongs(String artistId) {
        throw new UnsupportedOperationException("Fetching songs by artist is not supported directly by Spotify API.");
    }




    // ======== MAPPING METHODS ========
    private Artist mapToArtist(Map<String, Object> data) {
        Artist artist = new Artist();
        artist.setId((String) data.get("id"));
        artist.setName((String) data.get("name"));
        artist.setPopularity((int) data.getOrDefault("popularity", 0));
        artist.setUri((String) data.get("uri"));
        return artist;
    }

    private Album mapToAlbum(Map<String, Object> data) {
        Album album = new Album();
        album.setId((String) data.get("id"));
        album.setName((String) data.get("name"));
        album.setUri((String) data.get("uri"));
        album.setRelease_date((String) data.get("release_date"));
        album.setTotal_tracks((int) data.getOrDefault("total_tracks", 0));

        List<Map<String, Object>> imagesData = (List<Map<String, Object>>) data.get("images");
        if (imagesData != null) {
            List<Image> images = new ArrayList<>();
            for (Map<String, Object> imageData : imagesData) {
                Image image = new Image();
                image.setUrl((String) imageData.get("url"));
                image.setHeight((int) imageData.getOrDefault("height", 0));
                image.setWidth((int) imageData.getOrDefault("width", 0));
                images.add(image);
            }
            album.setImages(images);
        }

        return album;
    }

    private Song mapToSong(Map<String, Object> responseBody) {
        Song song = new Song();
        song.setId((String) responseBody.get("id"));
        song.setName((String) responseBody.get("name"));
        song.setDurationMs((Integer) responseBody.get("duration_ms"));
        song.setPopularity((Integer) responseBody.get("popularity"));
        song.setUri((String) responseBody.get("uri"));

        // Handling Album
        Map<String, Object> albumMap = (Map<String, Object>) responseBody.get("album");
        if (albumMap != null) {
            Album album = new Album();
            album.setId((String) albumMap.get("id"));
            album.setName((String) albumMap.get("name"));
            album.setUri((String) albumMap.get("uri"));
            album.setRelease_date((String) albumMap.get("release_date"));
            album.setTotal_tracks(albumMap.get("total_tracks") != null ? (int) albumMap.get("total_tracks") : 0);
            song.setAlbum(album);
        }

        // Handling Artists
        List<Map<String, Object>> artistsList = (List<Map<String, Object>>) responseBody.get("artists");
        if (artistsList != null) {
            List<Artist> artists = artistsList.stream().map(artistMap -> {
                Artist artist = new Artist();
                artist.setId((String) artistMap.get("id"));
                artist.setName((String) artistMap.get("name"));
                artist.setUri((String) artistMap.get("uri"));
                return artist;
            }).toList();
            song.setArtists(artists);
        }

        return song;
    }


    private Track mapToTrack(Map<String, Object> data) {
        Track track = new Track();
        track.setId((String) data.get("id"));
        track.setName((String) data.get("name"));

        // ✅ Safely handle duration_ms conversion
        Object durationObj = data.get("duration_ms");
        if (durationObj instanceof Integer) {
            track.setDuration_ms(((Integer) durationObj).longValue());
        } else if (durationObj instanceof Long) {
            track.setDuration_ms((Long) durationObj);
        } else {
            track.setDuration_ms(0L);  // Default value if missing
        }

        // ✅ Handle explicit field safely
        Object explicitObj = data.get("explicit");
        track.setExplicit(explicitObj instanceof Boolean ? (Boolean) explicitObj : false);

        track.setUri((String) data.get("uri"));
        return track;
    }

}