package com.example.catalog.controller;

import com.example.catalog.utils.CatalogUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import com.example.catalog.utils.SpotifyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.example.catalog.model.Artist;


@RestController
public class CatalogController {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CatalogUtils catalogUtils = new CatalogUtils();

    @GetMapping("/popularSongs")
    public ResponseEntity<JsonNode> getPopularSongs(@RequestParam(defaultValue = "0") int offset,
                                                    @RequestParam(defaultValue = "5") int limit) throws IOException {
        try {
            // Load the songs data
            ClassPathResource resource = new ClassPathResource("data/popular_songs.json");
            File file = resource.getFile();
            JsonNode songs = objectMapper.readTree(file);

            if (songs.isEmpty()) {
                ObjectNode errorResponse = objectMapper.createObjectNode();
                errorResponse.put("message", "No popular songs available");
                errorResponse.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
                return new ResponseEntity<>(errorResponse, HttpStatus.SERVICE_UNAVAILABLE);
            }

            // Convert JsonNode to List<JsonNode>
            List<JsonNode> songList = new ArrayList<>();
            songs.forEach(songList::add);

            // Apply pagination
            int totalSongs = songList.size();
            if (offset > totalSongs) {
                ObjectNode errorResponse = objectMapper.createObjectNode();
                errorResponse.put("message", "Offset is greater than the total number of songs");
                errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }

            // Ensure the limit is within a valid range
            if (limit <= 0) {
                ObjectNode errorResponse = objectMapper.createObjectNode();
                errorResponse.put("message", "Limit should be greater than zero");
                errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }

            // Calculate the sublist range based on offset and limit
            int endIndex = Math.min(offset + limit, totalSongs);
            List<JsonNode> paginatedSongs = songList.subList(offset, endIndex);

            // Convert paginated list back to JsonNode
            JsonNode paginatedSongsNode = objectMapper.valueToTree(paginatedSongs);

            // Return the paginated list of songs
            return new ResponseEntity<>(paginatedSongsNode, HttpStatus.OK);

        } catch (IOException e) {
            ObjectNode errorResponse = objectMapper.createObjectNode();
            errorResponse.put("message", "Internal server error");
            errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/popularArtists")
    public ResponseEntity<JsonNode> getPopularArtists() {
        try {
            ClassPathResource resource = new ClassPathResource("data/popular_artists.json");
            File file = resource.getFile();
            return new ResponseEntity<>(objectMapper.readTree(file), HttpStatus.OK);
        } catch (IOException e) {
            ObjectNode errorResponse = objectMapper.createObjectNode();
            errorResponse.put("message", "Internal server error");
            errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/albums/{id}")
    public ResponseEntity<JsonNode> getAlbumById(@PathVariable String id) {
        try {
            if (!SpotifyUtils.isValidId(id)) {
                ObjectNode errorResponse = objectMapper.createObjectNode();
                errorResponse.put("message", "Invalid ID");
                errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }

            int is_not_zero = 0;
            for (int i = 0; i < id.length(); i++) {
                if (id.charAt(i) != '0') {
                    is_not_zero = 1;
                    break;
                }
            }


            if (is_not_zero == 0) {
                ObjectNode errorResponse = objectMapper.createObjectNode();
                errorResponse.put("message", "Album ID is forbidden");
                errorResponse.put("status", HttpStatus.FORBIDDEN.value());
                return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
            }

            ClassPathResource resource = new ClassPathResource("data/albums.json");
            JsonNode albums = objectMapper.readTree(resource.getFile());
            JsonNode album = albums.get(id);
            if (album != null) {
                return new ResponseEntity<>(album, HttpStatus.OK);
            } else {
                ObjectNode errorResponse = objectMapper.createObjectNode();
                errorResponse.put("message", "Album not found");
                errorResponse.put("status", HttpStatus.NOT_FOUND.value());
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            ObjectNode errorResponse = objectMapper.createObjectNode();
            errorResponse.put("message", "Internal server error");
            errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/popularSongs/filter")
    public ResponseEntity<JsonNode> filterSongs(@RequestParam(required = false) String name,
                                                @RequestParam(required = false, defaultValue = "0") int minPopularity) {
        try {
            // Load the songs data
            ClassPathResource resource = new ClassPathResource("data/popular_songs.json");
            JsonNode songsNode = objectMapper.readTree(resource.getFile());

            // Convert JsonNode to List<JsonNode>
            List<JsonNode> songs = new ArrayList<>();
            songsNode.elements().forEachRemaining(songs::add);

            // Apply filters using CatalogUtils
            if (name != null && !name.isEmpty()) {
                songs = songs.stream()
                        .filter(song -> song.get("name").asText().equalsIgnoreCase(name))
                        .collect(Collectors.toList());
            }

            if (minPopularity > 0) {
                songs = catalogUtils.filterSongsByPopularity(songs, minPopularity);
            }

            // Check if no songs matched the filter criteria
            if (songs.isEmpty()) {
                ObjectNode errorResponse = objectMapper.createObjectNode();
                errorResponse.put("message", "No songs matched the filter criteria");
                errorResponse.put("status", HttpStatus.NOT_FOUND.value());
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }

            // Convert the filtered list back to JsonNode
            JsonNode filteredSongsNode = objectMapper.valueToTree(songs);

            // Return the filtered songs
            return new ResponseEntity<>(filteredSongsNode, HttpStatus.OK);

        } catch (IOException e) {
            ObjectNode errorResponse = objectMapper.createObjectNode();
            errorResponse.put("message", "Internal server error");
            errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/songs/mostRecent")
    public ResponseEntity<JsonNode> getMostRecentSong() {
        try {
            // Load the songs data
            ClassPathResource resource = new ClassPathResource("data/popular_songs.json");
            JsonNode songsNode = objectMapper.readTree(resource.getFile());

            // Convert JsonNode to List<JsonNode>
            List<JsonNode> songs = new ArrayList<>();
            Iterator<JsonNode> iterator = songsNode.elements();
            iterator.forEachRemaining(songs::add);

            // Get the most recent song
            JsonNode mostRecentSong = catalogUtils.getMostRecentSong(songs);
            if (mostRecentSong == null) {
                ObjectNode errorResponse = objectMapper.createObjectNode();
                errorResponse.put("message", "No songs available");
                errorResponse.put("status", HttpStatus.NOT_FOUND.value());
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(mostRecentSong, HttpStatus.OK);
        } catch (IOException e) {
            ObjectNode errorResponse = objectMapper.createObjectNode();
            errorResponse.put("message", "Internal server error");
            errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/songs/longest")
    public ResponseEntity<JsonNode> getLongestSong() {
        try {
            // Load the songs data
            ClassPathResource resource = new ClassPathResource("data/popular_songs.json");
            JsonNode songsNode = objectMapper.readTree(resource.getFile());

            // Convert JsonNode to List<JsonNode>
            List<JsonNode> songs = new ArrayList<>();
            Iterator<JsonNode> iterator = songsNode.elements();
            iterator.forEachRemaining(songs::add);

            // Get the longest song
            JsonNode longestSong = CatalogUtils.getLongestSong(songs);
            if (longestSong == null) {
                ObjectNode errorResponse = objectMapper.createObjectNode();
                errorResponse.put("message", "No songs available");
                errorResponse.put("status", HttpStatus.NOT_FOUND.value());
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(longestSong, HttpStatus.OK);
        } catch (IOException e) {
            ObjectNode errorResponse = objectMapper.createObjectNode();
            errorResponse.put("message", "Internal server error");
            errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("CatalogController is working!");
    }


    @PostMapping("/artists")
    public ResponseEntity<JsonNode> addArtist(@RequestBody Artist artist) {
        try {
            // Validate the artist object
            if (artist.getId() == null || artist.getName() == null) {
                ObjectNode errorResponse = objectMapper.createObjectNode();
                errorResponse.put("message", "Artist ID and name are required");
                errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }

            // Load existing artist data from popular_artists.json
            ClassPathResource resource = new ClassPathResource("data/popular_artists.json");
            File file = resource.getFile();

            // Read the existing JSON as a JsonNode
            JsonNode artistsNode = objectMapper.readTree(file);

            // Check if it's not already an ObjectNode
            if (!(artistsNode instanceof ObjectNode)) {
                artistsNode = objectMapper.createObjectNode();
            }

            // Check if artist already exists
            if (artistsNode.has(artist.getId())) {
                ObjectNode errorResponse = objectMapper.createObjectNode();
                errorResponse.put("message", "Artist already exists");
                errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }

            // Convert the Artist object to JsonNode and add it to the artists
            JsonNode artistNode = objectMapper.valueToTree(artist);
            ((ObjectNode) artistsNode).set(artist.getId(), artistNode);

            // Write the updated data back to the file
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, artistsNode);

            // Return success response
            return new ResponseEntity<>(artistNode, HttpStatus.CREATED);

        } catch (IOException e) {
            e.printStackTrace();
            ObjectNode errorResponse = objectMapper.createObjectNode();
            errorResponse.put("message", "Internal server error: " + e.getMessage());
            errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}