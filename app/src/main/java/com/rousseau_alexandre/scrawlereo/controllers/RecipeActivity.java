package com.rousseau_alexandre.scrawlereo.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.rousseau_alexandre.scrawlereo.R;
import com.rousseau_alexandre.scrawlereo.models.Recipe;

import static com.rousseau_alexandre.scrawlereo.controllers.MainActivity.EXTRA_RECIPE;

public class RecipeActivity extends AppCompatActivity {

    private Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);


        Intent intent = getIntent();
        recipe = (Recipe) intent.getSerializableExtra(EXTRA_RECIPE);

        loadRecipeData();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    /**
     * Set recipe
     */
    private void loadRecipeData() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(recipe.getTitle());
        setSupportActionBar(toolbar);

        ((TextView) findViewById(R.id.descriptionText)).setText(recipe.getDescription());
        ((TextView) findViewById(R.id.stepsText)).setText(recipe.getSteps());
        ((TextView) findViewById(R.id.ingredientsText)).setText(recipe.getIngredients());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.recipe, menu);
        return true;
    }


    /**
     * Handle item selection
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.editMenuItem:
                // newGame();
                Intent intent = new Intent(RecipeActivity.this, EditRecipeActivity.class);
                intent.putExtra(EXTRA_RECIPE, recipe);
                startActivity(intent);
                return true;
            case R.id.deleteMenuItem:
                recipe.delete(RecipeActivity.this);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        long id = recipe.getId();
        recipe = Recipe.get(RecipeActivity.this, id);
        loadRecipeData();
    }
}
