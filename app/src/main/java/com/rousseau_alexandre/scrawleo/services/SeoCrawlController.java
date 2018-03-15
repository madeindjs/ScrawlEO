package com.rousseau_alexandre.scrawleo.services;

import android.content.Context;

import java.util.Vector;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;



public class SeoCrawlController extends CrawlController {

    private Context context;

    /**
     * Folder where temporary files where stored
     */
    private static final String STORAGE_FOLDER = "crawl_data";
    /**
     * Number of crawler at the same times
     */
    private static final int NUMBER_OF_THREAD = 4;


    public static SeoCrawlController create(String url, Context context) throws Exception {
        // configure crawler
        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(STORAGE_FOLDER);
        config.setThreadShutdownDelaySeconds(1);
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        // create controller
        SeoCrawlController controller = new SeoCrawlController(config, pageFetcher, robotstxtServer);
        controller.addSeed(url);
        controller.setContext(context);

        return controller;
    }

    public Context getContext() {
        return context;
    }

    private void setContext(Context context) {
        this.context = context;
    }

    private SeoCrawlController(CrawlConfig config, PageFetcher pageFetcher, RobotstxtServer robotstxtServer) throws Exception {
        super(config, pageFetcher, robotstxtServer);
    }


    public void start() {
        start(SeoCrawler.class, NUMBER_OF_THREAD);
    }

    public void startNonBlocking() {
        startNonBlocking(SeoCrawler.class, NUMBER_OF_THREAD);
    }


    public void addSeed(String url) {
        super.addSeed(url);
        SeoCrawler.startUrl = url;
    }

}
