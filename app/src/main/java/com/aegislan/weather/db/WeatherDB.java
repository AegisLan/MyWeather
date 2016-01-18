package com.aegislan.weather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.aegislan.weather.WeatherApplication;
import com.aegislan.weather.model.City;
import com.aegislan.weather.model.WeatherInfo;

import java.sql.SQLException;

/**
 * Created by AegisLan on 2016.1.14.
 */
public class WeatherDB {
    private static final String TAG = "WeatherDB";
    private static WeatherDB coolWeatherDB;
    private SQLiteDatabase db;

    private WeatherDB(Context context) {
        DBOpenHelper dbHelper = new DBOpenHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    /**
     * 获取WeatherDB的实例。
     */
    public synchronized static WeatherDB getInstance(Context context) {
        if (coolWeatherDB == null) {
            coolWeatherDB = new WeatherDB(context);
        }
        return coolWeatherDB;
    }

    /**
     * 将City实例存储到数据库。
     */
    public void saveCity(City city) {
        if (city != null) {
            ContentValues values = new ContentValues();
            values.put("id", city.getId());
            values.put("name", city.getName());
            values.put("pinyin", city.getPinyin());
            values.put("province", city.getProvince());
            db.insert("City", null, values);
        }
    }

    /**
     * 根据城市名查询数据库
     */
    public City getCity(String name) {
        City city = null;
        Cursor cursor = db.query("City", null, "name = ?", new String[]{name}, null, null, null);
        if (cursor.moveToNext()) {
            city = new City(cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getString(cursor.getColumnIndex("pinyin")),
                    cursor.getString(cursor.getColumnIndex("province")));
        }
        return city;
    }

    public void saveNewCity(City city) throws SQLException {
        if (city != null) {
            ContentValues values = new ContentValues();
            values.put("id", city.getId());
            values.put("name", city.getName());
            values.put("pinyin", city.getPinyin());
            values.put("province", city.getProvince());
            long rowId = db.insert("City", null, values);
            if (rowId == -1) {
                throw new SQLException(city.getId() + "insert failed!");
            }
        }
    }

    public void saveWeatherInfo(WeatherInfo info) throws SQLException {
        if (info != null) {
            ContentValues values = new ContentValues();
            values.put("id", info.getCityId());
            values.put("name", info.getCity());
            values.put("temp", info.getTemp());
            values.put("wind", info.getWind());
            values.put("windStrong", info.getWindStrong());
            values.put("humidity", info.getHumidity());
            values.put("time", info.getTime());
            values.put("isRadar", info.isRadar());
            values.put("Radar", info.getRadar());
            values.put("njd", info.getNjd());
            values.put("qy", info.getQy());
            long rowId = db.insert("CityWeather", null, values);
            if (rowId == -1) {
                throw new SQLException(info.getCityId() + "insert failed!");
            }
        }
    }
}
