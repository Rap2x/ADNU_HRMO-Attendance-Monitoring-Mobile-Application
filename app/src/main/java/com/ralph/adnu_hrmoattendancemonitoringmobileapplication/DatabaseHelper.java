package com.ralph.adnu_hrmoattendancemonitoringmobileapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "ahcfams.db";
    public static final String TABLE_NAME = "STAFF";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "( staff_id VARCHAR PRIMARY KEY, password VARCHAR, privilege INTEGER, staff_type CHARACTER, last_login TEXT, route_id INTEGER, first_name VARCHAR, middle_name VARCHAR, last_name VARCHAR, token VARCHAR)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean loginStaff(String username, String token, String last_login){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("staff_id", username);
        contentValues.put("password", "null");
        contentValues.put("privilege", "null");
        contentValues.put("staff_type","null");
        contentValues.put("last_login", last_login);
        contentValues.put("route_id","null");
        contentValues.put("first_name","null");
        contentValues.put("middle_name","null");
        contentValues.put("last_name","null");
        contentValues.put("token", token);

        long result = db.insert(TABLE_NAME, null, contentValues);

        if(result == -1)
            return false;
        else
            return true;
    }
}
