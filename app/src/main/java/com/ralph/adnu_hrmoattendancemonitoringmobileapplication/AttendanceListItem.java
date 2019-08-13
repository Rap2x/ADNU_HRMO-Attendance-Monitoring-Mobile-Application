package com.ralph.adnu_hrmoattendancemonitoringmobileapplication;

public class AttendanceListItem {
    private String name;
    private String subjectCode;
    private String roomNumber;

    public AttendanceListItem(String name, String subjectCode, String roomNumber) {
        this.name = name;
        this.subjectCode = subjectCode;
        this.roomNumber = roomNumber;
    }

    public String getName() {
        return name;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public String getRoomNumber() {
        return roomNumber;
    }
}
