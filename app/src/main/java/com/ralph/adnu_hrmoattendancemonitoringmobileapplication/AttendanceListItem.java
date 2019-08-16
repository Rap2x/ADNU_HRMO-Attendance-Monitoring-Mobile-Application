package com.ralph.adnu_hrmoattendancemonitoringmobileapplication;

public class AttendanceListItem {
    private String name;
    private String subjectCode;
    private String roomNumber;
    private String classTime;
    private String facultyAttendance_Id;
    private String first;
    private String second;

    public AttendanceListItem(String name, String subjectCode, String roomNumber, String classTime, String facultyAttendance_Id, String first, String second) {
        this.name = name;
        this.subjectCode = subjectCode;
        this.roomNumber = roomNumber;
        this.classTime = classTime;
        this.facultyAttendance_Id = facultyAttendance_Id;
        this.first = first;
        this.second = second;
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

    public String getClassTime() {
        return classTime;
    }

    public String getFacultyAttendance_Id() {
        return facultyAttendance_Id;
    }

    public String getFirst() {
        return first;
    }

    public String getSecond() {
        return second;
    }

    public void setFirst(String firstTime) {
        first = firstTime;
    }

    public void setSecond(String secondTime) {
        second = secondTime;
    }
}
