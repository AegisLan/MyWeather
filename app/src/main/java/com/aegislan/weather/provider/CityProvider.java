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
public class CityProvider extends ContentProvider{
    private final static UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
    private final static int CITY = 1;
    private DBOpenHelper helper;

    static {
        matcher.addURI("com.aegisLan.weather.provider.CityProvider", "City", CITY);
    }

    public CityProvider() {

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
            case CITY:
                id = ContentUris.parseId(uri);
                String where = "where id = " + id;
                if (selection != null && !selection.equals("")) {
                    where += selection;
                }
                count = db.update("City", values, where, selectionArgs);
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
            case CITY:
                id = ContentUris.parseId(uri);
                String where = "where id = " + id;
                if (selection != null && !selection.equals("")) {
                    where += selection;
                }
                count = db.delete("City", where, selectionArgs);
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
            case CITY:
                cursor = db.query("City", projection, selection, selectionArgs, null, null, sortOrder);
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
            case CITY:
                id = db.insert("City", null, contentValues);
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
            case CITY:
                return "vnd.android.cursor.item/City";
        }
        return null;
    }
}
