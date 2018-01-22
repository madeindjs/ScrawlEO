package com.rousseau_alexandre.scrawleo.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent a page scraped
 */
public class Page extends Record {

    public static String TABLE_NAME = "pages";
    public static final String DATABASE_CREATE =  "CREATE TABLE " + TABLE_NAME + " ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "scrawler_id INTEGER NOT NULL,"
            + "url TEXT NOT NULL,"
            + "title TEXT,"
            + "description TEXT,"
            + "keywords TEXT,"
            + "status INTEGER,"
            + "h1 TEXT );";
    public static final String SELECT_FIELDS = "id, scrawler_id, url, title, description, keywords, status, h1";

    protected long scrawler_id;
    /**
     *
     */
    private String url;
    /**
     * Content of the first h1 tag found
     */
    private String h1;
    /**
     * Content of title tag
     */
    private String title;
    /**
     * Attribute "Content" of the meta tag "description"
     */
    private String description;
    /**
     * Attribute "Content" of the meta tag "keywords"
     */
    private String keywords;
    /**
     * HTTP status code
     */
    private int status;

    /**
     * Cursor obtened from this kind of query `SELECT id, url FROM scrawlers`
     * @param cursor
     */
    public Page(Cursor cursor) {
        id = cursor.getLong(0);
        scrawler_id = cursor.getLong(1);
        url = cursor.getString(2);
        title = cursor.getString(3);
        description = cursor.getString(4);
        keywords = cursor.getString(5);
        status = cursor.getInt(6);
        h1 = cursor.getString(7);
    }

    public Page(Scrawler scrawler) {
        scrawler_id = scrawler.id;
        url = scrawler.url;
    }

    public static List<Page> all(Context context) {
        SQLiteDatabase database = getDatabase(context);
        Cursor cursor = database.rawQuery(
                String.format("SELECT %s FROM %s", SELECT_FIELDS, TABLE_NAME),
                null
        );

        List<Page> pages = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            pages.add(new Page(cursor));
            cursor.moveToNext();
        }

        return pages;
    }


    public void setScrawler(Scrawler scrawler) {
        scrawler_id = scrawler.id;
    }

    public String getUrl() {
        return url;
    }

    protected ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put("url", url);
        values.put("scrawler_id", scrawler_id);
        values.put("h1", h1);
        values.put("title", title);
        values.put("description", description);
        values.put("keywords", keywords);
        values.put("status", status);
        return values;
    }

    /**
     * Dirty truck to get table name in child class
     * @return
     */
    protected String getTableName() {
        return TABLE_NAME;
    }

}
