package com.ralph.adnu_hrmoattendancemonitoringmobileapplication;

public class ConfirmationNoticeListItem {

    private String facultyAttendanceId;
    private String facultyId;
    private String facultyName;
    private String subjectCode;
    private String time;
    private String date;
    private String confirmatioNoticeId;

    public ConfirmationNoticeListItem(String facultyAttendanceId, String facultyId, String facultyName, String subjectCode, String time, String date, String confirmatioNoticeId) {
        this.facultyAttendanceId = facultyAttendanceId;
        this.facultyId = facultyId;
        this.facultyName = facultyName;
        this.subjectCode = subjectCode;
        this.time = time;
        this.date = date;
        this.confirmatioNoticeId = confirmatioNoticeId;
    }

    public String getDate() {
        return date;
    }

    public String getFacultyAttendanceId() {
        return facultyAttendanceId;
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

    public String getConfirmatioNoticeId() {
        return confirmatioNoticeId;
    }
}
