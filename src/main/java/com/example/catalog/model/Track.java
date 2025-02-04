package com.example.catalog.model;

public class Track {
    private long duration_ms;
    private String id;
    private String name;
    private boolean explicit;
    private String uri;

    // Constructor with parameters
    public Track(String id, String name, long duration_ms, boolean explicit, String uri) {
        this.id = id;
        this.name = name;
        this.duration_ms = duration_ms;
        this.explicit = explicit;
        this.uri = uri;
    }

    // Default constructor
    public Track() {}

    // Getters and Setters
    public long getDuration_ms() {
        return duration_ms;
    }

    public void setDuration_ms(long duration_ms) {
        this.duration_ms = duration_ms;
    }

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

    public boolean isExplicit() {
        return explicit;
    }

    public void setExplicit(boolean explicit) {
        this.explicit = explicit;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
