package com.icst241.adnu_hrmoattendancemonitoringmobileapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "ahcfams.db";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    db.execSQL("CREATE TABLE STAFF( staff_id VARCHAR PRIMARY KEY NOT NULL, password VARCHAR NOT NULL, privilege INTEGER NOT NULL, staff_type CHARACTER NOT NULL, last_login TEXT, route_id INTEGER, first_name VARCHAR, middle_name VARCHAR, last_name VARCHAR, token VARCHAR)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        onCreate(db);
    }
}
