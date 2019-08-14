package com.ralph.adnu_hrmoattendancemonitoringmobileapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "ahcfams.db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE STAFF ( staff_id VARCHAR PRIMARY KEY NOT NULL, last_login TEXT, route_id INTEGER, token VARCHAR)");
        db.execSQL("CREATE TABLE FACULTY(\n" +
                "\tfaculty_id VARCHAR PRIMARY KEY NOT NULL,\n" +
                "\tname VARCHAR NOT NULL,\n" +
                "\tdesignation VARCHAR NOT NULL,\n" +
                "\tpassword VARCHAR NOT NULL,\n" +
                "\tdepartment VARCHAR NOT NULL,\n" +
                "\tcollege VARCHAR NOT NULL\n" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        //db.execSQL("DROP TABLE IF EXISTS  FACULTY");
        //db.execSQL("DROP TABLE IF EXISTS STAFF");
        onCreate(db);
    }

    public boolean loginStaff(String username, String token, String last_login){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("staff_id", username);
        contentValues.put("last_login", last_login);
        contentValues.put("route_id","null");
        contentValues.put("token", token);

        long result;

        if(isUserRecorded(username)){
            result = db.update("STAFF", contentValues, "staff_id = " + username,null);
            if(result == -1)
                return false;
        }else
            result = db.insert("STAFF",null,contentValues);
                if(result == -1)
                    return false;
                else
                    return true;

    }


    public ArrayList getUserCredentials(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> arrayList = new ArrayList<String>();

        Cursor res = db.rawQuery("select staff_id, token from STAFF",null);
        res.moveToLast();
        arrayList.add(res.getString(0));
        arrayList.add(res.getString(1));

        return arrayList;
    }

    public boolean isUserRecorded(String staff_id){
        SQLiteDatabase db = getReadableDatabase();
        Cursor res = db.rawQuery("select staff_id from staff where staff_id =" + staff_id, null);

        if(res.getCount() == 0)
            return false;
        else
            return true;

    }

    public ArrayList getFaculty(){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<String> arrayList = new ArrayList<String>();
        Cursor res = db.rawQuery("select * from faculty", null);
        res.moveToLast();

        arrayList.add(res.getString(res.getColumnIndex("faculty_id")));


        return arrayList;
    }
}
