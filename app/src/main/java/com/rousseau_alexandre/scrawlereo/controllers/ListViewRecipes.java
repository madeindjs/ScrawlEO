package com.rousseau_alexandre.scrawlereo.controllers;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;


import com.rousseau_alexandre.scrawlereo.models.Recipe;
import com.rousseau_alexandre.scrawlereo.models.RecipeAdapter;

import java.util.List;

/**
 * Custom ListView for recipes
 */
public class ListViewRecipes extends ListView {

    /**
     * List of all recipes
     */
    private List<Recipe> recipes;

    public ListViewRecipes(Context context){
        super(context);
    }

    public ListViewRecipes(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewRecipes(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ListViewRecipes(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * Load all recipes
     *
     * @todo fetch this from https://raspberry-cook.fr
     * @param context
     */
    public void loadRecipes(Context context){
        final RecipeAdapter adapter = new RecipeAdapter(context, Recipe.all(context));
        setAdapter(adapter);
    }
}
