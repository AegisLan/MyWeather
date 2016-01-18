package com.aegislan.weather.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.aegislan.weather.db.DBOpenHelper;


/**
 * Created by AegisLan on 2016.1.16.
 */
public class WeatherInfoProvider extends ContentProvider {
    private final static UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
    private final static int CURRENT_CICY = 1;
    private final static int WEATHER_DAY = 2;
    private final static int WEATHER_WEEK = 3;
    private DBOpenHelper helper;

    static {
        matcher.addURI("com.aegislan.weather.provider.WeatherInfoProvider", "CurrentCity", CURRENT_CICY);
        matcher.addURI("com.aegislan.weather.provider.WeatherInfoProvider", "WeatherDay", WEATHER_DAY);
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
            case CURRENT_CICY:
                id = ContentUris.parseId(uri);
                String where = "where id = " + id;
                if (selection != null && !selection.equals("")) {
                    where += selection;
                }
                count = db.update("CurrentCity", values, where, selectionArgs);
                break;
            case WEATHER_DAY:
                id = ContentUris.parseId(uri);
                String where1 = "where id = " + id;
                if (selection != null && !selection.equals("")) {
                    where1 += selection;
                }
                count = db.update("WeatherDay", values, where1, selectionArgs);
                break;
        }
        return count;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        int flag = matcher.match(uri);
        SQLiteDatabase db = helper.getWritableDatabase();
        long id;
        switch (flag) {
            case CURRENT_CICY:
                id = ContentUris.parseId(uri);
                String where = "where id = " + id;
                if (selection != null && !selection.equals("")) {
                    where += selection;
                }
                count = db.delete("CurrentCity", where, selectionArgs);
                break;
            case WEATHER_DAY:
                id = ContentUris.parseId(uri);
                String where1 = "where id = " + id;
                if (selection != null && !selection.equals("")) {
                    where1 += selection;
                }
                count = db.delete("WeatherDay", where1, selectionArgs);
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
            case CURRENT_CICY:
                cursor = db.query("CurrentCity", projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case WEATHER_DAY:
                cursor = db.query("WeatherDay", projection, selection, selectionArgs, null, null, sortOrder);
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
            case CURRENT_CICY:
                id = db.insert("CurrentCity", null, contentValues);
                ret_uri = ContentUris.withAppendedId(uri, id);
                break;
            case WEATHER_DAY:
                id = db.insert("WeatherDay", null, contentValues);
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
            case CURRENT_CICY:
                return "vnd.android.cursor.dir/CurrentCities";
            case WEATHER_DAY:
                return "vnd.android.cursor.item/WeatherDay";
        }
        return null;
    }
}
