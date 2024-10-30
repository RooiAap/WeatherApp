package com.brendan.weatherapp.services;

import static androidx.core.app.ActivityCompat.requestPermissions;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.brendan.weatherapp.BuildConfig;
import com.brendan.weatherapp.models.Weather;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;

public class WeatherService {

    public static Weather getWeather(String location, String apiKey) throws InterruptedException, JSONException {
        Log.i("WeatherService", "getWeather(): " + location);
        String baseUrl = BuildConfig.BASE_WEATHER_URL;
        String url = String.format("%skey=%s&q=%s&days=1&aqi=no&alerts=no", baseUrl, apiKey, location);

        JsonDataRunnable runnable = new JsonDataRunnable(url);
        Thread jsonDataThread = new Thread(runnable);
        jsonDataThread.start();
        jsonDataThread.join();

        return Weather.fromJSON(runnable.getValue());
    }

    public static void getGPSLocation(Activity activity, int permissionCode, OnSuccessListener<Location> locationListener) {
        final FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, permissionCode);
        }
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Task<Location> locationTask = fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, null);
            locationTask.addOnSuccessListener(locationListener);
        }
    }
}
