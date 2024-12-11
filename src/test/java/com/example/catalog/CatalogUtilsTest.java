package com.example.catalog;

import com.example.catalog.utils.CatalogUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CatalogUtilsTest {

    private CatalogUtils catalogUtils;
    private List<JsonNode> songs;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws Exception {
        catalogUtils = new CatalogUtils();
        objectMapper = new ObjectMapper();

        // Sample song data for testing.
        String jsonData = """ 
                [
                    {
                      "duration_ms": 200040,
                      "name": "Blinding Lights",
                      "popularity": 87,
                      "album": {
                        "name": "After Hours",
                        "release_date": "2020-03-20",
                        "total_tracks": 14
                      },
                      "artists": [
                        {
                          "name": "The Weeknd"
                        }
                      ]
                    },
                    {
                      "duration_ms": 120000,
                      "name": "",
                      "popularity": 0,
                      "album": {
                        "name": "Unknown Album",
                        "release_date": "1900-01-01",
                        "total_tracks": 0
                      },
                      "artists": [
                        {
                          "name": "Unknown Artist"
                        }
                      ]
                    },
                    {
                      "duration_ms": 300000,
                      "name": "Longest Song",
                      "popularity": 99,
                      "album": {
                        "name": "Epic Album",
                        "release_date": "2022-07-15",
                        "total_tracks": 10
                      },
                      "artists": [
                        {
                          "name": "Epic Artist"
                        }
                      ]
                    },
                    {
                      "duration_ms": 180000,
                      "name": "Recent Hit",
                      "popularity": 95,
                      "album": {
                        "name": "Top Charts",
                        "release_date": "2023-11-11",
                        "total_tracks": 12
                      },
                      "artists": [
                        {
                          "name": "Chart Buster"
                        }
                      ]
                    },
                    {
                      "duration_ms": 150000,
                      "name": "Obscure Track",
                      "popularity": 1,
                      "album": {
                        "name": "Hidden Gems",
                        "release_date": "2010-05-10",
                        "total_tracks": 8
                      },
                      "artists": [
                        {
                          "name": "Indie Star"
                        }
                      ]
                    },
                    {
                      "duration_ms": 210000,
                      "name": "New Adventure",
                      "popularity": 88,
                      "album": {
                        "name": "Fresh Horizons",
                        "release_date": "2024-01-01",
                        "total_tracks": 15
                      },
                      "artists": [
                        {
                          "name": "Indie Star"
                        }
                      ]
                    }
                ]
            """;

        songs = new ArrayList<>();
        objectMapper.readTree(jsonData).forEach(songs::add);
    }

    @Test
    void testSortSongsByName() {
        List<JsonNode> sortedSongs = catalogUtils.sortSongsByName(songs);
        List<String> sortedNames = new ArrayList<>();
        for (JsonNode song : sortedSongs) {
            sortedNames.add(song.get("name").asText());
        }
        List<String> expectedNames = List.of("", "Blinding Lights", "Longest Song", "New Adventure", "Obscure Track", "Recent Hit");
        assertEquals(expectedNames, sortedNames);

    }

    @Test
    void testFilterSongsByPopularity() {
        List<JsonNode> filteredSongs = catalogUtils.filterSongsByPopularity(songs, 50);
        assertEquals(4, filteredSongs.size());
        for (JsonNode song : filteredSongs) {
            assertTrue(song.get("popularity").asInt() >= 50);
        }

    }

    @Test
    void testDoesSongExistByName() {
        assertTrue(catalogUtils.doesSongExistByName(songs, "Blinding Lights"));
        assertFalse(catalogUtils.doesSongExistByName(songs, "Nonexistent Song"));
    }

    @Test
    void testCountSongsByArtist() {

        long count = catalogUtils.countSongsByArtist(songs, "Tht");
        assertEquals(0, count);
        count = catalogUtils.countSongsByArtist(songs, "The Weeknd");
        assertEquals(1, count);
        count = catalogUtils.countSongsByArtist(songs, "Indie Star");
        assertEquals(2, count);


    }

    @Test
    void testGetLongestSong() {
        JsonNode longestSong = catalogUtils.getLongestSong(songs);
        assertEquals("Longest Song", longestSong.get("name").asText());
        assertEquals(300000, longestSong.get("duration_ms").asInt());
    }

    @Test
    void testGetSongByYear() {
        List<JsonNode> songsFrom2020 = catalogUtils.getSongByYear(songs, 2020);
        assertEquals(1, songsFrom2020.size());
        assertEquals("Blinding Lights", songsFrom2020.get(0).get("name").asText());

        List<JsonNode> songsFrom2025 = catalogUtils.getSongByYear(songs, 2025);
        assertEquals(0, songsFrom2025.size());
    }

    @Test
    void testGetMostRecentSong() {
        assertEquals(catalogUtils.getMostRecentSong(songs).get("name").asText(),"New Adventure");
        assertEquals("2024-01-01", catalogUtils.getMostRecentSong(songs).get("album").get("release_date").asText());
    }
}
