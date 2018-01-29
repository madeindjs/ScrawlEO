package com.rousseau_alexandre.scrawleo.controllers;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;


import com.rousseau_alexandre.scrawleo.models.ScrawlerAdapter;
import com.rousseau_alexandre.scrawleo.models.Scrawler;

import java.util.List;

/**
 * Custom ListView for scrawlers
 */
public class ListViewScrawlers extends ListView {

    public ListViewScrawlers(Context context){
        super(context);
    }

    public ListViewScrawlers(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewScrawlers(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ListViewScrawlers(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * Load all scrawlers
     *
     * @todo fetch this from https://raspberry-cook.fr
     * @param context
     */
    public void loadRecipes(Context context){
        final ScrawlerAdapter adapter = new ScrawlerAdapter(context, Scrawler.all(context));
        setAdapter(adapter);
    }
}
