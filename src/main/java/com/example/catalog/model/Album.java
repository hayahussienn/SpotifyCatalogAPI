package com.example.catalog.model;

import java.util.List;

public class Album {
    private String id;
    private String name;
    private String uri;
    private String release_date;
    private int total_tracks;
    private List<Image> images;
    private List<Track> tracks;


    // Constructor with parameters
    public Album(String id, String name, String uri, String release_date, int total_tracks, List<Image> images, List<Track> tracks) {
        this.id = id;
        this.name = name;
        this.uri = uri;
        this.release_date = release_date;
        this.total_tracks = total_tracks;
        this.images = images;
        this.tracks = tracks;
    }

    // Default constructor
    public Album() {}

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public int getTotal_tracks() {
        return total_tracks;
    }

    public void setTotal_tracks(int total_tracks) {
        this.total_tracks = total_tracks;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }
}


