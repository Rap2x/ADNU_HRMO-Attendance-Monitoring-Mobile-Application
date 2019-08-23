package com.ralph.adnu_hrmoattendancemonitoringmobileapplication;

public class Faculty {
    private String id;
    private String FACULTY_ID;
    private String NAME;
    private String DEPARTMENT;
    private String COLLEGE;
    private String token;


    public Faculty(String id, String token) {
        this.id = id;
        this.token = token;
    }

    public String getFACULTY_ID() {
        return FACULTY_ID;
    }

    public String getNAME() {
        return NAME;
    }

    public String getDEPARTMENT() {
        return DEPARTMENT;
    }

    public String getCOLLEGE() {
        return COLLEGE;
    }
}
