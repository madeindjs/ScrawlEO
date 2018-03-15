package com.rousseau_alexandre.scrawleo.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.rousseau_alexandre.scrawleo.services.PageError;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;

/**
 * Represent a page scraped
 */
public class ScrapedPage extends Record {

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
    private int inserted_at;

    /**
     * Cursor obtened from this kind of query `SELECT id, url FROM scrawlers`
     *
     * @param cursor
     */
    public ScrapedPage(Cursor cursor) {
        id = cursor.getLong(0);
        scrawler_id = cursor.getLong(1);
        url = cursor.getString(2);
        title = cursor.getString(3);
        description = cursor.getString(4);
        keywords = cursor.getString(5);
        status = cursor.getInt(6);
        h1 = cursor.getString(7);
//        inserted_at = cursor.getInt(8);
    }

    public ScrapedPage(Scrawler scrawler) {
        scrawler_id = scrawler.id;
        url = scrawler.url;
    }

    public ScrapedPage(Page page) throws ParseException {
        url = page.getWebURL().getURL();
        status = page.getStatusCode();

        // Parse HTML with JSoup
        Document doc = null;
        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String html = htmlParseData.getHtml();
            doc = Jsoup.parse(html);
        } else {
            throw new ParseException("Can't parse HTML", 0);
        }

        title = doc.title();

        // set h1
        Element eH1 = doc.selectFirst("h1");
        if (eH1 != null) {
            h1 = eH1.text();
        }

        // set description
        Element eDescription = doc.selectFirst("meta[name=\"description\"]");
        if (eDescription != null) {
            description = eDescription.attr("content");
        }

        // set eKeywords
        Element eKeywords = doc.selectFirst("meta[name=\"keywords\"]");
        if (eKeywords != null) {
            keywords = eKeywords.attr("content");
        }

        /*
        // check if some elements have alt tag
        for (Element eImage : doc.select("img")) {
            if (!eImage.hasAttr("alt")) {
                imagesWithoutAlt.add(new ImageWithoutAlt(eImage));
            }
        }
        */
    }

    public ScrapedPage(String _url) {
        url = _url;
    }

    public static List<ScrapedPage> all(Context context) {
        SQLiteDatabase database = getDatabase(context);
        Cursor cursor = database.rawQuery(
                String.format("SELECT %s FROM %s", SELECT_FIELDS, TABLE_NAME),
                null
        );

        List<ScrapedPage> scrapedPages = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            scrapedPages.add(new ScrapedPage(cursor));
            cursor.moveToNext();
        }
        database.close();

        return scrapedPages;
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

    public int getInsertedAt() {
        return inserted_at;
    }

    protected ContentValues toContentValues() {
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        // inserted_at = date.toString();

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
