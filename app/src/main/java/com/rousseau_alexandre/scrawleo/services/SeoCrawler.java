package com.rousseau_alexandre.scrawleo.services;


import com.rousseau_alexandre.scrawleo.models.MySQLiteHelper;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.url.WebURL;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * A Crawler who fetch website & check some SEO point
 *
 * - use sitemap file;
 * - each page are reachable
 * - each page has title
 * - use unique title tags for each page
 * - each page has meta description
 * - ues unique descriptions for each page
 * - url does not just use ID number.
 * - url does not use excessive keywords
 * - url does not have deep nesting of subdirectories
 */
public class SeoCrawler extends WebCrawler {

    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg|png|mp3|mp4|zip|gz|svg|avi))$");

    /**
     * Database where page will be saved
     */
    // public static MySQLiteHelper database = MySQLiteHelper;

    public static String startUrl = "";

    public static Vector<Observer> observers = new Vector();

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();

        // TODO: limit for html page
        return href.startsWith(startUrl) || FILTERS.matcher(href).matches();
    }

    @Override
    public void visit(Page page) {
        insertInDatabase(page);
        notifyObservers(page.getWebURL().getURL());
    }

    @Override
    protected void onUnexpectedStatusCode(String urlStr, int statusCode, String contentType, String description) {
        super.onUnexpectedStatusCode(urlStr, statusCode, contentType, description);
        notifyObservers(urlStr);

        // TODO
    }

    @Override
    public void onBeforeExit() {
        for (Observer observer : observers) {
            observer.onCrawlerFinish();
        }
    }

    private void notifyObservers(String url) {
        for (Observer observer : observers) {
            observer.onPageCrawled(url);
        }
    }

    protected void insertInDatabase(Page page) {
        // TODO
    }

}
