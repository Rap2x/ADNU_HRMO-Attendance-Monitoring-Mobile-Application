package com.ralph.adnu_hrmoattendancemonitoringmobileapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

@RequiresApi(api = Build.VERSION_CODES.N)
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "ahcfams.db";
    private SQLiteDatabase readDB = getReadableDatabase();
    private SQLiteDatabase writeDB = getWritableDatabase();

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE FACULTY( faculty_id VARCHAR PRIMARY KEY NOT NULL, name VARCHAR NOT NULL, department VARCHAR NOT NULL, college VARCHAR NOT NULL)");
        db.execSQL("CREATE TABLE STAFF ( staff_id VARCHAR PRIMARY KEY NOT NULL, last_login TEXT, route_id INTEGER, token VARCHAR)");
        db.execSQL("CREATE TABLE ROUTE( route_id INTEGER PRIMARY KEY NOT NULL, description VARCHAR)");
        db.execSQL("CREATE TABLE ROOM( room_id VARCHAR PRIMARY KEY NOT NULL, route_id INTEGER, building_name VARCHAR NOT NULL, room_order VARCHAR NOT NULL)");
        db.execSQL("CREATE TABLE CLASS_SCHEDULE( class_schedule_id VARCHAR PRIMARY KEY NOT NULL, room_id VARCHAR, faculty_id VARCHAR, semester INTEGER NOT NULL, school_year VARCHAR NOT NULL, start_time TEXT, end_time TEXT, class_section VARCHAR NOT NULL, class_day VARCHAR, subject_code VARCHAR NOT NULL, half_day INTEGER NOT NULL, hours FLOAT NOT NULL)");
        db.execSQL("CREATE TABLE FACULTY_ATTENDANCE( faculty_attendance_id VARCHAR PRIMARY KEY NOT NULL, staff_id VARCHAR NOT NULL, class_schedule_id VARCHAR NOT NULL, attendance_date TEXT NOT NULL, first_check TEXT, second_check TEXT, image_file VARCHAR, salary_deduction CHARACTER, status VARCHAR, synchronized VARCHAR)");
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
        ContentValues contentValues = new ContentValues();

        contentValues.put("staff_id", username);
        contentValues.put("last_login", last_login);
        contentValues.put("route_id","null");
        contentValues.put("token", token);

        long result;

        if(isUserRecorded(username)){
            result = writeDB.update("STAFF", contentValues, "staff_id = " + username,null);

            if(result == -1){
                return false;
            }

        }else{
            result = writeDB.insert("STAFF",null,contentValues);
        }

        if(result == -1)
            return false;
        else
            return true;

    }


    public ArrayList getUserCredentials(){
        ArrayList<String> arrayList = new ArrayList<String>();

        Cursor res = readDB.rawQuery("select staff_id, token from STAFF",null);

        res.moveToLast();
        arrayList.add(res.getString(0));
        arrayList.add(res.getString(1));

        return arrayList;
    }

    public boolean isUserRecorded(String staff_id){
        Cursor res = readDB.rawQuery("select staff_id from staff where staff_id =" + staff_id, null);


        if(res.getCount() == 0)
            return false;
        else
            return true;

    }

    public boolean isTeacherRecorded(String faculty_id){
        Cursor res = readDB.rawQuery("select faculty_id from faculty where faculty_id = " + faculty_id, null);


        if(res.getCount() == 0)
            return false;
        else
            return true;

    }

    public ArrayList getFaculty(){
        ArrayList<String> arrayList = new ArrayList<String>();
        Cursor res = readDB.rawQuery("select * from faculty", null);

        res.moveToLast();

        arrayList.add(res.getString(res.getColumnIndex("faculty_id")));


        return arrayList;
    }

    public boolean updateFaculty(String faculty_id,String name , String department, String college){
        ContentValues contentValues = new ContentValues();

        contentValues.put("faculty_id", faculty_id);
        contentValues.put("name" , name);
        contentValues.put("department", department);
        contentValues.put("college", college);

        long result;

        if(isTeacherRecorded(faculty_id)){
            result = writeDB.update("FACULTY", contentValues, "faculty_id = " + faculty_id,null);

            if(result == -1)
                return false;
        }else{
            result = writeDB.insert("FACULTY",null,contentValues);

        }

        if(result == -1){
            return false;}
        else{
            return true;}

    }

    public boolean isRecorded(String id, String colName, String table){ // Checks if an id is already existing in the database
        Cursor res = readDB.rawQuery("select " + colName +" from " + table +" where " + colName + " = '" + id + "'", null);

        if(res.getCount() == 0){
            return false;
        }else{
            return true;
        }

    }

    public boolean updateRoute(String route_id, String description){
        ContentValues contentValues = new ContentValues();

        contentValues.put("route_id", route_id);
        contentValues.put("description", description);

        long result;

        if(isRecorded(route_id, "route_id", "route")){
            result = writeDB.update("ROUTE", contentValues, "route_id = " + route_id,null);
            if(result == -1)
                return false;
        }else{
            result = writeDB.insert("ROUTE", null, contentValues);
        }if(result == -1)
            return false;
        else
            return true;

    }

    public boolean updateClassSchedule(String class_schedule_id, String room_id, String faculty_id, String semester, String school_year, String start_time, String end_time, String class_section, String class_day, String subject_code, String half_day, String hours){
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
            result = writeDB.update("CLASS_SCHEDULE", contentValues, "class_schedule_id = '" + class_schedule_id + "'",null);
            if(result == -1)
                return false;
        }else
            result = writeDB.insert("CLASS_SCHEDULE", null, contentValues);

        if(result == -1){
            return false;
        }else{
            return true;
        }

    }

    public ArrayList<String> getSchedule(){
        ArrayList<String> arrayList = new ArrayList<>();

        Cursor res = readDB.rawQuery("select class_schedule_id from class_schedule",null);
        res.moveToFirst();
        do{
            arrayList.add(res.getString(res.getColumnIndex("class_schedule_id")));
        }while(res.moveToNext());

        return arrayList;
    }

    public String generateRandomString(int size){
        String pool = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        String randomString = "";
        Random rand = new Random();

        for(int i =0; i< size; i++){
            int length = pool.length();
            int n = rand.nextInt(length);
            randomString += pool.charAt(n);
        }

        return randomString;
    }

    public void createAttendance(){
        ArrayList<String> classScheduleId = getSchedule();
        ContentValues contentValues = new ContentValues();
        String faculty_attendance_id;
        int scheduleSize = getScheduleSize();

        for(int i = 0; i < scheduleSize; i++){
            do{
                faculty_attendance_id = generateRandomString(9);

                contentValues.put("faculty_attendance_id", faculty_attendance_id);
                contentValues.put("staff_id", MainActivity.userStaffId);
                contentValues.put("class_schedule_id", classScheduleId.get(i));
                contentValues.put("attendance_date", MainActivity.getCurrentDate());
                contentValues.put("first_check", "null");
                contentValues.put("second_check", "null");
                contentValues.put("image_file", "null");
                contentValues.put("salary_deduction", "null");
                contentValues.put("status", "null");
                contentValues.put("synchronized", "0");

            }while(isRecorded(faculty_attendance_id, "faculty_attendance_id", "FACULTY_ATTENDANCE"));

            if (!isAttendanceDuplicated(classScheduleId.get(i))){
                writeDB.insert("FACULTY_ATTENDANCE", null, contentValues);
            }
        }

    }

    public boolean isAttendanceDuplicated(String class_schedule_id){
        Cursor res = readDB.rawQuery("select faculty_attendance_id from faculty_attendance where class_schedule_id = '" + class_schedule_id + "' and attendance_date = '" + MainActivity.getCurrentDate() + "'",null);
        int count = res.getCount();


        if(count > 0)
            return true;
        else
            return false;

        // Return true if there's a duplicate in the table false if unique
    }

    public int getScheduleSize(){

        Cursor res = readDB.rawQuery("select * from class_schedule", null);
        int count = res.getCount();

        return count;
    }

    public int getAttendanceSize(){
        Cursor res = readDB.rawQuery("select * from faculty_attendance",null);

        int count = res.getCount();
        return count;
    }

    public Cursor getFacultyAttendance(){

        Cursor res = readDB.rawQuery("SELECT faculty_attendance_id, class_schedule.room_id, class_schedule.subject_code,faculty.name, class_schedule.start_time || ' - ' || class_schedule.end_time AS Class_Time, faculty_attendance.first_check, faculty_attendance.second_check FROM faculty_attendance INNER JOIN class_schedule ON faculty_attendance.class_schedule_id = class_schedule.class_schedule_id INNER JOIN faculty ON class_schedule.faculty_id = faculty.faculty_id",null);
        return res;
    }

    public boolean checkFirstAttendance(String id, String time){

        ContentValues contentValues = new ContentValues();

        long result;

        contentValues.put("first_check", time);

        result = writeDB.update("faculty_attendance", contentValues, "faculty_attendance_id = '" + id + "'", null);

        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean updateRoom(String room_id, String route_id, String building_name, String room_order){

        ContentValues contentValues = new ContentValues();

        long result;

        contentValues.put("room_id", room_id);
        contentValues.put("route_id", route_id);
        contentValues.put("building_name", building_name);
        contentValues.put("room_order", room_order);

        if(isRecorded(room_id, "room_id", "room")){
            result = writeDB.update("ROOM", contentValues, "room_id = '" + room_id+ "'",null);
            if(result == -1)
                return false;
        }else
            result = writeDB.insert("room", null, contentValues);

        if(result == -1){
            return false;
        }else{
            return true;
        }

    }


}
