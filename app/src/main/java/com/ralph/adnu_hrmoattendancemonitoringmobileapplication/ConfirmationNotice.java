package com.ralph.adnu_hrmoattendancemonitoringmobileapplication;

public class ConfirmationNotice {
    private String id;
    private String token;

    private String CONFIRMATION_NOTICE_ID;
    private String FACULTY_ATTENDANCE_ID;
    private String CONFIRMATION_NOTICE_DATE;
    private String REASON;
    private String ELECTRONIC_SIGNATURE;
    private String REMARKS;

    public ConfirmationNotice(String id, String token) {
        this.id = id;
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public String getCONFIRMATION_NOTICE_ID() {
        return CONFIRMATION_NOTICE_ID;
    }

    public String getFACULTY_ATTENDANCE_ID() {
        return FACULTY_ATTENDANCE_ID;
    }

    public String getCONFIRMATION_NOTICE_DATE() {
        return CONFIRMATION_NOTICE_DATE;
    }

    public String getREASON() {
        return REASON;
    }

    public String getELECTRONIC_SIGNATURE() {
        return ELECTRONIC_SIGNATURE;
    }

    public String getREMARKS() {
        return REMARKS;
    }
}
