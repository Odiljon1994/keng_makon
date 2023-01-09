package com.toplevel.kengmakon.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.toplevel.kengmakon.models.RecentlyViewedModel;

public class RecentlyViewedDB extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseRecentlyViewed";
    private static final String TABLE_NAME = "recently_viewed";
    private static final String COL2 = "furniture_id";
    private static final String COL3 = "url";

    public RecentlyViewedDB(@Nullable Context context) {
        super(context, TABLE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL2 + " TEXT," + COL3 + " TEXT)";
        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
    public boolean addData(String id, String url) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, id);
        contentValues.put(COL3, url);


        Log.d(TAG, "addData: Adding " + id + " to " + TABLE_NAME);

        long result = db.insert(TABLE_NAME, null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public void deleteTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME;
        db.execSQL(query);
    }

    public void deleteRow(RecentlyViewedModel model) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "ID" + " = ?",
                new String[] { String.valueOf(model.getID()) });
        db.close();
    }
}
