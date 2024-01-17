
package com.reflect.reflect.model;

import androidx.annotation.NonNull;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;



public class Item {

    private Album album;
    private List<Artist> artists;
    private String name;
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
