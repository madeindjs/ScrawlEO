package com.rousseau_alexandre.scrawleo.controllers;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

import com.rousseau_alexandre.scrawleo.models.Page;
import com.rousseau_alexandre.scrawleo.models.PageAdapter;

import java.util.List;

/**
 * Custom ListView for pages
 */
public class ListViewPages extends ListView {

    /**
     * List of all pages
     */
    private List<Page> pages;

    public ListViewPages(Context context){
        super(context);
    }

    public ListViewPages(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewPages(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ListViewPages(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * Load all pages
     *
     * @todo fetch this from https://raspberry-cook.fr
     * @param context
     */
    public void loadPages(Context context){
        final PageAdapter adapter = new PageAdapter(context, Page.all(context));
        setAdapter(adapter);
    }
}
