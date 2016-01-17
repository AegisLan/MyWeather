package com.aegislan.weather.model;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AegisLan on 2016.1.17.
 */
public class CityFinder {
    public static City QueryCity(Context context,String cityName) {
        ContentResolver resolver = context.getContentResolver();
        Uri uri = Uri.parse("content://com.aegislan.weather.provider.CityProvider/City");
        Cursor cursor = resolver.query(uri, null, "name = ？", new String[]{cityName}, null);
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
}
