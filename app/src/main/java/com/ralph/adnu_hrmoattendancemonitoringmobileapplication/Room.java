package com.ralph.adnu_hrmoattendancemonitoringmobileapplication;

public class Room {
    private String ROOM_ID;
    private String ROOM_NAME;
    private String ROUTE;
    private String BUILDING_NAME;
    private String id;
    private String token;

    public Room(String id, String token) {
        this.id = id;
        this.token = token;
    }

    public String getROOM_ID() {
        return ROOM_ID;
    }

    public String getROUTE() {
        return ROUTE;
    }

    public String getBUILDING_NAME() {
        return BUILDING_NAME;
    }

    public String getROOM_NAME() {
        return ROOM_NAME;
    }
}
