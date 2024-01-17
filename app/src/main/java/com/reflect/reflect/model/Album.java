
package com.reflect.reflect.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;



public class Album {

    private List<Image> images;
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
