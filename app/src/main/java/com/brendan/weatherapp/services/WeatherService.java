package com.brendan.weatherapp.services;

import static androidx.core.app.ActivityCompat.requestPermissions;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.brendan.weatherapp.models.Weather;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class WeatherService {

    private final static String baseUrl = "https://api.weatherapi.com/v1/forecast.json?";

    private final Activity activity;
    private final String apiKey;
    private final int permissionCode;
    private final FusedLocationProviderClient fusedLocationProviderClient;
    private final OnSuccessListener<Location> locationListener;

    public WeatherService(Activity activity, String apiKey, int permissionCode, OnSuccessListener<Location> locationListener) {
        this.activity = activity;
        this.apiKey = apiKey;
        this.permissionCode = permissionCode;
        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);
        this.locationListener = locationListener;

        getGPSLocation();
    }

    public Weather getWeather(String location) throws InterruptedException, JSONException, IOException {
        Log.i("WeatherService", "getWeather(): " + location);
        String url = String.format("%skey=%s&q=%s&days=1&aqi=no&alerts=no", baseUrl, apiKey, location);

        JsonDataRunnable runnable = new JsonDataRunnable(url);
        Thread jsonDataThread = new Thread(runnable);
        jsonDataThread.start();
        jsonDataThread.join();

        return Weather.fromJSON(runnable.getValue());
    }

    private void getGPSLocation() {

        if(ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            Task<Location> locationTask = fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, null);
            locationTask.addOnSuccessListener(locationListener);

        }else{
            requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, permissionCode);

            if(ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

                Task<Location> locationTask = fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, null);
                locationTask.addOnSuccessListener(locationListener);

            }
        }
    }
}
