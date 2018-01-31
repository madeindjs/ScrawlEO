package com.rousseau_alexandre.scrawleo.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rousseau_alexandre.scrawleo.R;
import com.rousseau_alexandre.scrawleo.models.Page;
import com.rousseau_alexandre.scrawleo.models.Scrawler;

import static com.rousseau_alexandre.scrawleo.controllers.MainActivity.EXTRA_RECIPE;
import static com.rousseau_alexandre.scrawleo.controllers.ScrawlerActivity.EXTRA_PAGE;

public class PageActivity extends AppCompatActivity {

    private Page page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);

        Intent intent = getIntent();
        page = (Page) intent.getSerializableExtra(EXTRA_PAGE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(page.getUrl());
        setSupportActionBar(toolbar);

        // set values to view

        // title https://moz.com/learn/seo/title-tag
        final String pageTitle = page.getTitle();
        final int pageTtitleSize = pageTitle.length();
        ((TextView) findViewById(R.id.pageTitleValue)).setText(pageTitle);
        int titleScore = 100;
        if(pageTtitleSize > 60 || pageTtitleSize < 50) {
            titleScore = titleScore - 30;
        }else if(pageTtitleSize == 0) {
            titleScore = 0;
        }
        ((ProgressBar) findViewById(R.id.pageTitleProgress)).setProgress(titleScore);


        ((TextView) findViewById(R.id.pageDescriptionValue)).setText(page.getDescription());
        ((TextView) findViewById(R.id.pageKeywordsValue)).setText(page.getKeywords());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
