package com.ralph.adnu_hrmoattendancemonitoringmobileapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.sql.SQLInput;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "ahcfams.db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE FACULTY( faculty_id VARCHAR PRIMARY KEY NOT NULL, name VARCHAR NOT NULL, designation VARCHAR NOT NULL, password VARCHAR, department VARCHAR NOT NULL, college VARCHAR NOT NULL)");
        db.execSQL("CREATE TABLE STAFF ( staff_id VARCHAR PRIMARY KEY NOT NULL, last_login TEXT, route_id INTEGER, token VARCHAR)");
        db.execSQL("CREATE TABLE ROUTE( route_id INTEGER PRIMARY KEY NOT NULL, description VARCHAR)");
        db.execSQL("CREATE TABLE ROOM( room_id VARCHAR PRIMARY KEY NOT NULL, route_id INTEGER, buidling_name VARCHAR NOT NULL)");
        db.execSQL("CREATE TABLE CLASS_SCHEDULE( class_schedule_id VARCHAR PRIMARY KEY NOT NULL, room_id VARCHAR, faculty_id VARCHAR, semester INTEGER NOT NULL, school_year VARCHAR NOT NULL, start_time TEXT, end_time TEXT, class_section VARCHAR NOT NULL, class_day VARCHAR, subject_code VARCHAR NOT NULL, half_day INTEGER NOT NULL, hours FLOAT NOT NULL)");
        db.execSQL("CREATE TABLE FACULTY_ATTENDANCE( faculty_attendance_id VARCHAR PRIMARY KEY NOT NULL, staff_id VARCHAR NOT NULL, class_schedule_id VARCHAR NOT NULL, attendance_date TEXT NOT NULL, first_check TEXT, second_check TEXT, image_file VARCHAR, salary_deduction CHARACTER, status VARCHAR)");
        db.execSQL("CREATE TABLE CONFIRMATION_NOTICE( confirmation_notice_id VARCHAR PRIMARY KEY NOT NULL, faculty_attendance_id VARCHAR, confirmation_notice_date TEXT, reason VARCHAR, electronic_signature VARCHAR, remarks VARCHAR)");
        db.execSQL("CREATE TABLE ABSENCE_APPEAL( absence_appeal_id VARCHAR PRIMARY KEY NOT NULL, confirmation_notice_id VARCHAR NOT NULL, staff_id VARCHAR, chairperson_id VARCHAR, absence_appeal_reason VARCHAR, validated INTEGER, remarks INTEGER)");
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

    public boolean isTeacherRecorded(String faculty_id){
        SQLiteDatabase db = getReadableDatabase();
        Cursor res = db.rawQuery("select faculty_id from faculty where faculty_id = " + faculty_id, null);

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

    public boolean updateFaculty(String faculty_id,String name, String designation, String department, String college){

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("faculty_id", faculty_id);
        contentValues.put("name" , name);
        contentValues.put("designation", designation);
        contentValues.put("password", "null");
        contentValues.put("department", department);
        contentValues.put("college", college);

        long result;

        if(isTeacherRecorded(faculty_id)){
            result = db.update("FACULTY", contentValues, "faculty_id = " + faculty_id,null);
            if(result == -1)
                return false;
        }else
            result = db.insert("FACULTY",null,contentValues);

            if(result == -1)
                return false;
            else
                return true;

    }

    public boolean isRecorded(String id, String colName, String table){ // Checks if an id is already existing in the database
        SQLiteDatabase db = getReadableDatabase();
        Cursor res = db.rawQuery("select " + colName +" from " + table +" where " + colName + " = '" + id + "'", null);

        if(res.getCount() == 0)
            return false;
        else
            return true;

    }

    public boolean updateRoute(String route_id, String description){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("route_id", route_id);
        contentValues.put("description", description);

        long result;

        if(isRecorded(route_id, "route_id", "route")){
            result = db.update("ROUTE", contentValues, "route_id = " + route_id,null);
            if(result == -1)
                return false;
        }else
            result = db.insert("ROUTE", null, contentValues);

            if(result == -1)
                return false;
            else
                return true;
    }

    public boolean updateClassSchedule(String class_schedule_id, String room_id, String faculty_id, String semester, String school_year, String start_time, String end_time, String class_section, String class_day, String subject_code, String half_day, String hours){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("class_schedule_id", class_schedule_id);
        contentValues.put("room_id", room_id);
        contentValues.put("faculty_id", faculty_id);
        contentValues.put("semester", semester);
        contentValues.put("school_year", school_year);
        contentValues.put("start_time", start_time);
        contentValues.put("end_time", end_time);
        contentValues.put("class_section", class_section);
        contentValues.put("class_day", class_day);
        contentValues.put("subject_code", subject_code);
        contentValues.put("half_day", half_day);
        contentValues.put("hours", hours);

        long result;

        if(isRecorded(class_schedule_id, "class_schedule_id", "class_schedule")){
            result = db.update("CLASS_SCHEDULE", contentValues, "class_schedule_id = " + class_schedule_id,null);
            if(result == -1)
                return false;
        }else
            result = db.insert("CLASS_SCHEDULE", null, contentValues);

        if(result == -1)
            return false;
        else
            return true;
    }


}
