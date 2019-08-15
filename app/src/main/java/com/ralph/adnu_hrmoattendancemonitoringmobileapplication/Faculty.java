package com.ralph.adnu_hrmoattendancemonitoringmobileapplication;

public class Faculty {
    private String faculty_id;
    private String id;
    private String name;
    private String designation;
    private String password;
    private String department;
    private String college;
    private String token;

    public Faculty(String id, String token) {
        this.id = id;
        this.token = token;
    }

    public String getFaculty_id() {
        return faculty_id;
    }

    public String getName() {
        return name;
    }

    public String getDesignation() {
        return designation;
    }

    public String getPassword() {
        return password;
    }

    public String getDepartment() {
        return department;
    }

    public String getCollege() {
        return college;
    }
}
