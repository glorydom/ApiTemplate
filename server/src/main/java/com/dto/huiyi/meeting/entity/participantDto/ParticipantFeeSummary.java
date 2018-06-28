package com.dto.huiyi.meeting.entity.participantDto;

import java.io.Serializable;

public class ParticipantFeeSummary implements Serializable {

    private static final long serialVersionUID = 1L;

    private String date;
    private int paidParticipantCount;
    private float totalFee;
    private int unpaidParticipantCount;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(float totalFee) {
        this.totalFee = totalFee;
    }

    public int getPaidParticipantCount() {
        return paidParticipantCount;
    }

    public void setPaidParticipantCount(int paidParticipantCount) {
        this.paidParticipantCount = paidParticipantCount;
    }

    public int getUnpaidParticipantCount() {
        return unpaidParticipantCount;
    }

    public void setUnpaidParticipantCount(int unpaidParticipantCount) {
        this.unpaidParticipantCount = unpaidParticipantCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParticipantFeeSummary summary = (ParticipantFeeSummary) o;

        return date != null ? date.equals(summary.date) : summary.date == null;
    }

    @Override
    public int hashCode() {
        return date != null ? date.hashCode() : 0;
    }
}
