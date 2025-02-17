// src/main/java/com/example/catalog/services/JSONDataSourceService.java

package com.example.catalog.services;

import com.example.catalog.model.Album;
import com.example.catalog.model.Artist;
import com.example.catalog.model.Song;
import com.example.catalog.model.Track;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class JSONDataSourceService implements DataSourceService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${json.file.path}")
    private String artistsJsonFilePath;

    @Value("${json.albums.file.path}")
    private String albumsJsonFilePath;

    @Value("${json.songs.file.path}")
    private String songsJsonFilePath;

    // ======== ARTIST OPERATIONS ========
    @Override
    public Artist getArtistById(String id) throws IOException {
        JsonNode artists = loadJsonData(artistsJsonFilePath);
        JsonNode artistNode = artists.get(id);
        return artistNode != null ? objectMapper.treeToValue(artistNode, Artist.class) : null;
    }

    @Override
    public List<Artist> getAllArtists() throws IOException {
        JsonNode artists = loadJsonData(artistsJsonFilePath);
        List<Artist> artistList = new ArrayList<>();
        artists.fieldNames().forEachRemaining(id -> {
            try {
                artistList.add(objectMapper.treeToValue(artists.get(id), Artist.class));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return artistList;
    }

    @Override
    public void createArtist(Artist artist) throws IOException {
        JsonNode artists = loadJsonData(artistsJsonFilePath);
        ((ObjectNode) artists).set(artist.getId(), objectMapper.valueToTree(artist));
        writeJsonData(artistsJsonFilePath, artists);
    }

    @Override
    public void updateArtist(String id, Artist artist) throws IOException {
        JsonNode artists = loadJsonData(artistsJsonFilePath);
        if (artists.has(id)) {
            ((ObjectNode) artists).set(id, objectMapper.valueToTree(artist));
            writeJsonData(artistsJsonFilePath, artists);
        }
    }

    @Override
    public void deleteArtist(String id) throws IOException {
        JsonNode artists = loadJsonData(artistsJsonFilePath);
        ((ObjectNode) artists).remove(id);
        writeJsonData(artistsJsonFilePath, artists);
    }

    // ======== ALBUM OPERATIONS ========
    @Override
    public Album getAlbumById(String id) throws IOException {
        JsonNode albums = loadJsonData(albumsJsonFilePath);
        JsonNode albumNode = albums.get(id);
        return albumNode != null ? objectMapper.treeToValue(albumNode, Album.class) : null;
    }

    @Override
    public List<Album> getAllAlbums() throws IOException {
        JsonNode albums = loadJsonData(albumsJsonFilePath);
        List<Album> albumList = new ArrayList<>();
        albums.fieldNames().forEachRemaining(id -> {
            try {
                albumList.add(objectMapper.treeToValue(albums.get(id), Album.class));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return albumList;
    }

    @Override
    public void createAlbum(Album album) throws IOException {
        JsonNode albums = loadJsonData(albumsJsonFilePath);
        ((ObjectNode) albums).set(album.getId(), objectMapper.valueToTree(album));
        writeJsonData(albumsJsonFilePath, albums);
    }

    @Override
    public void updateAlbum(String id, Album album) throws IOException {
        JsonNode albums = loadJsonData(albumsJsonFilePath);
        if (albums.has(id)) {
            ((ObjectNode) albums).set(id, objectMapper.valueToTree(album));
            writeJsonData(albumsJsonFilePath, albums);
        }
    }

    @Override
    public void deleteAlbum(String id) throws IOException {
        JsonNode albums = loadJsonData(albumsJsonFilePath);
        ((ObjectNode) albums).remove(id);
        writeJsonData(albumsJsonFilePath, albums);
    }

    // ======== SONG OPERATIONS ========
    @Override
    public Song getSongById(String id) throws IOException {
        JsonNode songs = loadJsonData(songsJsonFilePath);

        for (JsonNode songNode : songs) {
            JsonNode songIdNode = songNode.get("id");
            if (songIdNode != null && songIdNode.asText().equals(id)) {
                try {
                    return objectMapper.treeToValue(songNode, Song.class);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null; // Handle malformed song node
                }
            }
        }
        return null; // Song not found
    }



    @Override
    public List<Song> getAllSongs() throws IOException {
        JsonNode songs = loadJsonData(songsJsonFilePath);
        List<Song> songList = new ArrayList<>();
        songs.forEach(songNode -> {
            try {
                songList.add(objectMapper.treeToValue(songNode, Song.class));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return songList;
    }

    @Override
    public void createSong(Song song) throws IOException {
        JsonNode songs = loadJsonData(songsJsonFilePath);

        // Validate ID
        if (song.getId() == null || song.getId().isEmpty()) {
            throw new IllegalArgumentException("Song ID cannot be null or empty.");
        }

        // Check for duplicate ID
        for (JsonNode existingSong : songs) {
            if (existingSong.has("id") && existingSong.get("id").asText().equals(song.getId())) {
                throw new IllegalArgumentException("A song with this ID already exists.");
            }
        }

        // Add the song
        if (!(songs instanceof ArrayNode)) {
            songs = objectMapper.createArrayNode();
        }

        ((ArrayNode) songs).add(objectMapper.valueToTree(song));
        writeJsonData(songsJsonFilePath, songs);
    }


    @Override
    public void updateSong(String id, Song song) throws IOException {
        JsonNode songs = loadJsonData(songsJsonFilePath);

        for (int i = 0; i < songs.size(); i++) {
            JsonNode songNode = songs.get(i);
            JsonNode songIdNode = songNode.get("id");  // Ensure this is not null

            if (songIdNode != null && songIdNode.asText().equals(id)) {
                ((ArrayNode) songs).set(i, objectMapper.valueToTree(song));
                writeJsonData(songsJsonFilePath, songs);
                return;
            }
        }
    }

    @Override
    public void deleteSong(String id) throws IOException {
        JsonNode songs = loadJsonData(songsJsonFilePath);

        for (int i = 0; i < songs.size(); i++) {
            JsonNode songNode = songs.get(i);
            JsonNode songIdNode = songNode.get("id");  // Check if "id" exists

            if (songIdNode != null && songIdNode.asText().equals(id)) {
                ((ArrayNode) songs).remove(i);
                writeJsonData(songsJsonFilePath, songs);
                return;
            }
        }
    }


    @Override
    public List<Track> getAlbumTracks(String albumId) throws IOException {
        JsonNode albums = loadJsonData(albumsJsonFilePath);
        JsonNode albumNode = albums.get(albumId);
        if (albumNode != null && albumNode.has("tracks")) {
            return objectMapper.readValue(albumNode.get("tracks").toString(), objectMapper.getTypeFactory().constructCollectionType(List.class, Track.class));
        }
        return new ArrayList<>();
    }

    @Override
    public Track addTrackToAlbum(String albumId, Track track) throws IOException {
        JsonNode albums = loadJsonData(albumsJsonFilePath);
        JsonNode albumNode = albums.get(albumId);
        if (albumNode != null) {
            ArrayNode tracksArray = (ArrayNode) albumNode.get("tracks");
            if (tracksArray == null) {
                tracksArray = objectMapper.createArrayNode();
                ((ObjectNode) albumNode).set("tracks", tracksArray);
            }
            tracksArray.add(objectMapper.valueToTree(track));
            writeJsonData(albumsJsonFilePath, albums);
            return track;
        }
        return null;
    }


    @Override
    public Track updateAlbumTrack(String albumId, Track track) throws IOException {
        JsonNode albums = loadJsonData(albumsJsonFilePath);
        JsonNode albumNode = albums.get(albumId);
        if (albumNode != null && albumNode.has("tracks")) {
            ArrayNode tracksArray = (ArrayNode) albumNode.get("tracks");
            for (int i = 0; i < tracksArray.size(); i++) {
                JsonNode trackNode = tracksArray.get(i);
                if (trackNode.get("id").asText().equals(track.getId())) {
                    tracksArray.set(i, objectMapper.valueToTree(track));
                    writeJsonData(albumsJsonFilePath, albums);
                    return track;
                }
            }
        }
        return null;
    }


    @Override
    public boolean deleteAlbumTrack(String albumId, String trackId) throws IOException {
        JsonNode albums = loadJsonData(albumsJsonFilePath);
        JsonNode albumNode = albums.get(albumId);
        if (albumNode != null && albumNode.has("tracks")) {
            ArrayNode tracksArray = (ArrayNode) albumNode.get("tracks");
            for (int i = 0; i < tracksArray.size(); i++) {
                JsonNode trackNode = tracksArray.get(i);
                if (trackNode.get("id").asText().equals(trackId)) {
                    tracksArray.remove(i);
                    writeJsonData(albumsJsonFilePath, albums);
                    return true;
                }
            }
        }
        return false;
    }



    @Override
    public List<Album> getArtistAlbums(String artistId) throws IOException {
        JsonNode songs = loadJsonData(songsJsonFilePath);
        JsonNode albums = loadJsonData(albumsJsonFilePath);

        Set<String> albumIds = new HashSet<>();
        List<Album> artistAlbums = new ArrayList<>();

        // Step 1: Find albums where the artist has songs
        for (JsonNode songNode : songs) {
            if (songNode.has("artists")) {
                for (JsonNode artistNode : songNode.get("artists")) {
                    if (artistNode.get("id").asText().equals(artistId)) {
                        String albumId = songNode.get("album").get("id").asText();
                        albumIds.add(albumId);
                    }
                }
            }
        }

        // Step 2: Retrieve album details from albums.json
        for (String albumId : albumIds) {
            JsonNode albumNode = albums.get(albumId);
            if (albumNode != null) {
                artistAlbums.add(objectMapper.treeToValue(albumNode, Album.class));
            }
        }

        return artistAlbums;
    }


    @Override
    public List<Song> getArtistSongs(String artistId) throws IOException {
        JsonNode songs = loadJsonData(songsJsonFilePath);
        List<Song> artistSongs = new ArrayList<>();

        for (JsonNode songNode : songs) {
            if (songNode.has("artists")) {
                for (JsonNode artistNode : songNode.get("artists")) {
                    if (artistNode.get("id").asText().equals(artistId)) {
                        artistSongs.add(objectMapper.treeToValue(songNode, Song.class));
                        break; // Add song only once
                    }
                }
            }
        }

        return artistSongs;
    }


    // ======== HELPER METHODS ========
    private JsonNode loadJsonData(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            objectMapper.writeValue(file, filePath.contains("songs") ? objectMapper.createArrayNode() : objectMapper.createObjectNode());
        }
        return objectMapper.readTree(file);
    }

    private void writeJsonData(String filePath, JsonNode data) throws IOException {
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), data);
    }
}
