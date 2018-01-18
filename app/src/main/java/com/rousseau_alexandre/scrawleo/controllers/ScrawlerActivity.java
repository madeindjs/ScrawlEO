package com.rousseau_alexandre.scrawleo.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.rousseau_alexandre.scrawleo.R;
import com.rousseau_alexandre.scrawleo.models.Scrawler;

import static com.rousseau_alexandre.scrawleo.controllers.MainActivity.EXTRA_RECIPE;

public class ScrawlerActivity extends AppCompatActivity {

    private Scrawler scrawler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrawler);


        Intent intent = getIntent();
        scrawler = (Scrawler) intent.getSerializableExtra(EXTRA_RECIPE);

        loadRecipeData();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    /**
     * Set scrawler
     */
    private void loadRecipeData() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(scrawler.getTitle());
        setSupportActionBar(toolbar);
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
                Intent intent = new Intent(ScrawlerActivity.this, EditScrawlerActivity.class);
                intent.putExtra(EXTRA_RECIPE, scrawler);
                startActivity(intent);
                return true;
            case R.id.deleteMenuItem:
                scrawler.delete(ScrawlerActivity.this);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        long id = scrawler.getId();
        scrawler = Scrawler.get(ScrawlerActivity.this, id);
        loadRecipeData();
    }
}
