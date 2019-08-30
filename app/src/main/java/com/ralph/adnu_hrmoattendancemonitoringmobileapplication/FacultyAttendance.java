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

    public FacultyAttendance(String id, String token, String faid, String sid, String csid, String adate, String fcheck, String scheck, String fipath, String sipath, String sdeduct, String status) {
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
}
