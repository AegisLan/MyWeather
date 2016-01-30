package com.aegisLan.weather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.aegisLan.weather.R;
import com.aegisLan.weather.WeatherApplication;
import com.aegisLan.weather.model.City;
import com.aegisLan.weather.util.XmlParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by AegisLan on 2016.1.13.
 */
public class DBOpenHelper extends SQLiteOpenHelper {
    private final static String TAG = "DBOpenHelper";
    private static String name = "weather.db";
    private static int version = 2;

    public DBOpenHelper(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CITY);
        db.execSQL(CREATE_CURRENT_CITY);
        db.execSQL(CREATE_WEATHER_DAY);
        db.execSQL(CREATE_TRIGGER_INSERT);
        db.execSQL(CREATE_TRIGGER_DELETE);
        InitCityTable(db);
        onUpgrade(db,1,version);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch(oldVersion) {
            case 1:
                db.execSQL(CREATE_FORECAST_HOUR);
                db.execSQL(CREATE_FORECAST_DAY);
                db.execSQL(CREATE_TRIGGER_DELETE_FORECAST_HOUR);
                db.execSQL(CREATE_TRIGGER_DELETE_FORECAST_DAY);
        }
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
            + "name text)";

    public static final String CREATE_WEATHER_DAY = "create table WeatherDay ("
            + "id integer primary key, " + "name text, " + "temp integer NULL, "
            + "state text NULL, "+ "wind text NULL, " + "windStrong text NULL, " + "time text NULL)";

    public static final String CREATE_TRIGGER_INSERT = " create trigger SyncWeatherInsert " +
            "after insert on CurrentCity " +
            "for each row " +
            "begin " +
            "insert into WeatherDay (id, name) values(new.id, new.name); " +
            "end;";

    public static final String CREATE_TRIGGER_DELETE = " create trigger SyncWeatherDelete " +
            "before delete on CurrentCity " +
            "for each row " +
            "begin " +
            "delete from WeatherDay where id = old.id; " +
            "end;";

    public static final String CREATE_FORECAST_HOUR = "create table ForecastHour ("
            + "id integer, " + "time smallDatetime, " + "temp integer NULL, " + "hum integer NULL, "
            + "rainRate integer NULL, " + "windDir text NULL, "+ "windDescribe text NULL, " + "windSpeed integer NULL)";

    public static final String CREATE_FORECAST_DAY = "create table ForecastDay ("
            + "id integer, " + "time Date, " + "tempMax integer NULL, " + "tempMin integer NULL, "
            + "dayStateCode integer NULL, " + "nightStateCode integer NULL, " + "dayState text NULL, " + "nightState text NULL, "
            + "hum integer NULL, " + "rainRate integer NULL, " + "windDir text NULL, "+ "windDescribe text NULL, "
            + "windSpeed integer NULL)";

    public static final String CREATE_TRIGGER_DELETE_FORECAST_HOUR = " create trigger SyncHourForecastDelete " +
            "before delete on CurrentCity " +
            "for each row " +
            "begin " +
            "delete from ForecastHour where id = old.id; " +
            "end;";

    public static final String CREATE_TRIGGER_DELETE_FORECAST_DAY = " create trigger SyncDayForecastDelete " +
            "before delete on CurrentCity " +
            "for each row " +
            "begin " +
            "delete from ForecastDay where id = old.id; " +
            "end;";
}
