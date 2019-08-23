package com.ralph.adnu_hrmoattendancemonitoringmobileapplication;

public class Route {
    private String ROUTE_ID;
    private String DESCRIPTION;
    private String id;
    private String token;

    public Route(String id, String token) {
        this.id = id;
        this.token = token;
    }

    public String getROUTE_ID() {
        return ROUTE_ID;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public String getId() {
        return id;
    }

    public String getToken() {
        return token;
    }
}
