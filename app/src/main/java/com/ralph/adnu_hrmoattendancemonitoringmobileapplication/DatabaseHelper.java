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
        // Create the tables here
        db.execSQL("CREATE TABLE FACULTY(\n" +
                "\tfaculty_id VARCHAR PRIMARY KEY NOT NULL,\n" +
                "\tfirst_name VARCHAR NOT NULL,\n" +
                "\tmiddle_name VARCHAR NOT NULL,\n" +
                "\tlast_name VARCHAR NOT NULL,\n" +
                "\tdesignation VARCHAR NOT NULL,\n" +
                "\temail VARCHAR NOT NULL UNIQUE,\n" +
                "\tcontact_number VARCHAR NOT NULL UNIQUE,\n" +
                "\tpassword VARCHAR NOT NULL,\n" +
                "\tdepartment VARCHAR NOT NULL,\n" +
                "\tcollege VARCHAR NOT NULL\n" +
                ")");
        db.execSQL("CREATE TABLE STAFF(\n" +
                "\tstaff_id VARCHAR PRIMARY KEY NOT NULL,\n" +
                "\tpassword VARCHAR,\n" +
                "\tprivilege INTEGER,\n" +
                "\tstaff_type CHARACTER,\n" +
                "\tlast_login TEXT,\n" +
                "\troute_id INTEGER,\n" +
                "\tfirst_name VARCHAR,\n" +
                "\tmiddle_name VARCHAR,\n" +
                "\tlast_name VARCHAR,\n" +
                "\ttoken VARCHAR\n" +
                ")");

        db.execSQL("CREATE TABLE ROUTE(\n" +
                "\troute_id INTEGER PRIMARY KEY NOT NULL,\n" +
                "\tdescription VARCHAR\n" +
                ")");

        db.execSQL("CREATE TABLE ROOM(\n" +
                "\troom_id VARCHAR PRIMARY KEY NOT NULL,\n" +
                "\troute_id INTEGER,\n" +
                "\tbuidling_name VARCHAR NOT NULL\n" +
                ")");

        db.execSQL("CREATE TABLE CLASS_SCHEDULE(\n" +
                "\tclass_schedule_id VARCHAR PRIMARY KEY NOT NULL,\n" +
                "\troom_id VARCHAR,\n" +
                "\tfaculty_id VARCHAR,\n" +
                "\tsemester INTEGER NOT NULL,\n" +
                "\tschool_year VARCHAR NOT NULL,\n" +
                "\tstart_time TEXT,\n" +
                "\tend_time TEXT,\n" +
                "\tclass_section VARCHAR NOT NULL,\n" +
                "\tclass_day VARCHAR,\n" +
                "\tsubject_code VARCHAR NOT NULL,\n" +
                "\thalf_day INTEGER NOT NULL,\n" +
                "\thours FLOAT NOT NULL\n" +
                ")");

        db.execSQL("CREATE TABLE FACULTY_ATTENDANCE(\n" +
                "\tfaculty_attendance_id VARCHAR PRIMARY KEY NOT NULL,\n" +
                "\tstaff_id VARCHAR NOT NULL,\n" +
                "\tclass_schedule_id VARCHAR NOT NULL,\n" +
                "\tattendance_date TEXT NOT NULL,\n" +
                "\tfirst_check TEXT,\n" +
                "\tsecond_check TEXT,\n" +
                "\timage_file VARCHAR,\n" +
                "\tsalary_deduction CHARACTER,\n" +
                "\tstatus VARCHAR\n" +
                ")");

        db.execSQL("CREATE TABLE CONFIRMATION_NOTICE(\n" +
                "\tconfirmation_notice_id VARCHAR PRIMARY KEY NOT NULL,\n" +
                "\tfaculty_attendance_id VARCHAR,\n" +
                "\tconfirmation_notice_id TEXT,\n" +
                "\treason VARCHAR,\n" +
                "\telectronic_signature VARCHAR,\n" +
                "\tremarks VARCHAR\n" +
                ")");

        db.execSQL("CREATE TABLE ABSENCE_APPEAL(\n" +
                "\tabsence_appeal_id VARCHAR PRIMARY KEY NOT NULL,\n" +
                "\tconfirmation_notice_id VARCHAR NOT NULL,\n" +
                "\tstaff_id VARCHAR,\n" +
                "\tchairperson_id VARCHAR,\n" +
                "\tabsence_appeal_reason VARCHAR,\n" +
                "\tvalidated INTEGER,\n" +
                "\tremarks INTEGER\n" +
                ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        //db.execSQL("DROP TABLE IF EXISTS  FACULTY");
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
