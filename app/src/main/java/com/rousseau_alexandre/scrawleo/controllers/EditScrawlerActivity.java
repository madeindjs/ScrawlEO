package com.rousseau_alexandre.scrawleo.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.rousseau_alexandre.scrawleo.R;
import com.rousseau_alexandre.scrawleo.models.Scrawler;

import static com.rousseau_alexandre.scrawleo.controllers.MainActivity.EXTRA_RECIPE;

/**
 * Form to create a new recipe
 */
public class EditScrawlerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_scrawler);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // get scrawler
        Intent intent = getIntent();
        final Scrawler scrawler = (Scrawler) intent.getSerializableExtra(EXTRA_RECIPE);

        // get all text fields
        final EditText name = (EditText) findViewById(R.id.nameText);
        // fill all fields
        name.setText(scrawler.getUrl());

        // set submit button callback
        Button btnSubmit = (Button) findViewById(R.id.submitButton);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrawler.setUrl(name.getText().toString());
                scrawler.save(EditScrawlerActivity.this);
                finish();
            }
        });
    }
}
