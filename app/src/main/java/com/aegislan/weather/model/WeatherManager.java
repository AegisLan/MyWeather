package com.aegislan.weather.model;

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
        values.put("id", info.getCityId());
        values.put("city", info.getCity());
        values.put("temp", info.getTemp());
        values.put("wind", info.getWind());
        values.put("windStrong", info.getWindStrong());
        values.put("humidity", info.getHumidity());
        values.put("time", info.getTime());
        values.put("isRadar", info.isRadar());
        values.put("Radar", info.getRadar());
        values.put("njd", info.getNjd());
        values.put("qy", info.getQy());
        Uri uri = Uri.parse("content://com.aegislan.weather.provider.WeatherInfoProvider/WeatherDay");
        resolver.insert(uri, values);
    }

    public static void RemoveCityWeather(Context context, WeatherInfo info) {
        ContentResolver resolver = context.getContentResolver();
        Uri uri = Uri.parse("content://com.aegislan.weather.provider.WeatherInfoProvider/WeatherDay");
        resolver.delete(uri, "id = ?", new String[]{info.getCityId() + ""});
    }

    public static void UpdateCityWeather(Context context, WeatherInfo info) {
        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put("id", info.getCityId());
        values.put("city", info.getCity());
        values.put("temp", info.getTemp());
        values.put("wind", info.getWind());
        values.put("windStrong", info.getWindStrong());
        values.put("humidity", info.getHumidity());
        values.put("time", info.getTime());
        values.put("isRadar", info.isRadar());
        values.put("Radar", info.getRadar());
        values.put("njd", info.getNjd());
        values.put("qy", info.getQy());
        Uri uri = Uri.parse("content://com.aegislan.weather.provider.WeatherInfoProvider/WeatherDay");
        resolver.update(uri, values, "id = ?", new String[]{info.getCityId() + ""});
    }

    public static WeatherInfo QueryCityWeather(Context context, int id) {
        ContentResolver resolver = context.getContentResolver();
        Uri uri = Uri.parse("content://com.aegislan.weather.provider.WeatherInfoProvider/WeatherDay");
        Cursor cursor = resolver.query(uri, null, "id = ?", new String[]{id + ""}, null);
        WeatherInfo info = null;
        if(cursor != null && cursor.moveToNext()) {
            info = new WeatherInfo();
            info.setCityId(cursor.getInt(cursor.getColumnIndex("id")));
            info.setCity(cursor.getString(cursor.getColumnIndex("city")));
            info.setTemp(cursor.getInt(cursor.getColumnIndex("temp")));
            info.setWind(cursor.getString(cursor.getColumnIndex("wind")));
            info.setWindStrong(cursor.getString(cursor.getColumnIndex("windStrong")));
            info.setHumidity(cursor.getString(cursor.getColumnIndex("humidity")));
            info.setTime(cursor.getString(cursor.getColumnIndex("time")));
            if(cursor.getInt(cursor.getColumnIndex("isRadar")) == 1 ) {
                info.setIsRadar(true);
            } else {
                info.setIsRadar(false);
            }
            info.setRadar(cursor.getString(cursor.getColumnIndex("Radar")));
            info.setNjd(cursor.getString(cursor.getColumnIndex("njd")));
            info.setQy(cursor.getString(cursor.getColumnIndex("qy")));
            cursor.close();
        }
        return info;
    }
}
