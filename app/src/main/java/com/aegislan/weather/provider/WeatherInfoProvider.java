package com.aegisLan.weather.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.aegisLan.weather.db.DBOpenHelper;


/**
 * Created by AegisLan on 2016.1.16.
 */
public class WeatherInfoProvider extends ContentProvider {
    private final static UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
    private final static int CURRENT_CITY = 1;
    private final static int WEATHER_DAY = 2;
    private final static int WEATHERS_DAY = 3;
    private final static int FORECAST_HOUR = 4;
    private final static int FORECAST_DAY = 5;
    private DBOpenHelper helper;

    static {
        matcher.addURI("com.aegisLan.weather.provider.WeatherInfoProvider", "CurrentCity", CURRENT_CITY);
        matcher.addURI("com.aegisLan.weather.provider.WeatherInfoProvider", "WeatherDay/#", WEATHER_DAY);
        matcher.addURI("com.aegisLan.weather.provider.WeatherInfoProvider", "WeatherDay", WEATHERS_DAY);
        matcher.addURI("com.aegisLan.weather.provider.WeatherInfoProvider", "ForecastHour", FORECAST_HOUR);
        matcher.addURI("com.aegisLan.weather.provider.WeatherInfoProvider", "ForecastDay", FORECAST_DAY);
    }

    public WeatherInfoProvider() {
    }

    @Override
    public boolean onCreate() {
        helper = new DBOpenHelper(getContext());
        return true;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count = 0;
        int flag = matcher.match(uri);
        SQLiteDatabase db = helper.getWritableDatabase();
        long id;
        switch (flag) {
            case CURRENT_CITY:
                count = db.update("CurrentCity", values, selection, selectionArgs);
                break;
            case WEATHER_DAY:
                id = ContentUris.parseId(uri);
                String where = "id = " + id;
                if (selection != null && !selection.equals("")) {
                    where += selection;
                }
                count = db.update("WeatherDay", values, where, selectionArgs);
                break;
        }
        return count;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        int flag = matcher.match(uri);
        SQLiteDatabase db = helper.getWritableDatabase();
        switch (flag) {
            case CURRENT_CITY:
                count = db.delete("CurrentCity", selection, selectionArgs);
                break;
            case WEATHER_DAY:
                long id = ContentUris.parseId(uri);
                String where = "id = " + id;
                if (selection != null && !selection.equals("")) {
                    where += selection;
                }
                count = db.delete("WeatherDay", where, selectionArgs);
                break;
            case FORECAST_HOUR:
                count = db.delete("ForecastHour", selection, selectionArgs);
                break;
            case FORECAST_DAY:
                count = db.delete("ForecastDay", selection, selectionArgs);
                break;
        }
        return count;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;
        int flag = matcher.match(uri);
        SQLiteDatabase db = helper.getWritableDatabase();
        switch (flag) {
            case CURRENT_CITY:
                cursor = db.query("CurrentCity", projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case WEATHER_DAY:
                long id = ContentUris.parseId(uri);
                String where = "id = " + id;
                if (selection != null && !selection.equals("")) {
                    where += selection;
                }
                cursor = db.query("WeatherDay", projection, where, selectionArgs, null, null, sortOrder);
                break;
            case WEATHERS_DAY:
                cursor = db.query("WeatherDay", projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case FORECAST_HOUR:
                cursor = db.query("ForecastHour", projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case FORECAST_DAY:
                cursor = db.query("ForecastDay", projection, selection, selectionArgs, null, null, sortOrder);
                break;
        }
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        int flag = matcher.match(uri);
        SQLiteDatabase db = helper.getWritableDatabase();
        Uri ret_uri = null;
        long id;
        switch (flag) {
            case CURRENT_CITY:
                id = db.insert("CurrentCity", null, contentValues);
                ret_uri = ContentUris.withAppendedId(uri, id);
                break;
            case WEATHER_DAY:
                id = db.insert("WeatherDay", null, contentValues);
                ret_uri = ContentUris.withAppendedId(uri, id);
                break;
            case FORECAST_HOUR:
                id = db.insert("ForecastHour", null, contentValues);
                ret_uri = ContentUris.withAppendedId(uri, id);
                break;
            case FORECAST_DAY:
                id = db.insert("ForecastDay", null, contentValues);
                ret_uri = ContentUris.withAppendedId(uri, id);
                break;
        }
        return ret_uri;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        int flag = matcher.match(uri);
        switch (flag) {
            case CURRENT_CITY:
                return "vnd.android.cursor.dir/CurrentCities";
            case WEATHER_DAY:
                return "vnd.android.cursor.item/WeatherDay";
            case WEATHERS_DAY:
                return "vnd.android.cursor.dir/WeatherDay";
            case FORECAST_HOUR:
                return "vnd.android.cursor.dir/ForecastHour";
            case FORECAST_DAY:
                return "vnd.android.cursor.dir/ForecastDay";
        }
        return null;
    }
}
