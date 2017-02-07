package org.query.expansion.models;

import com.google.gson.Gson;

public class Response {
    private Photo photo;
    private int status;

    public static Response fromJson(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, Response.class);
    }
}
