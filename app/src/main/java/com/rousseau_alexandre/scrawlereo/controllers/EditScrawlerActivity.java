package com.rousseau_alexandre.scrawlereo.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.rousseau_alexandre.scrawlereo.R;
import com.rousseau_alexandre.scrawlereo.models.Scrawler;

import static com.rousseau_alexandre.scrawlereo.controllers.MainActivity.EXTRA_RECIPE;

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
        final EditText description = (EditText) findViewById(R.id.descriptionText);
        final EditText steps = (EditText) findViewById(R.id.stepsText);
        final EditText ingredients = (EditText) findViewById(R.id.ingredientsText);
        // fill all fields
        name.setText(scrawler.getName());
        description.setText(scrawler.getDescription());
        steps.setText(scrawler.getDescription());
        ingredients.setText(scrawler.getIngredients());

        // set submit button callback
        Button btnSubmit = (Button) findViewById(R.id.submitButton);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrawler.setName(name.getText().toString());
                scrawler.setDescription(description.getText().toString());
                scrawler.setSteps(steps.getText().toString());
                scrawler.setIngredients(ingredients.getText().toString());
                scrawler.save(EditScrawlerActivity.this);
                finish();
            }
        });
    }
}
