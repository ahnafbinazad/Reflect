
package com.reflect.reflect.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;



public class Tracks {

    private List<Item> items;
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
