// src/main/java/com/example/catalog/controller/ArtistController.java

package com.example.catalog.controller;

import com.example.catalog.model.Artist;
import com.example.catalog.services.DataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/artists")
public class ArtistController {

    private final DataSourceService dataSourceService;

    @Autowired
    public ArtistController(DataSourceService dataSourceService) {
        this.dataSourceService = dataSourceService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Artist> getArtistById(@PathVariable String id) throws IOException {
        Artist artist = dataSourceService.getArtistById(id);
        return artist != null ? ResponseEntity.ok(artist) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<Artist>> getAllArtists() throws IOException {
        List<Artist> artists = dataSourceService.getAllArtists();
        return artists != null ? ResponseEntity.ok(artists) : ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<Void> createArtist(@RequestBody Artist artist) throws IOException {
        dataSourceService.createArtist(artist);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateArtist(@PathVariable String id, @RequestBody Artist artist) throws IOException {
        dataSourceService.updateArtist(id, artist);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArtist(@PathVariable String id) throws IOException {
        dataSourceService.deleteArtist(id);
        return ResponseEntity.ok().build();
    }
}
