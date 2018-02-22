package com.rousseau_alexandre.scrawleo.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rousseau_alexandre.scrawleo.R;
import com.rousseau_alexandre.scrawleo.models.Page;
import com.rousseau_alexandre.scrawleo.models.PageAdapter;
import com.rousseau_alexandre.scrawleo.models.Scrawler;
import com.rousseau_alexandre.scrawleo.models.ScrawlerAdapter;
import com.rousseau_alexandre.scrawleo.services.WebCrawler;

import static com.rousseau_alexandre.scrawleo.controllers.MainActivity.EXTRA_RECIPE;

public class ScrawlerActivity extends AppCompatActivity {

    private ListViewPages listPage;
    private Scrawler scrawler;
    private ProgressBar progress;

    public static final String EXTRA_PAGE = "com.rousseau_alexandre.scrawleo.PAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrawler);

        progress = (ProgressBar) findViewById(R.id.crawlerProgress);

        Intent intent = getIntent();
        scrawler = (Scrawler) intent.getSerializableExtra(EXTRA_RECIPE);

        loadScrawlerData();

        View scrawlerDescription = findViewById(R.id.scrawlerDescription);
        View scrawlerBar = scrawlerDescription.findViewById(R.id.scrawlerBar);
        TextView nbLinkValue = (TextView) scrawlerBar.findViewById(R.id.nbLinkValue);
        int countPage = scrawler.countPages(getApplicationContext());
        // nbLinkValue.setText();
        nbLinkValue.setText(Integer.toString(countPage));

        listPage = (ListViewPages) findViewById(R.id.listPage);
        listPage.loadPages(ScrawlerActivity.this, scrawler);
        listPage.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Page page = (Page) listPage.getItemAtPosition(position);
                Intent intent = new Intent(ScrawlerActivity.this, PageActivity.class);
                intent.putExtra(EXTRA_PAGE, page);
                startActivity(intent);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // @todo set FAV button as stop crawl
                WebCrawler crawler = new WebCrawler(ScrawlerActivity.this, scrawler, mCallback);
                crawler.startCrawlerTask();
                progress.setVisibility(View.VISIBLE);
            }
        });
    }

    private WebCrawler.CrawlingCallback mCallback = new WebCrawler.CrawlingCallback() {

        @Override
        public void onPageCrawlingCompleted() {
            // android.view.ViewRootImpl$CalledFromWrongThreadException:
            // Only the original thread that created a view hierarchy can touch its views.
            // PageAdapter adapter = (PageAdapter) listPage.getAdapter();
            // adapter.reload();
        }

        @Override
        public void onPageCrawlingFailed(String Url, int errorCode) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onCrawlingCompleted() {
            progress.setVisibility(View.INVISIBLE);
            PageAdapter adapter = (PageAdapter) listPage.getAdapter();
            adapter.reload();
        }

    };



    /**
     * Set scrawler
     */
    private void loadScrawlerData() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(scrawler.getTitle());
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.scrawler, menu);
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
        loadScrawlerData();
    }
}
