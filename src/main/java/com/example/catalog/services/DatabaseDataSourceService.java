// src/main/java/com/example/catalog/services/DatabaseDataSourceService.java

package com.example.catalog.services;

import com.example.catalog.model.Album;
import com.example.catalog.model.Artist;
import com.example.catalog.model.Song;
import com.example.catalog.model.Track;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class DatabaseDataSourceService implements DataSourceService {

    @Override
    public Artist getArtistById(String id) throws IOException {
        return null;
    }

    @Override
    public List<Artist> getAllArtists() throws IOException {
        return null;
    }

    @Override
    public void createArtist(Artist artist) throws IOException {
        // No operation
    }

    @Override
    public void updateArtist(String id, Artist artist) throws IOException {
        // No operation
    }

    @Override
    public void deleteArtist(String id) throws IOException {
        // No operation
    }

    @Override
    public Album getAlbumById(String id) throws IOException {
        return null;
    }

    @Override
    public List<Album> getAllAlbums() throws IOException {
        return null;
    }

    @Override
    public void createAlbum(Album album) throws IOException {
        // No operation
    }

    @Override
    public void updateAlbum(String id, Album album) throws IOException {
        // No operation
    }

    @Override
    public void deleteAlbum(String id) throws IOException {
        // No operation
    }

    @Override
    public Song getSongById(String id) throws IOException {
        return null;
    }

    @Override
    public List<Song> getAllSongs() throws IOException {
        return null;
    }

    @Override
    public void createSong(Song song) throws IOException {
        // No operation
    }

    @Override
    public void updateSong(String id, Song song) throws IOException {
        // No operation
    }

    @Override
    public void deleteSong(String id) throws IOException {
        // No operation
    }

    /*
    public List<Track> getAlbumTracks(String albumId) throws IOException {
        return null;
    }

    public Track addTrackToAlbum(String albumId, Track track) throws IOException {
        return null;
    }

    public Track updateAlbumTrack(String albumId, Track track) throws IOException {
        return null;
    }

    public boolean deleteAlbumTrack(String albumId, String trackId) throws IOException {
        return false;
    }

    public List<Album> getArtistAlbums(String artistId) throws IOException {
        return null;
    }

    public List<Song> getArtistSongs(String artistId) throws IOException {
        return null;
    }

     */
}
