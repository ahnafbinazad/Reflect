
package com.reflect.reflect.model;

import java.util.LinkedHashMap;
import java.util.Map;



public class Artist {

    private String name;
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

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

}
