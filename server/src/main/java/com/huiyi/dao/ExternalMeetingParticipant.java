package com.huiyi.dao;

import java.io.Serializable;
import java.util.Date;

public class ExternalMeetingParticipant implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String companyName;
    private String telephone;
    private String participantName;
    private float fee;
    private String isDisabled;
    private Date registTime;


    @Override
    public String toString() {
        return "ExternalMeetingParticipant{" +
                "id=" + id +
                ", companyName='" + companyName + '\'' +
                ", telephone='" + telephone + '\'' +
                ", participantName='" + participantName + '\'' +
                ", fee=" + fee +
                ", isDisabled='" + isDisabled + '\'' +
                ", registTime=" + registTime +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getParticipantName() {
        return participantName;
    }

    public void setParticipantName(String participantName) {
        this.participantName = participantName;
    }

    public float getFee() {
        return fee;
    }

    public void setFee(float fee) {
        this.fee = fee;
    }

    public String getIsDisabled() {
        return isDisabled;
    }

    public void setIsDisabled(String isDisabled) {
        this.isDisabled = isDisabled;
    }

    public Date getRegistTime() {
        return registTime;
    }

    public void setRegistTime(Date registTime) {
        this.registTime = registTime;
    }
}
