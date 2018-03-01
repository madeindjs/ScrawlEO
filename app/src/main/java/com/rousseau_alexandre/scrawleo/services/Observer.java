package com.rousseau_alexandre.scrawleo.services;

public interface Observer {

    public void onPageCrawled(String url);

    public void onCrawlerFinish();

}
