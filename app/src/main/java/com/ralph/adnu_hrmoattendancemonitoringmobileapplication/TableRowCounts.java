package com.ralph.adnu_hrmoattendancemonitoringmobileapplication;

public class TableRowCounts {
    private String id;
    private String token;
    private String ABSENCE_APPEAL;
    private String CLASS_SCHEDULE;
    private String CONFIRMATION_NOTICE;
    private String FACULTY;
    private String FACULTY_ATTENDANCE;
    private String ROOM;
    private String ROUTE;
    private String STAFF;

    public TableRowCounts(String id, String token) {
        this.id = id;
        this.token = token;
    }

    public String getABSENCE_APPEAL() {
        return ABSENCE_APPEAL;
    }

    public String getCLASS_SCHEDULE() {
        return CLASS_SCHEDULE;
    }

    public String getCONFIRMATION_NOTICE() {
        return CONFIRMATION_NOTICE;
    }

    public String getFACULTY() {
        return FACULTY;
    }

    public String getFACULTY_ATTENDANCE() {
        return FACULTY_ATTENDANCE;
    }

    public String getROOM() {
        return ROOM;
    }

    public String getROUTE() {
        return ROUTE;
    }

    public String getSTAFF() {
        return STAFF;
    }
}
