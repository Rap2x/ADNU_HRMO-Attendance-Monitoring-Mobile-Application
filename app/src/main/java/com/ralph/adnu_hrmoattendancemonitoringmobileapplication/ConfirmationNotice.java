package com.ralph.adnu_hrmoattendancemonitoringmobileapplication;

public class ConfirmationNotice {
    private String id;
    private String token;

    private String CONFIRMATION_NOTICE_ID;
    private String CONFIRMATION_NOTICE_DATE;
    private String ELECTRONIC_SIGNATURE;
    private String REMARKS;
    private String REASON;
    private String CONFIRMED;

    private String status;
    private String message;
    private String faid;

    public ConfirmationNotice(String id, String token) {
        this.id = id;
        this.token = token;
    }

    public ConfirmationNotice(String CONFIRMATION_NOTICE_ID, String CONFIRMATION_NOTICE_DATE, String ELECTRONIC_SIGNATURE, String REMARKS, String REASON, String CONFIRMED) {
        this.CONFIRMATION_NOTICE_ID = CONFIRMATION_NOTICE_ID;
        this.CONFIRMATION_NOTICE_DATE = CONFIRMATION_NOTICE_DATE;
        this.ELECTRONIC_SIGNATURE = ELECTRONIC_SIGNATURE;
        this.REMARKS = REMARKS;
        this.REASON = REASON;
        this.CONFIRMED = CONFIRMED;
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

    public String getCONFIRMATION_NOTICE_DATE() {
        return CONFIRMATION_NOTICE_DATE;
    }

    public String getELECTRONIC_SIGNATURE() {
        return ELECTRONIC_SIGNATURE;
    }

    public String getREMARKS() {
        return REMARKS;
    }

    public String getREASON() {
        return REASON;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getFaid() {
        return faid;
    }

    public String getCONFIRMED() {
        return CONFIRMED;
    }
}
