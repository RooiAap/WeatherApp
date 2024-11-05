package com.brendan.weatherapp.view.model;

public class WeatherInfoItem {

    private final String unitName;
    private final String unitValue;
    private final String unitSymbol;

    public WeatherInfoItem() {
        unitName = "";
        unitValue = "";
        unitSymbol = "";
    }

    public WeatherInfoItem(String unitName, String unitValue, String unitSymbol) {
        this.unitName = unitName;
        this.unitValue = unitValue;
        this.unitSymbol = unitSymbol;
    }

    public String getUnitName() {
        return unitName;
    }

    public String getUnitValue() {
        return unitValue;
    }

    public String getUnitSymbol() {
        return unitSymbol;
    }
}
