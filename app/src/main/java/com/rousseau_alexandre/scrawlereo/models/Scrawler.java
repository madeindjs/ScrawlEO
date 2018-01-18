package com.rousseau_alexandre.scrawlereo.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represent a recipe from raspberry-cook.fr
 */
public class Scrawler extends Record {

    public String name;
    public long user_id;
    public Date created_at;
    public String description;
    public String ingredients;
    public String steps;
    public String season;
    public String photo;
    public String image;
    public long root_recipe_id = 0;
    public String variant_name;
    public String varchar;
    public int baking = 0;
    public int cooling = 0;
    public int rest = 0;
    public int cooking = 0;


    public Scrawler(String _name) {
        name = _name;
    }

    public Scrawler(long _id, String _name) {
        this(_name);
        id = _id;
    }

    /**
     * Cursor obtened from this kind of query `SELECT id, name, description, ingredients, steps FROM recipes`
     * @param cursor
     */
    public Scrawler(Cursor cursor) {
        id = cursor.getLong(0);
        name = cursor.getString(1);
        description = cursor.getString(2);
        ingredients = cursor.getString(3);
        steps = cursor.getString(4);
    }

    public static List<Scrawler> all(Context context) {
        SQLiteDatabase database = getDatabase(context);
        Cursor cursor = database.rawQuery("SELECT id, name, description, ingredients, steps FROM " + TABLE_NAME, null);


        List<Scrawler> scrawlers = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            scrawlers.add(new Scrawler(cursor));
            cursor.moveToNext();
        }

        return scrawlers;
    }

    public static Scrawler get(Context context, long id) {
        SQLiteDatabase database = getDatabase(context);
        String sql = String.format("SELECT id, name, description, ingredients, steps FROM %s WHERE id = ?", TABLE_NAME);
        Cursor cursor = database.rawQuery(sql, new String[]{Long.toString(id)});
        cursor.moveToFirst();

        return new Scrawler(cursor);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String _name) {
        name = _name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String _description) {
        description = _description;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String _steps) {
        steps = _steps;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String _ingredients) {
        ingredients = _ingredients;
    }

    @Override
    public boolean synchronise(Context context) {
        return false;
    }

    protected ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("description", description);
        values.put("ingredients", ingredients);
        values.put("steps", steps);
        return values;
    }

    public String getTitle(){
        return String.format("%s # %s", id, name);
    }

}
