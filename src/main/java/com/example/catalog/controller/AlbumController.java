// src/main/java/com/example/catalog/controller/AlbumController.java

package com.example.catalog.controller;

import com.example.catalog.model.Album;
import com.example.catalog.model.Track;
import com.example.catalog.services.DataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/albums")
public class AlbumController {

    private final DataSourceService dataSourceService;

    @Autowired
    public AlbumController(DataSourceService dataSourceService) {
        this.dataSourceService = dataSourceService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Album> getAlbumById(@PathVariable String id) throws IOException {
        Album album = dataSourceService.getAlbumById(id);
        return album != null ? ResponseEntity.ok(album) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<Album>> getAllAlbums() throws IOException {
        List<Album> albums = dataSourceService.getAllAlbums();
        return albums != null ? ResponseEntity.ok(albums) : ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<Void> createAlbum(@RequestBody Album album) throws IOException {
        dataSourceService.createAlbum(album);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateAlbum(@PathVariable String id, @RequestBody Album album) throws IOException {
        dataSourceService.updateAlbum(id, album);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlbum(@PathVariable String id) throws IOException {
        dataSourceService.deleteAlbum(id);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/{id}/tracks")
    public ResponseEntity<List<Track>> getAlbumTracks(@PathVariable String id) throws IOException {
        List<Track> tracks = dataSourceService.getAlbumTracks(id);
        return tracks != null ? ResponseEntity.ok(tracks) : ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/tracks")
    public ResponseEntity<Track> addTrackToAlbum(@PathVariable String id, @RequestBody Track track) throws IOException {
        Track addedTrack = dataSourceService.addTrackToAlbum(id, track);
        return addedTrack != null ? ResponseEntity.ok(addedTrack) : ResponseEntity.badRequest().build();
    }

    @PutMapping("/{id}/tracks/{track_id}")
    public ResponseEntity<Track> updateAlbumTrack(@PathVariable String id, @PathVariable String track_id, @RequestBody Track track) throws IOException {
        track.setId(track_id);
        Track updatedTrack = dataSourceService.updateAlbumTrack(id, track);
        return updatedTrack != null ? ResponseEntity.ok(updatedTrack) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}/tracks/{track_id}")
    public ResponseEntity<Void> deleteAlbumTrack(@PathVariable String id, @PathVariable String track_id) throws IOException {
        boolean deleted = dataSourceService.deleteAlbumTrack(id, track_id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
