package com.rousseau_alexandre.scrawleo.models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "scrawler.sqlite";
    private static final int DATABASE_VERSION = 6;

    /**
     * SQL Query to create database
     */
    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(Page.DATABASE_CREATE);
        database.execSQL(Scrawler.DATABASE_CREATE);
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
        db.execSQL("DROP TABLE IF EXISTS " + Scrawler.TABLE_NAME );
        db.execSQL("DROP TABLE IF EXISTS " + Page.TABLE_NAME );
        onCreate(db);
    }
}
