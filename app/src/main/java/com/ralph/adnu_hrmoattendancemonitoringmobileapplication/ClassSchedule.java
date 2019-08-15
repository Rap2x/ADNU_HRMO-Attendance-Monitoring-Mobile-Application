package com.ralph.adnu_hrmoattendancemonitoringmobileapplication;

public class ClassSchedule {
    private String id;
    private String token;
    private String class_schedule_id;
    private String room_id;
    private String faculty_id;
    private String semester;
    private String school_year;
    private String start_time;
    private String end_time;
    private String class_section;
    private String class_day;
    private String subject_code;
    private String half_day;
    private String hours;

    public ClassSchedule(String id, String token) {
        this.id = id;
        this.token = token;
    }

    public String getClass_schedule_id() {
        return class_schedule_id;
    }

    public String getRoom_id() {
        return room_id;
    }

    public String getFaculty_id() {
        return faculty_id;
    }

    public String getSemester() {
        return semester;
    }

    public String getSchool_year() {
        return school_year;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public String getClass_section() {
        return class_section;
    }

    public String getClass_day() {
        return class_day;
    }

    public String getSubject_code() {
        return subject_code;
    }

    public String getHalf_day() {
        return half_day;
    }

    public String getHours() {
        return hours;
    }
}
