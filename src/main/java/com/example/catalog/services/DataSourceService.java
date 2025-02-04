// src/main/java/com/example/catalog/services/DataSourceService.java

package com.example.catalog.services;

import com.example.catalog.model.Artist;
import com.example.catalog.model.Album;
import com.example.catalog.model.Song;
import com.example.catalog.model.Track;

import java.io.IOException;
import java.util.List;

public interface DataSourceService {
    Artist getArtistById(String id) throws IOException;
    List<Artist> getAllArtists() throws IOException;
    void createArtist(Artist artist) throws IOException;
    void updateArtist(String id, Artist artist) throws IOException;
    void deleteArtist(String id) throws IOException;

    Album getAlbumById(String id) throws IOException;
    List<Album> getAllAlbums() throws IOException;
    void createAlbum(Album album) throws IOException;
    void updateAlbum(String id, Album album) throws IOException;
    void deleteAlbum(String id) throws IOException;

    Song getSongById(String id) throws IOException;
    List<Song> getAllSongs() throws IOException;
    void createSong(Song song) throws IOException;
    void updateSong(String id, Song song) throws IOException;
    void deleteSong(String id) throws IOException;


    /*
    List<Track> getAlbumTracks(String albumId) throws IOException;
    Track addTrackToAlbum(String albumId, Track track) throws IOException;
    Track updateAlbumTrack(String albumId, Track track) throws IOException;
    boolean deleteAlbumTrack(String albumId, String trackId) throws IOException;

    List<Album> getArtistAlbums(String artistId) throws IOException;
    List<Song> getArtistSongs(String artistId) throws IOException;

     */
}
