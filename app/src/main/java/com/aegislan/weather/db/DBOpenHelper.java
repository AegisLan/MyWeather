package com.aegislan.weather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.aegislan.weather.R;
import com.aegislan.weather.WeatherApplication;
import com.aegislan.weather.model.City;
import com.aegislan.weather.util.XmlParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by AegisLan on 2016.1.13.
 */
public class DBOpenHelper extends SQLiteOpenHelper {
    private final static String TAG = "DBOpenHelper";
    private static String name = "weather.db";
    private static int version = 1;

    public DBOpenHelper(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CITY);
        db.execSQL(CREATE_CURRENT_CITY);
        db.execSQL(CREATE_WEATHER_DAY);
        InitCityTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }
    private int InitCityTable(SQLiteDatabase db) {
        InputStream is = WeatherApplication.getAppContext().getResources().openRawResource(R.raw.citylist);
        List<City> cityList = null;
        try {
            cityList = XmlParser.getCityList(is);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        int i = 0;
        // TODO: 2016.1.16 解析XML数据写入数据库,不合理的设计需要更改，数据库不应该知道MODEL内容
        for (City city : cityList) {
            ContentValues values = new ContentValues();
            values.put("id", city.getId());
            values.put("name", city.getName());
            values.put("pinyin", city.getPinyin());
            values.put("province", city.getProvince());
            db.insert("City", null, values);
            i++;
        }
        return i;
    }
    /**
     * City表建表语句
     */
    public static final String CREATE_CITY = "create table City ("
            + "id integer primary key, "
            + "name text, "
            + "pinyin text, "
            + "province text)";
    public static final String CREATE_CURRENT_CITY = "create table CurrentCity ("
            + "id integer primary key, "
            + "name text, "
            + "pinyin text, "
            + "province text)";

    public static final String CREATE_WEATHER_DAY = "create table WeatherDay ("
            + "id integer primary key, " + "city text, " + "temp integer, "
            + "wind text, " + "windStrong text, " + "humidity text, "
            + "time text, " + "isRadar bit, " + "Radar text"
            + "njd text, " + "qy text)";
}
