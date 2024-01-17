
package com.reflect.reflect.model;

import java.util.LinkedHashMap;
import java.util.Map;



public class SpotifyRecords {

    private Tracks tracks;
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    public Tracks getTracks() {
        return tracks;
    }

    public void setTracks(Tracks tracks) {
        this.tracks = tracks;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
