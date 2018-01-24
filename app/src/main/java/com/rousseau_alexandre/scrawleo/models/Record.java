package com.rousseau_alexandre.scrawleo.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;

/**
 * Represent an object who can be saved into Database
 */
public abstract class Record implements Serializable {

    /**
     * Name of SQL table
     *
     * @todo find a way to rewritte in in child class
     */
    public static String TABLE_NAME;

    protected long id;

    /**
     * Save the given record into database
     *
     * @param context
     * @return `true` if success
     */
    public boolean save(Context context) {
        SQLiteDatabase db = getDatabase(context);
        int countRows = db.update(
                getTableName(),
                toContentValues(),
                "id = ?",
                new String[]{Long.toString(id)}
        );
        db.close();

        return countRows == 1;
    }

    /**
     * Insert given record into database
     *
     * @param context
     * @return `true` if success
     */
    public boolean insert(Context context) {
        SQLiteDatabase db = getDatabase(context);
        long newId = db.insert(
                getTableName(), null, toContentValues()
        );
        db.close();
        if(newId == -1){
            return false;
        }else{
            id = newId;
            return true;
        }
    }

    /**
     * Remove given record into database
     *
     * @param context
     * @return `true` if success
     */
    public boolean delete(Context context) {
        SQLiteDatabase db = getDatabase(context);
        int count = db.delete(
                getTableName(),
                "id = ?",
                new String[]{Long.toString(id)}
        );
        db.close();

        return count > 0;
    }

    protected ContentValues toContentValues() {
        return new ContentValues();
    }

    protected static SQLiteDatabase getDatabase(Context context) {
        MySQLiteHelper helper = new MySQLiteHelper(context);
        return helper.getWritableDatabase();
    }

    protected String getTableName() {
        return TABLE_NAME;
    }

}
