package com.brendan.weatherapp.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Weather {

    private String location;
    private String mainCondition;
    private Integer temperature;
    private Integer temperatureMin;
    private Integer temperatureMax;
    private String sunriseTime;
    private String sunsetTime;


    public Weather(){
        this.location = null;
        this.mainCondition = null;
        this.temperature = null;
        this.temperatureMin = null;
        this.temperatureMax = null;
        this.sunriseTime = null;
        this.sunsetTime = null;
    }

    public Weather(String location, String mainCondition, int temperature, int temperatureMin, int temperatureMax, String sunriseTime, String sunsetTime) {
        this.location = location;
        this.mainCondition = mainCondition;
        this.temperature = temperature;
        this.temperatureMin = temperatureMin;
        this.temperatureMax = temperatureMax;
        this.sunriseTime = sunriseTime;
        this.sunsetTime = sunsetTime;
    }

    public Weather(Weather other){
        this.location = other.location;
        this.mainCondition = other.mainCondition;
        this.temperature = other.temperature;
        this.temperatureMin = other.temperatureMin;
        this.temperatureMax = other.temperatureMax;
        this.sunriseTime = other.sunriseTime;
        this.sunsetTime = other.sunsetTime;
    }

    public static Weather fromJSON(JSONObject json) throws JSONException {

        JSONObject locationObject = (JSONObject) json.get("location");
        JSONObject currentObject = (JSONObject) json.get("current");
        JSONObject forecastObject = (JSONObject) json.get("forecast");

        JSONArray forecastDayArray = (JSONArray) forecastObject.get("forecastday");
        JSONObject dayObject = (JSONObject) ((JSONObject)forecastDayArray.get(0)).get("day");
        JSONObject astroObject = (JSONObject) ((JSONObject)forecastDayArray.get(0)).get("astro");

        String location = locationObject.getString("name");
        String mainCondition = ((JSONObject)currentObject.get("condition")).getString("text");
        int temperature = currentObject.getInt("temp_c");
        int temperatureMin = dayObject.getInt("mintemp_c");
        int temperatureMax = dayObject.getInt("maxtemp_c");
        String sunriseTime = astroObject.getString("sunrise");
        String sunsetTime = astroObject.getString("sunset");

        return new Weather(
                location,
                mainCondition,
                temperature,
                temperatureMin,
                temperatureMax,
                sunriseTime,
                sunsetTime
        );
    }

    public void set(Weather other){
        this.location = other.location;
        this.mainCondition = other.mainCondition;
        this.temperature = other.temperature;
        this.temperatureMin = other.temperatureMin;
        this.temperatureMax = other.temperatureMax;
        this.sunriseTime = other.sunriseTime;
        this.sunsetTime = other.sunsetTime;
    }

    public String getLocation() {
        return location;
    }

    public String getMainCondition() {
        return mainCondition;
    }

    public Integer getTemperature() {
        return temperature;
    }

    public Integer getTemperatureMin() {
        return temperatureMin;
    }

    public Integer getTemperatureMax() {
        return temperatureMax;
    }

    public String getSunriseTime() {
        return sunriseTime;
    }

    public String getSunsetTime() {
        return sunsetTime;
    }
}
