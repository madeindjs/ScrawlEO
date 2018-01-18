package com.rousseau_alexandre.scrawleo.models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_SCRAWLER = "scrawlers";

    private static final String DATABASE_NAME = "scrawler.sqlite";
    private static final int DATABASE_VERSION = 1;

    /**
     * SQL Query to create database
     */
    private static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_SCRAWLER + " (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, url TEXT NOT NULL)" ;

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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCRAWLER);
        onCreate(db);
    }
}
