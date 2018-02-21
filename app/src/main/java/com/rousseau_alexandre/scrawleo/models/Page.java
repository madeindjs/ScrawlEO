package com.rousseau_alexandre.scrawleo.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.rousseau_alexandre.scrawleo.services.PageError;

import org.jsoup.nodes.Document;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * Represent a page scraped
 */
public class Page extends Record {

    public static String TABLE_NAME = "pages";
    public static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_NAME + " ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "scrawler_id INTEGER NOT NULL,"
            + "url TEXT NOT NULL,"
            + "title TEXT,"
            + "description TEXT,"
            + "keywords TEXT,"
            + "status INTEGER,"
            + "h1 TEXT,"
            + "inserted_at INTEGER,"
            + "UNIQUE(scrawler_id, url) ON CONFLICT REPLACE);";
    public static final String SELECT_FIELDS = "id, scrawler_id, url, title, description, keywords, status, h1";

    public static final int DESCRIPTION_MIN = 230;
    public static final int DESCRIPTION_MAX = 320;
    public static final int TITLE_MAX = 71;

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
     * Timestamp of insertion
     */
    private Date inserted_at;

    /**
     * Cursor obtened from this kind of query `SELECT id, url FROM scrawlers`
     *
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

    public Page(Scrawler scrawler, String _url, Document document) {
        scrawler_id = scrawler.id;
        url = _url;
        title = document.title();
        h1 = document.select("h1").text();
        description = document.select("meta[name=\"description\"]").attr("content");
        keywords = document.select("meta[name=\"keywords\"]").attr("content");
    }

    public Page(String _url) {
        url = _url;
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
        database.close();

        return pages;
    }

    public void setScrawler(Scrawler scrawler) {
        scrawler_id = scrawler.id;
    }

    public String getUrl() {
        return url;
    }

    public String getUrlWithoutDomain() {
        String[] parts = url.split("/");
        StringBuilder urlWithoutDomain = new StringBuilder();

        if(parts.length <= 3) {
            return "/";
        }

        for (int i = 3 ; i < parts.length ; i++) {
            urlWithoutDomain.append("/");
            urlWithoutDomain.append(parts[i]);
        }

        return urlWithoutDomain.toString();
    }

    public String getTitle() {
        return title;
    }

    public String getH1() {
        return h1;
    }

    public int getRate() {
        int rate = 100;

        for (PageError error : getErrors()) {
            rate = rate - error.getSeverity();
        }

        if(rate < 0) {
            return 0;
        }

        return rate;
    }

    public String getDescription() {
        return description;
    }

    public String getKeywords() {
        return keywords;
    }

    public int getStatus() {
        return status;
    }

    public Date getInsertedAt() {
        return inserted_at;
    }

    protected ContentValues toContentValues() {
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        inserted_at = date;

        ContentValues values = new ContentValues();
        values.put("url", url);
        values.put("scrawler_id", scrawler_id);
        values.put("h1", h1);
        values.put("title", title);
        values.put("description", description);
        values.put("keywords", keywords);
        values.put("status", status);
        values.put("inserted_at", timestamp.getTime());
        return values;
    }

    /**
     * Dirty truck to get table name in child class
     *
     * @return
     */
    protected String getTableName() {
        return TABLE_NAME;
    }

    public Vector<PageError> getErrors() {
        Vector<PageError> errors = new Vector<>();

        // check title
        int titleSize = title.length();
        if (titleSize == 0) {
            errors.add(PageError.TITLE_EMPTY);
        } else if (titleSize > TITLE_MAX) {
            errors.add(PageError.TITLE_TOO_LONG);
        }

        // check h1
        if (h1.length() == 0) {
            errors.add(PageError.H1_EMPTY);
        }

        // check description
        int descriptionSize = description.length();
        if (descriptionSize == 0) {
            errors.add(PageError.DESCRIPTION_TOO_SHORT);
        } else if (descriptionSize > DESCRIPTION_MAX) {
            errors.add(PageError.DESCRIPTION_TOO_LONG);
        } else if (descriptionSize < DESCRIPTION_MIN) {
            errors.add(PageError.DESCRIPTION_TOO_SHORT);
        }

        // check keywords
        if (keywords.length() == 0) {
            errors.add(PageError.KEYWORDS_EMPTY);
        }

        return errors;
    }

}
