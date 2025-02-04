// src/main/java/com/example/catalog/controller/SongController.java

package com.example.catalog.controller;

import com.example.catalog.model.Song;
import com.example.catalog.services.DataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/songs")
public class SongController {

    private final DataSourceService dataSourceService;

    @Autowired
    public SongController(DataSourceService dataSourceService) {
        this.dataSourceService = dataSourceService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Song> getSongById(@PathVariable String id) throws IOException {
        Song song = dataSourceService.getSongById(id);
        return song != null ? ResponseEntity.ok(song) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<Song>> getAllSongs() throws IOException {
        List<Song> songs = dataSourceService.getAllSongs();
        return songs != null ? ResponseEntity.ok(songs) : ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<Void> createSong(@RequestBody Song song) throws IOException {
        dataSourceService.createSong(song);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateSong(@PathVariable String id, @RequestBody Song song) throws IOException {
        dataSourceService.updateSong(id, song);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSong(@PathVariable String id) throws IOException {
        dataSourceService.deleteSong(id);
        return ResponseEntity.ok().build();
    }
}