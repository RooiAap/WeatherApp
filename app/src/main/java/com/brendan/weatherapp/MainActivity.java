package com.brendan.weatherapp;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.databinding.DataBindingUtil;

import com.brendan.weatherapp.databinding.ActivityMainBinding;
import com.brendan.weatherapp.models.Weather;
import com.brendan.weatherapp.services.WeatherService;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private final static int FINE_LOCATION_CODE = 1;
    private final static int BOTTOM_SHEET_OFFSET = 200;
    private final static int BOTTOM_SHEET_PEEK = 150;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);

        // Get UI binding for activity
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // Set up bottom sheet behaviour
        BottomSheetBehavior<FrameLayout> bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet);
        bottomSheetBehavior.setFitToContents(false);
        bottomSheetBehavior.setExpandedOffset(BOTTOM_SHEET_OFFSET);
        bottomSheetBehavior.setPeekHeight(BOTTOM_SHEET_PEEK, true);
        bottomSheetBehavior.setHideable(false);
        bottomSheetBehavior.setSaveFlags(BottomSheetBehavior.SAVE_FIT_TO_CONTENTS | BottomSheetBehavior.SAVE_PEEK_HEIGHT | BottomSheetBehavior.SAVE_HIDEABLE);
        OnBackPressedCallback bottomSheetBackCallback = new OnBackPressedCallback(false) {
            @Override
            public void handleOnBackPressed() {
                bottomSheetBehavior.handleBackInvoked();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, bottomSheetBackCallback);
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED || newState == BottomSheetBehavior.STATE_HALF_EXPANDED) {
                    bottomSheetBackCallback.setEnabled(true);
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED || newState == BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheetBackCallback.setEnabled(false);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        // Get initial weather data
        updateCurrentWeatherData();
    }

    private void updateCurrentWeatherData() {
        WeatherService.getGPSLocation(this, FINE_LOCATION_CODE, location -> {
            if (location != null) {
                String lat = Double.toString(location.getLatitude());
                String lon = Double.toString(location.getLongitude());

                String coordinates = String.format(Locale.getDefault(), "%s, %s", lat, lon);
                Log.i("WeatherService", coordinates);

                try {
                    updateWeatherViews(WeatherService.getWeather(coordinates, BuildConfig.API_KEY));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void updateWeatherViews(Weather weather) {
        binding.locationText.setText(weather.getLocation());
        binding.temperatureText.setText(String.format(Locale.getDefault(), "%d°C", weather.getTemperature()));
        binding.conditionText.setText(weather.getMainCondition());
        binding.sunriseDataText.setText(weather.getSunriseTime());
        binding.sunsetDataText.setText(weather.getSunsetTime());
        binding.tempMinDataText.setText(String.format(Locale.getDefault(), "%d°C", weather.getTemperatureMin()));
        binding.tempMaxDataText.setText(String.format(Locale.getDefault(), "%d°C", weather.getTemperatureMax()));
        binding.conditionImage.setImageResource(getConditionImageId(weather.getMainCondition()));
    }

    private int getConditionImageId(String condition) {

        boolean sunCondition = Objects.equals(condition, "Sunny");

        boolean moonCondition = Objects.equals(condition, "Clear");

        boolean partlyCloudyCondition = Objects.equals(condition, "Partly cloudy");

        boolean cloudyCondition = (
                Objects.equals(condition, "Cloudy") ||
                Objects.equals(condition, "Overcast") ||
                Objects.equals(condition, "Mist") ||
                Objects.equals(condition, "Fog") ||
                Objects.equals(condition, "Freezing fog") ||
                Objects.equals(condition, "Blizzard")
        );

        boolean partlyRainyCondition = (
                Objects.equals(condition, "Patchy rain possible") ||
                Objects.equals(condition, "Patchy light drizzle") ||
                Objects.equals(condition, "Patchy light rain")
        );

        boolean rainCondition = (
                Objects.equals(condition, "Light drizzle") ||
                Objects.equals(condition, "Freezing drizzle") ||
                Objects.equals(condition, "Heavy freezing drizzle") ||
                Objects.equals(condition, "Light rain") ||
                Objects.equals(condition, "Moderate rain at times") ||
                Objects.equals(condition, "Moderate rain") ||
                Objects.equals(condition, "Heavy rain at times") ||
                Objects.equals(condition, "Heavy rain") ||
                Objects.equals(condition, "Light freezing rain") ||
                Objects.equals(condition, "Moderate or heavy freezing rain") ||
                Objects.equals(condition, "Light rain shower") ||
                Objects.equals(condition, "Moderate or heavy rain shower") ||
                Objects.equals(condition, "Torrential rain shower")
        );

        boolean thunderstormCondition = (
                Objects.equals(condition, "Patchy light rain with thunder") ||
                Objects.equals(condition, "Moderate or heavy rain with thunder")
        );

        if (sunCondition) {
            return R.drawable.sun;
        } else if (moonCondition) {
            return R.drawable.moon;
        } else if (partlyCloudyCondition) {
            return R.drawable.partly_cloudy;
        } else if (cloudyCondition) {
            return R.drawable.cloud;
        } else if (partlyRainyCondition) {
            return R.drawable.partly_rainy;
        } else if (rainCondition) {
            return R.drawable.rain;
        } else if (thunderstormCondition) {
            return R.drawable.thunderstorm;
        } else {
            return R.drawable.partly_cloudy;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_LOCATION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                updateCurrentWeatherData();
            }
        }
    }
}