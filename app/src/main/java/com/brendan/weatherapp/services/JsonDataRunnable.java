package com.brendan.weatherapp.services;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class JsonDataRunnable implements Runnable{

    private String url;
    private JSONObject json;

    public JsonDataRunnable(String url) {
        this.url = url;
    }

    @Override
    public void run() {
        try {
            json = NetworkData.getJSONObject(url);
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public JSONObject getValue() {
        return json;
    }

}
