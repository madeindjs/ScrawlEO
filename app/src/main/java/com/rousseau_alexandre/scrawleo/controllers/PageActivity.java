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

        // set title
        View pageTitle = findViewById(R.id.pageTitle);
        ((TextView) pageTitle.findViewById(R.id.propertyTitleText)).setText("Title");
        ((TextView) pageTitle.findViewById(R.id.propertyTitleValue)).setText(page.getTitle());
        // check errors
        StringBuilder errorsTitleText = new StringBuilder();
        int titleSize = page.getTitle().length();
        if (titleSize == 0) {
            errorsTitleText.append("no title provided");
        } else if (titleSize > 60) {
            errorsTitleText.append("title too long (longer than 60 char)");
        } else if (titleSize < 50) {
            errorsTitleText.append("title too short (shorter than 50 char)");
        }
        // TODO: Check duplicates
        if (errorsTitleText.length() > 0) {
            TextView errors = (TextView) pageTitle.findViewById(R.id.propertyErrorsText);
            errors.setText(errorsTitleText.toString());
            errors.setVisibility(View.VISIBLE);
        }

        // h1
        View pageH1 = findViewById(R.id.pageH1);
        ((TextView) pageH1.findViewById(R.id.propertyTitleText)).setText("H1");
        ((TextView) pageH1.findViewById(R.id.propertyTitleValue)).setText(page.getH1());
        // TODO: Check duplicates
        if(page.getH1().length() == 0) {
            TextView errors = (TextView) pageH1.findViewById(R.id.propertyErrorsText);
            errors.setText("No H1 found");
            errors.setVisibility(View.VISIBLE);
        }

        // description
        View pageDescription = findViewById(R.id.pageDescription);
        ((TextView) pageDescription.findViewById(R.id.propertyTitleText)).setText("Description");
        ((TextView) pageDescription.findViewById(R.id.propertyTitleValue)).setText(page.getDescription());
        StringBuilder errorsDescriptionText = new StringBuilder();
        int descriptionSize = page.getDescription().length();
        if (descriptionSize == 0) {
            errorsDescriptionText.append("no description provided");
        } else if (descriptionSize > 320) {
            errorsDescriptionText.append("description too long (longer than 320 char)");
        } else if (titleSize < 230) {
            errorsDescriptionText.append("description too short (shorter than 230 char)");
        }
        if (errorsDescriptionText.length() > 0) {
            TextView errors = (TextView) pageDescription.findViewById(R.id.propertyErrorsText);
            errors.setText(errorsDescriptionText.toString());
            errors.setVisibility(View.VISIBLE);
        }

        // keywords
        View pageKeywords = findViewById(R.id.pageKeywords);
        ((TextView) pageKeywords.findViewById(R.id.propertyTitleText)).setText("Keywords");
        ((TextView) pageKeywords.findViewById(R.id.propertyTitleValue)).setText(page.getKeywords());
        if(page.getKeywords().length() == 0) {
            TextView errors = (TextView) pageKeywords.findViewById(R.id.propertyErrorsText);
            errors.setText("No keywords found");
            errors.setVisibility(View.VISIBLE);
        }


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
