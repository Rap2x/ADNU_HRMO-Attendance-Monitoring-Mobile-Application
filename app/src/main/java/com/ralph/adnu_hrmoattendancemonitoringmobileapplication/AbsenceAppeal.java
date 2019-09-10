package com.ralph.adnu_hrmoattendancemonitoringmobileapplication;

public class AbsenceAppeal {
    private String id;
    private String token;

    private String ABSENCE_APPEAL_ID;
    private String CONFIRMATION_NOTICE_ID;
    private String STAFF_ID;
    private String CHAIRPERSON_ID;
    private String ABSENCE_APPEAL_REASON;
    private String VALIDATED;
    private String REMARKS;

    private String aid;
    private String cid;
    private String staff_id;
    private String chair_id;
    private String absence_reason;
    private String validated;
    private String remarks;



    public AbsenceAppeal(String id, String token) {
        this.id = id;
        this.token = token;
    }

    public AbsenceAppeal(String id, String token, String aid, String cid, String staff_id, String chair_id, String absence_reason, String validated, String remarks) {
        this.id = id;
        this.token = token;
        this.aid = aid;
        this.cid = cid;
        this.staff_id = staff_id;
        this.chair_id = chair_id;
        this.absence_reason = absence_reason;
        this.validated = validated;
        this.remarks = remarks;
    }

    public String getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public String getABSENCE_APPEAL_ID() {
        return ABSENCE_APPEAL_ID;
    }

    public String getCONFIRMATION_NOTICE_ID() {
        return CONFIRMATION_NOTICE_ID;
    }

    public String getSTAFF_ID() {
        return STAFF_ID;
    }

    public String getCHAIRPERSON_ID() {
        return CHAIRPERSON_ID;
    }

    public String getABSENCE_APPEAL_REASON() {
        return ABSENCE_APPEAL_REASON;
    }

    public String getVALIDATED() {
        return VALIDATED;
    }

    public String getREMARKS() {
        return REMARKS;
    }
}
