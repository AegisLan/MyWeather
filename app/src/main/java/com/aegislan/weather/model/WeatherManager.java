package com.aegisLan.weather.model;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by AegisLan on 2016.1.17.
 */
public class WeatherManager {
    public static void AddCityWeather(Context context, WeatherInfo info) {
        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put("id", info.getId());
        values.put("name", info.getName());
        values.put("temp", info.getTemp());
        values.put("state", info.getState());
        values.put("wind", info.getWind());
        values.put("windStrong", info.getWindStrong());
        values.put("time", info.getTime());
        Uri uri = Uri.parse("content://com.aegisLan.weather.provider.WeatherInfoProvider/WeatherDay/" + info.getId());
        resolver.insert(uri, values);
    }

    public static void RemoveCityWeather(Context context, WeatherInfo info) {
        ContentResolver resolver = context.getContentResolver();
        Uri uri = Uri.parse("content://com.aegisLan.weather.provider.WeatherInfoProvider/WeatherDay/" + info.getId());
        resolver.delete(uri, null, null);
    }

    public static int UpdateCityWeather(Context context, WeatherInfo info) {
        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put("id", info.getId());
        values.put("name", info.getName());
        values.put("temp", info.getTemp());
        values.put("state", info.getState());
        values.put("wind", info.getWind());
        values.put("windStrong", info.getWindStrong());
        values.put("time", info.getTime());
        Uri uri = Uri.parse("content://com.aegisLan.weather.provider.WeatherInfoProvider/WeatherDay/" + info.getId());
        int count = resolver.update(uri, values, null, null);
        return count;
    }

    public static WeatherInfo QueryCityWeather(Context context, int id) {
        ContentResolver resolver = context.getContentResolver();
        Uri uri = Uri.parse("content://com.aegisLan.weather.provider.WeatherInfoProvider/WeatherDay/" + id);
        Cursor cursor = resolver.query(uri, null, null, null, null);
        WeatherInfo info = null;
        if(cursor != null && cursor.moveToNext()) {
            info = new WeatherInfo();
            info.setId(cursor.getInt(cursor.getColumnIndex("id")));
            info.setName(cursor.getString(cursor.getColumnIndex("name")));
            info.setTemp(cursor.getInt(cursor.getColumnIndex("temp")));
            info.setState(cursor.getString(cursor.getColumnIndex("state")));
            info.setWind(cursor.getString(cursor.getColumnIndex("wind")));
            info.setWindStrong(cursor.getString(cursor.getColumnIndex("windStrong")));
            info.setTime(cursor.getString(cursor.getColumnIndex("time")));
            cursor.close();
        }
        return info;
    }
}
