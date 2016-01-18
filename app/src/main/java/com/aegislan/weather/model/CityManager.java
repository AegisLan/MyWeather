package com.aegislan.weather.model;

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

    public static void AddCity(Context context,City city) {
        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put("id",city.getId());
        values.put("name",city.getName());
        values.put("pinyin", city.getPinyin());
        values.put("province", city.getProvince());
        Uri uri = Uri.parse("content://com.aegislan.weather.provider.WeatherInfoProvider/CurrentCity");
        resolver.insert(uri,values);
    }

    public static List<City> QueryCity(Context context) {
        ContentResolver resolver = context.getContentResolver();
        Uri uri = Uri.parse("content://com.aegislan.weather.provider.WeatherInfoProvider/CurrentCity");
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

    public static void RemoveCity(Context context,City city) {
        ContentResolver resolver = context.getContentResolver();
        Uri uri = Uri.parse("content://com.aegislan.weather.provider.WeatherInfoProvider/CurrentCity");
        resolver.delete(uri,"id = ?",new String[]{city.getId()+""});
    }
}
