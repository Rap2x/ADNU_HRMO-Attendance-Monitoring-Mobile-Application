package com.ralph.adnu_hrmoattendancemonitoringmobileapplication;

public class ConfirmationNoticeListItem {
    private String confirmationNoticeId;
    private String facultyId;
    private String facultyName;
    private String subjectCode;
    private String time;
    private String date;

    public ConfirmationNoticeListItem(String confirmationNoticeId, String facultyId, String facultyName, String subjectCode, String time, String date) {
        this.confirmationNoticeId = confirmationNoticeId;
        this.facultyId = facultyId;
        this.facultyName = facultyName;
        this.subjectCode = subjectCode;
        this.time = time;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public String getConfirmationNoticeId() {
        return confirmationNoticeId;
    }

    public String getFacultyId() {
        return facultyId;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public String getTime() {
        return time;
    }

    public void setConfirmationNoticeId(String confirmationNoticeId) {
        this.confirmationNoticeId = confirmationNoticeId;
    }

    public void setFacultyId(String facultyId) {
        this.facultyId = facultyId;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
