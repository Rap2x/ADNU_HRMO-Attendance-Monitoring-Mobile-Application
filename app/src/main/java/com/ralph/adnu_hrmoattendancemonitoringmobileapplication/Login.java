package com.ralph.adnu_hrmoattendancemonitoringmobileapplication;

public class Login {
    private String username;
    private String password;
    private String status;
    private String message;
    private String staff_id;
    private String token;
    private String DateTime;
    private String route_id;

    public Login(String username,String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return staff_id;
    }

    public String getPassword() {
        return password;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }

    public String getDateTime() {
        return DateTime;
    }

    public String getRoute_id() {
        return route_id;
    }
}
