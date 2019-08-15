package com.ralph.adnu_hrmoattendancemonitoringmobileapplication;

public class Route {
    private String route_id;
    private String description;
    private String id;
    private String token;

    public Route(String id, String token) {
        this.id = id;
        this.token = token;
    }

    public String getRoute_id() {
        return route_id;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public String getToken() {
        return token;
    }
}
