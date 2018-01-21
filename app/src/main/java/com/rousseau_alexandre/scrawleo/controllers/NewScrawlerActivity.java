package com.rousseau_alexandre.scrawleo.controllers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.rousseau_alexandre.scrawleo.models.Scrawler;
import com.rousseau_alexandre.scrawleo.R;


/**
 * Form to create a new scrawler
 */
public class NewScrawlerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_scrawler);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final EditText name = (EditText) findViewById(R.id.nameText);
        Button btnSubmit = (Button) findViewById(R.id.submitButton);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Scrawler scrawler = new Scrawler(name.getText().toString());
                scrawler.insert(NewScrawlerActivity.this);
                finish();
            }
        });
    }
}
