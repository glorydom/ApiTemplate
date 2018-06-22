package com.dto.huiyi.meeting.entity.register;

import com.huiyi.meeting.dao.model.MeetingParticipant;

import java.io.Serializable;
import java.util.List;

public class ComparisonResultDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String companyName;
    private List<MeetingParticipant> participants;
    private boolean match;


    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public List<MeetingParticipant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<MeetingParticipant> participants) {
        this.participants = participants;
    }

    public boolean isMatch() {
        return match;
    }

    public void setMatch(boolean match) {
        this.match = match;
    }
}
