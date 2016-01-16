package com.aegislan.weather.model;

import com.aegislan.weather.WeatherApplication;
import com.aegislan.weather.db.WeatherDB;

/**
 * Created by AegisLan on 2016.1.16.
 */
public class CityProvider {
    private static CityProvider instance;
    private WeatherDB db;

    private CityProvider() {
        db = WeatherDB.getInstance(WeatherApplication.getAppContext());
    }

    public static synchronized CityProvider getInstance() {
        if (instance == null) {
            instance = new CityProvider();
        }
        return instance;
    }

    public City getCityInfoByName(String name) {
        City city = db.getCity(name);
        return city;
    }
}
