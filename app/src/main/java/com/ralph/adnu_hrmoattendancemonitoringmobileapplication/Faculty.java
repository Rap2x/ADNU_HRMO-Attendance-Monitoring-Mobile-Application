package com.ralph.adnu_hrmoattendancemonitoringmobileapplication;

public class Faculty {
    private String id;
    private String name;
    private String designation;
    private String password;
    private String department;
    private String college;
    private String token;

    public Faculty(String faculty_id, String token) {
        this.id = faculty_id;
        this.token = token;
    }

    public String getId() {
        return id;
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

    public String getToken() {
        return token;
    }
}
