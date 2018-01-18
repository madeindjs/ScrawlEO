package com.rousseau_alexandre.scrawlereo.models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_RECIPES = "recipes";

    private static final String DATABASE_NAME = "raspberry_cook.sqlite";
    private static final int DATABASE_VERSION = 2;

    /**
     * SQL Query to create database
     */
    private static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_RECIPES + " (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name TEXT, user_id integer, created_at datetime, updated_at datetime, description text, ingredients text, steps text, season varchar, photo varchar, image varchar, root_recipe_id integer DEFAULT 0, variant_name varchar, rtype varchar, baking integer DEFAULT 0, cooling integer DEFAULT 0, rest integer DEFAULT 0, cooking integer DEFAULT 0)" ;

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(
                MySQLiteHelper.class.getName(),
                String.format(
                        "Upgrading database from version %s to %s, which will destroy all old data",
                        oldVersion, newVersion
                )
        );
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        onCreate(db);
    }
}
