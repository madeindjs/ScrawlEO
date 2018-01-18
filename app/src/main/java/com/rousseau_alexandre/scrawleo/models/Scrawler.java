package com.rousseau_alexandre.scrawleo.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represent a recipe from raspberry-cook.fr
 */
public class Scrawler extends Record {

    public String url;


    public Scrawler(String newUrl) {
        url = newUrl;
    }

    public Scrawler(long _id, String newUrl) {
        this(newUrl);
        id = _id;
    }

    /**
     * Cursor obtened from this kind of query `SELECT id, url FROM scrawlers`
     * @param cursor
     */
    public Scrawler(Cursor cursor) {
        id = cursor.getLong(0);
        url = cursor.getString(1);
    }

    public static List<Scrawler> all(Context context) {
        SQLiteDatabase database = getDatabase(context);
        Cursor cursor = database.rawQuery("SELECT id, url FROM " + TABLE_NAME, null);


        List<Scrawler> scrawlers = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            scrawlers.add(new Scrawler(cursor));
            cursor.moveToNext();
        }

        return scrawlers;
    }

    public static Scrawler get(Context context, long id) {
        SQLiteDatabase database = getDatabase(context);
        String sql = String.format("SELECT id, url FROM %s WHERE id = ?", TABLE_NAME);
        Cursor cursor = database.rawQuery(sql, new String[]{Long.toString(id)});
        cursor.moveToFirst();

        return new Scrawler(cursor);
    }

    public long getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String _name) {
        url = _name;
    }


    @Override
    public boolean synchronise(Context context) {
        return false;
    }

    protected ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put("url", url);
        return values;
    }

    public String getTitle(){
        return String.format("%s # %s", id, url);
    }

}
