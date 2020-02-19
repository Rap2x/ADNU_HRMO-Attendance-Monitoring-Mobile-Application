package com.ralph.adnu_hrmoattendancemonitoringmobileapplication;

public class FacultyAttendance {
    private String id;
    private String token;
    private String faid;
    private String sid;
    private String csid;
    private String adate;
    private String fcheck;
    private String scheck;
    private String fipath;
    private String sipath;
    private String sdeduct;
    private String status;
    private String validated;

    private String message;

    private String FACULTY_ATTENDANCE_ID;
    private String STAFF_ID;
    private String CLASS_SCHEDULE_ID;
    private String CONFIRMATION_NOTICE_ID;
    private String ROOM_ID;
    private String ATTENDANCE_DATE;
    private String FIRST_CHECK;
    private String SECOND_CHECK;
    private String FIRST_IMAGE_FILE;
    private String SECOND_IMAGE_FILE;
    private String STATUS;
    private String VALIDATED;


    public FacultyAttendance(String id, String token, String faid, String sid, String csid, String adate, String fcheck, String scheck, String fipath, String sipath, String sdeduct, String status, String validated) {
        this.id = id;
        this.token = token;
        this.faid = faid;
        this.sid = sid;
        this.csid = csid;
        this.adate = adate;
        this.fcheck = fcheck;
        this.scheck = scheck;
        this.fipath = fipath;
        this.sipath = sipath;
        this.sdeduct = sdeduct;
        this.status = status;
        this.validated = validated;
    }

    public FacultyAttendance(String id, String token) {
        this.id = id;
        this.token = token;
    }

    public void setFaid(String faid) {
        this.faid = faid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public void setCsid(String csid) {
        this.csid = csid;
    }

    public void setAdate(String adate) {
        this.adate = adate;
    }

    public void setFcheck(String fcheck) {
        this.fcheck = fcheck;
    }

    public void setScheck(String scheck) {
        this.scheck = scheck;
    }

    public void setFipath(String fipath) {
        this.fipath = fipath;
    }

    public void setSipath(String sipath) {
        this.sipath = sipath;
    }

    public void setSdeduct(String sdeduct) {
        this.sdeduct = sdeduct;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public String getFaid() {
        return faid;
    }

    public String getSid() {
        return sid;
    }

    public String getCsid() {
        return csid;
    }

    public String getAdate() {
        return adate;
    }

    public String getFcheck() {
        return fcheck;
    }

    public String getScheck() {
        return scheck;
    }

    public String getFipath() {
        return fipath;
    }

    public String getSipath() {
        return sipath;
    }

    public String getSdeduct() {
        return sdeduct;
    }

    public String getStatus() {
        return status;
    }

    public String getValidated() {
        return validated;
    }

    public String getFACULTY_ATTENDANCE_ID() {
        return FACULTY_ATTENDANCE_ID;
    }

    public String getSTAFF_ID() {
        return STAFF_ID;
    }

    public String getCLASS_SCHEDULE_ID() {
        return CLASS_SCHEDULE_ID;
    }

    public String getATTENDANCE_DATE() {
        return ATTENDANCE_DATE;
    }

    public String getFIRST_CHECK() {
        return FIRST_CHECK;
    }

    public String getSECOND_CHECK() {
        return SECOND_CHECK;
    }

    public String getFIRST_IMAGE_FILE() {
        return FIRST_IMAGE_FILE;
    }

    public String getSECOND_IMAGE_FILE() {
        return SECOND_IMAGE_FILE;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public String getROOM_ID() {
        return ROOM_ID;
    }

    public String getMessage() {
        return message;
    }

    public String getCONFIRMATION_NOTICE_ID() {
        return CONFIRMATION_NOTICE_ID;
    }

    public String getVALIDATED() {
        return VALIDATED;
    }
}
