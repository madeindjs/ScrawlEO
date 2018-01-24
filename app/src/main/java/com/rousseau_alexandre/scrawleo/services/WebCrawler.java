package com.rousseau_alexandre.scrawleo.services;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.rousseau_alexandre.scrawleo.models.Page;
import com.rousseau_alexandre.scrawleo.models.Scrawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Android Multithreaded  Web Crawler
 *
 * inspired from http://androidsrc.net/android-web-crawler-example-multithreaded-implementation
 */
public class WebCrawler {

    /**
     * Interface for crawling callback
     */
    public interface CrawlingCallback {
        void onPageCrawlingCompleted();

        void onPageCrawlingFailed(String Url, int errorCode);

        void onCrawlingCompleted();
    }

    private final int SC_OK = 200;
    private Context context;
    /**
     * Scrawler
     */
    private Scrawler scrawler;
    /**
     * Set containing already visited URls
     */
    private HashSet<String> crawledURL;
    /**
     * Queue for unvisited URL
     */
    BlockingQueue<String> uncrawledURL;
    /**
     * For parallel crawling execution using ThreadPoolExecuter
     */
    RunnableManager mManager;
    /**
     * Callback interface object to notify UI
     */
    CrawlingCallback callback;
    /**
     * For sync of crawled and yet to crawl url lists
     */
    Object lock;

    /**
     * @param _context
     * @param _scrawler linked scrawler to save pages
     * @param _callback
     */
    public WebCrawler(Context _context, Scrawler _scrawler, CrawlingCallback _callback) {
        context = _context;
        callback = _callback;
        crawledURL = new HashSet<>();
        scrawler = _scrawler;
        uncrawledURL = new LinkedBlockingQueue<>();
        lock = new Object();
    }


    public void startCrawlerTask() {
        crawledURL.clear();
        uncrawledURL.clear();
        mManager = new RunnableManager();

        startCrawlerTask(scrawler.url);
    }

    /**
     * API to add crawler runnable in ThreadPoolExecutor workQueue
     *
     * @param url URL to crawl
     */
    public void startCrawlerTask(String url) {
        // If ThreadPoolExecuter is not shutting down, add runnable to workQueue
        if (!mManager.isShuttingDown()) {
            CrawlerRunnable mTask = new CrawlerRunnable(callback, url);
            mManager.addToCrawlingQueue(mTask);
        }
    }

    /**
     * API to shutdown ThreadPoolExecuter
     */
    public void stopCrawlerTasks() {
        mManager.cancelAllRunnable();
    }

    /**
     * Runnable task which performs task of crawling and adding encountered URls
     * to crawling list
     */
    private class CrawlerRunnable implements Runnable {

        CrawlingCallback mCallback;
        String runnableUrl;

        public CrawlerRunnable(CrawlingCallback _callback, String _runnableUrl) {
            mCallback = _callback;
            runnableUrl = _runnableUrl;
        }

        @Override
        public void run() {
            String pageContent = retreiveHtmlContent(scrawler.getUrl());

            if (!TextUtils.isEmpty(pageContent.toString())) {
                Document doc = Jsoup.parse(pageContent.toString());
                Page page = new Page(scrawler, runnableUrl, doc);

                // START
                // JSoup Library used to filter urls from html body
                Elements links = doc.select("a[href]");
                for (Element link : links) {
                    String extractedLink = link.attr("href");
                    if (!TextUtils.isEmpty(extractedLink)) {
                        synchronized (lock) {
                            if (!crawledURL.contains(extractedLink))
                                uncrawledURL.add(extractedLink);
                        }

                    }
                }// End JSoup

                page.insert(context);
                synchronized (lock) {
                    crawledURL.add(runnableUrl);
                }
                mCallback.onPageCrawlingCompleted();
            } else {
                mCallback.onPageCrawlingFailed(runnableUrl, -1);
            }

            // Send msg to handler that crawling for this url is finished
            // start more crawling tasks if queue is not empty
            mHandler.sendEmptyMessage(0);

        }

        private String retreiveHtmlContent(String Url) {
            URL httpUrl = null;
            try {
                httpUrl = new URL(Url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            int responseCode = SC_OK;
            StringBuilder pageContent = new StringBuilder();
            try {
                if (httpUrl != null) {
                    HttpURLConnection conn = (HttpURLConnection) httpUrl
                            .openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(5000);
                    responseCode = conn.getResponseCode();
                    if (responseCode != SC_OK) {
                        throw new IllegalAccessException("HTTP connection failed");
                    }
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        pageContent.append(line);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
                mCallback.onPageCrawlingFailed(Url, -1);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                mCallback.onPageCrawlingFailed(Url, responseCode);
            }

            return pageContent.toString();
        }

    }


    /**
     * To manage Messages in a Thread
     */
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(android.os.Message msg) {

            synchronized (lock) {
                if (uncrawledURL != null && uncrawledURL.size() > 0) {
                    int availableTasks = mManager.getUnusedPoolSize();
                    while (availableTasks > 0 && !uncrawledURL.isEmpty()) {
                        startCrawlerTask(uncrawledURL.remove());
                        availableTasks--;
                    }
                    callback.onCrawlingCompleted();
                }
            }
        }
    };

    /**
     * Helper class to interact with ThreadPoolExecutor for adding and removing
     * runnable in workQueue
     */
    private class RunnableManager {

        /**
         * Sets the amount of time an idle thread will wait for a task before terminating
         */
        private static final int KEEP_ALIVE_TIME = 1;
        /**
         * Sets the Time Unit to seconds
         */
        private final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
        /**
         * Sets the initial threadpool size to 5
         */
        private static final int CORE_POOL_SIZE = 5;
        /**
         * Sets the maximum threadpool size to 8
         */
        private static final int MAXIMUM_POOL_SIZE = 8;
        /**
         * A queue of Runnables for crawling url
         */
        private final BlockingQueue<Runnable> mCrawlingQueue;
        /**
         * A managed pool of background crawling threads
         */
        private final ThreadPoolExecutor mCrawlingThreadPool;

        RunnableManager() {
            mCrawlingQueue = new LinkedBlockingQueue<>();
            mCrawlingThreadPool = new ThreadPoolExecutor(
                    CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME,
                    KEEP_ALIVE_TIME_UNIT, mCrawlingQueue
            );
        }

        private void addToCrawlingQueue(Runnable runnable) {
            mCrawlingThreadPool.execute(runnable);
        }

        private void cancelAllRunnable() {
            mCrawlingThreadPool.shutdownNow();
        }

        private int getUnusedPoolSize() {
            return MAXIMUM_POOL_SIZE - mCrawlingThreadPool.getActiveCount();
        }

        private boolean isShuttingDown() {
            return mCrawlingThreadPool.isShutdown() || mCrawlingThreadPool.isTerminating();
        }

    }

}
