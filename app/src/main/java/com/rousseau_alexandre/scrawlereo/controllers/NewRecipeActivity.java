package com.rousseau_alexandre.scrawlereo.controllers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.rousseau_alexandre.scrawlereo.R;
import com.rousseau_alexandre.scrawlereo.models.Recipe;


/**
 * Form to create a new recipe
 */
public class NewRecipeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_recipe);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final EditText name = (EditText) findViewById(R.id.nameText);
        final EditText description = (EditText) findViewById(R.id.descriptionText);
        final EditText steps = (EditText) findViewById(R.id.stepsText);
        final EditText ingredients = (EditText) findViewById(R.id.ingredientsText);
        Button btnSubmit = (Button) findViewById(R.id.submitButton);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Recipe recipe = new Recipe(name.getText().toString());
                recipe.setDescription(description.getText().toString());
                recipe.setSteps(steps.getText().toString());
                recipe.setIngredients(ingredients.getText().toString());
                recipe.insert(NewRecipeActivity.this);
                finish();
            }
        });
    }
}
