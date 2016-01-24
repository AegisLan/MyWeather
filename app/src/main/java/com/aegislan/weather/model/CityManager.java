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
public class CityManager {

    public static City FindCity(Context context,String cityName) {
        ContentResolver resolver = context.getContentResolver();
        Uri uri = Uri.parse("content://com.aegisLan.weather.provider.CityProvider/City");
        Cursor cursor = resolver.query(uri, null, "name = ?", new String[]{cityName}, null);
        City city = null;
        if(cursor != null && cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String name  = cursor.getString(cursor.getColumnIndex("name"));
            String pinyin  = cursor.getString(cursor.getColumnIndex("pinyin"));
            String province  = cursor.getString(cursor.getColumnIndex("province"));
            city = new City(id,name,pinyin,province);
            cursor.close();
        }
        return city;
    }

    public static List<String> SearchCity(Context context,String cityName) {
        List<String> cityList = new ArrayList<>();
        ContentResolver resolver = context.getContentResolver();
        Uri uri = Uri.parse("content://com.aegisLan.weather.provider.CityProvider/City");
        Cursor cursor = resolver.query(uri, null, "name like ?", new String[]{cityName+"%"}, null);
        while (cursor != null && cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            cityList.add(name);
        }
        if(cursor != null) {
            cursor.close();
        }
        return cityList;
    }

    public static void AddCity(Context context,City city) {
        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put("id",city.getId());
        values.put("name",city.getName());
        Uri uri = Uri.parse("content://com.aegisLan.weather.provider.WeatherInfoProvider/CurrentCity");
        resolver.insert(uri,values);
    }

    public static List<City> QueryCity(Context context) {
        ContentResolver resolver = context.getContentResolver();
        Uri uri = Uri.parse("content://com.aegisLan.weather.provider.WeatherInfoProvider/CurrentCity");
        Cursor cursor = resolver.query(uri, null, null, null, null);
        List<City> list = new ArrayList<>();
        while(cursor != null && cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String name  = cursor.getString(cursor.getColumnIndex("name"));
            String pinyin  = cursor.getString(cursor.getColumnIndex("pinyin"));
            String province  = cursor.getString(cursor.getColumnIndex("province"));
            City city = new City(id,name,pinyin,province);
            list.add(city);
            cursor.close();
        }
        return list;
    }

    public static void RemoveCity(Context context, int cityId) {
        ContentResolver resolver = context.getContentResolver();
        Uri uri = Uri.parse("content://com.aegisLan.weather.provider.WeatherInfoProvider/CurrentCity");
        resolver.delete(uri, "id = ?", new String[]{cityId + ""});
    }
}
