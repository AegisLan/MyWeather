package com.aegisLan.weather.model;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

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

    public static List<HourForecastInfo> QueryCityHourWeatherForecast(Context context, int id) {
        ContentResolver resolver = context.getContentResolver();
        Uri uri = Uri.parse("content://com.aegisLan.weather.provider.WeatherInfoProvider/ForecastHour");
        Cursor cursor = resolver.query(uri, null, "id = ?", new String[]{"" + id}, null);
        List<HourForecastInfo> list = new ArrayList<>();
        while (cursor != null && cursor.moveToNext()) {
            HourForecastInfo info = new HourForecastInfo();
            info.setId(cursor.getInt(cursor.getColumnIndex("id")));
            info.setTime(cursor.getString(cursor.getColumnIndex("time")));
            info.setTemp(cursor.getInt(cursor.getColumnIndex("temp")));
            info.setHum(cursor.getInt(cursor.getColumnIndex("hum")));
            info.setRainRate(cursor.getInt(cursor.getColumnIndex("rainRate")));
            info.getWindInfo().setDir(cursor.getString(cursor.getColumnIndex("windDir")));
            info.getWindInfo().setDescribe(cursor.getString(cursor.getColumnIndex("windDescribe")));
            info.getWindInfo().setSpeed(cursor.getInt(cursor.getColumnIndex("windSpeed")));
            list.add(info);
        }
        if(cursor != null) cursor.close();
        return list;
    }

    public static List<DayForecastInfo> QueryCityDayWeatherForecast(Context context, int id) {
        ContentResolver resolver = context.getContentResolver();
        Uri uri = Uri.parse("content://com.aegisLan.weather.provider.WeatherInfoProvider/ForecastDay");
        Cursor cursor = resolver.query(uri, null, "id = ?", new String[]{"" + id}, null);
        List<DayForecastInfo> list = new ArrayList<>();
        while (cursor != null && cursor.moveToNext()) {
            DayForecastInfo info = new DayForecastInfo();
            info.setId(cursor.getInt(cursor.getColumnIndex("id")));
            info.setTime(cursor.getString(cursor.getColumnIndex("time")));
            info.setTempMax(cursor.getInt(cursor.getColumnIndex("tempMax")));
            info.setTempMin(cursor.getInt(cursor.getColumnIndex("tempMin")));
            info.setDayStateCode(cursor.getInt(cursor.getColumnIndex("dayStateCode")));
            info.setNightStateCode(cursor.getInt(cursor.getColumnIndex("nightStateCode")));
            info.setDayStateText(cursor.getString(cursor.getColumnIndex("dayState")));
            info.setNightStateText(cursor.getString(cursor.getColumnIndex("nightState")));
            info.setHum(cursor.getInt(cursor.getColumnIndex("hum")));
            info.setRainRate(cursor.getInt(cursor.getColumnIndex("rainRate")));
            info.getWindInfo().setDir(cursor.getString(cursor.getColumnIndex("windDir")));
            info.getWindInfo().setDescribe(cursor.getString(cursor.getColumnIndex("windDescribe")));
            info.getWindInfo().setSpeed(cursor.getInt(cursor.getColumnIndex("windSpeed")));
            list.add(info);
        }
        if(cursor != null) cursor.close();
        return list;
    }

    public static int UpdateHourForecast(Context context, int id, List<HourForecastInfo> list) {
        int count = 0;
        ContentResolver resolver = context.getContentResolver();
        Uri uri = Uri.parse("content://com.aegisLan.weather.provider.WeatherInfoProvider/ForecastHour");
        int deleteCount = resolver.delete(uri, "id = ?", new String[]{"" + id});
        if(list != null) {
            for (HourForecastInfo info: list) {
                ContentValues values = new ContentValues();
                values.put("id", info.getId());
                values.put("time", info.getTime());
                values.put("temp", info.getTemp());
                values.put("hum", info.getHum());
                values.put("rainRate", info.getRainRate());
                values.put("windDir", info.getWindInfo().getDir());
                values.put("windDescribe", info.getWindInfo().getDescribe());
                values.put("windSpeed", info.getWindInfo().getSpeed());
                resolver.insert(uri, values);
                count++;
            }
        }
        return count;
    }

    public static int UpdateDayForecast(Context context, int id, List<DayForecastInfo> list) {
        int count = 0;
        ContentResolver resolver = context.getContentResolver();
        Uri uri = Uri.parse("content://com.aegisLan.weather.provider.WeatherInfoProvider/ForecastDay");
        int deleteCount = resolver.delete(uri, "id = ?", new String[]{"" + id});
        if(list != null) {
            for (DayForecastInfo info: list) {
                ContentValues values = new ContentValues();
                values.put("id", info.getId());
                values.put("time", info.getTime());
                values.put("tempMax", info.getTempMax());
                values.put("tempMin", info.getTempMin());
                values.put("dayStateCode", info.getDayStateCode());
                values.put("nightStateCode", info.getNightStateCode());
                values.put("dayState", info.getDayStateText());
                values.put("nightState", info.getNightStateText());
                values.put("hum", info.getHum());
                values.put("rainRate", info.getRainRate());
                values.put("windDir", info.getWindInfo().getDir());
                values.put("windDescribe", info.getWindInfo().getDescribe());
                values.put("windSpeed", info.getWindInfo().getSpeed());
                resolver.insert(uri, values);
                count++;
            }
        }
        return count;
    }
}
