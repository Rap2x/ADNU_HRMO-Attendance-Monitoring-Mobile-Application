package com.ralph.adnu_hrmoattendancemonitoringmobileapplication;

public class ClassSchedule {
    private String id;
    private String token;
    private String CLASS_SCHEDULE_ID;
    private String FACULTY_ID;
    private String SEMESTER;
    private String SCHOOL_YEAR;
    private String START_TIME;
    private String END_TIME;
    private String CLASS_SECTION;
    private String CLASS_DAY;
    private String SUBJECT_CODE;
    private String HOURS;

    public ClassSchedule(String id, String token) {
        this.id = id;
        this.token = token;
    }

    public String getCLASS_SCHEDULE_ID() {
        return CLASS_SCHEDULE_ID;
    }

    public String getFACULTY_ID() {
        return FACULTY_ID;
    }

    public String getSEMESTER() {
        return SEMESTER;
    }

    public String getSCHOOL_YEAR() {
        return SCHOOL_YEAR;
    }

    public String getSTART_TIME() {
        return START_TIME;
    }

    public String getEND_TIME() {
        return END_TIME;
    }

    public String getCLASS_SECTION() {
        return CLASS_SECTION;
    }

    public String getCLASS_DAY() {
        return CLASS_DAY;
    }

    public String getSUBJECT_CODE() {
        return SUBJECT_CODE;
    }

    public String getHOURS() {
        return HOURS;
    }
}
