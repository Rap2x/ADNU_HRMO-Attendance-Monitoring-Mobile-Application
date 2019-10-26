package com.ralph.adnu_hrmoattendancemonitoringmobileapplication;

public class AttendanceListItem {
    private String name;
    private String subjectCode;
    private String roomNumber;
    private String classTime;
    private String facultyAttendance_Id;
    private String first;
    private String second;
    private String faculty_id;
    private String noticeCount;
    private String firstImageFile;
    private String secondImageFile;

    private String set;

    public AttendanceListItem(String name, String subjectCode, String roomNumber, String classTime, String facultyAttendance_Id, String first, String second, String faculty_id, String noticeCount, String firstImageFile, String secondImageFile) {
        this.name = name;
        this.subjectCode = subjectCode;
        this.roomNumber = roomNumber;
        this.classTime = classTime;
        this.facultyAttendance_Id = facultyAttendance_Id;
        this.first = first;
        this.second = second;
        this.faculty_id = faculty_id;
        this.noticeCount = noticeCount;
        this.firstImageFile = firstImageFile;
        this.secondImageFile = secondImageFile;
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

    public String getFaculty_id() {
        return faculty_id;
    }

    public String getNoticeCount() {
        return noticeCount;
    }

    public String getSet() {
        return set;
    }

    public void setSet(String set) {
        this.set = set;
    }

    public String getFirstImageFile() {
        return firstImageFile;
    }

    public String getSecondImageFile() {
        return secondImageFile;
    }
}
