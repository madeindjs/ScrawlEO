package com.rousseau_alexandre.scrawleo.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represent a scrawler from raspberry-cook.fr
 */
public class Scrawler extends Record {

    public static final String TABLE_NAME = "scrawlers";
    public static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, url TEXT NOT NULL);";

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
        SQLiteDatabase db = getDatabase(context);
        Cursor cursor = db.rawQuery("SELECT id, url FROM " + TABLE_NAME, null);

        List<Scrawler> scrawlers = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            scrawlers.add(new Scrawler(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();

        return scrawlers;
    }

    public static Scrawler get(Context context, long id) {
        Cursor cursor = getDatabase(context).rawQuery(
                String.format("SELECT id, url FROM %s WHERE id = ?", TABLE_NAME),
                new String[]{Long.toString(id)}
        );
        cursor.moveToFirst();

        return new Scrawler(cursor);
    }

    public List<Page> getPages(Context context) {
        SQLiteDatabase db = getDatabase(context);
        Cursor cursor = db.rawQuery(
                String.format(
                        "SELECT %s FROM %s WHERE scrawler_id = ?",
                        Page.SELECT_FIELDS,
                        Page.TABLE_NAME
                ),
                new String[]{Long.toString(id) }
        );

        List<Page> pages = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            pages.add(new Page(cursor));
            cursor.moveToNext();
        }
        db.close();
        cursor.close();

        return pages;
    }

    public int countPages(Context context) {
        SQLiteDatabase db = getDatabase(context);
        Cursor cursor = db.rawQuery(
                String.format("SELECT COUNT(*) FROM %s WHERE scrawler_id = ?", Page.TABLE_NAME),
                new String[]{Long.toString(id)}
        );
        int count = 0;

        if(null != cursor) {
            if(cursor.getCount() > 0){
                cursor.moveToFirst();
                count = cursor.getInt(0);
            }
        }
        cursor.close();
        db.close();

        return count;
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


    protected ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put("url", url);
        return values;
    }

    public String getTitle(){
        return url;
    }

    /**
     * Dirty truck to get table name in child class
     * @return
     */
    protected String getTableName() {
        return TABLE_NAME;
    }

}
