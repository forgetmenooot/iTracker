package com.bugztracker.web.helpers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Response {

    private Map<String, Object> response;

    public Response(Map<String, Object> response) {
        this.response = new ConcurrentHashMap<>(response);
    }

    public Response() {
        response = new ConcurrentHashMap<>();
    }

    public void add(String key, Object value) {
        response.put(key, value);
    }

    public boolean contains(String key) {
        return response.containsKey(key);
    }

    public Object get(String key) {
        return response.get(key);
    }

    public Map<String, Object> getResponse() {
        return response;
    }

    public boolean isEmpty() {
        return response.isEmpty();
    }
}
