package com.dto.huiyi.meeting.entity.register;

import com.huiyi.dao.ExternalMeetingParticipant;
import com.huiyi.meeting.dao.model.MeetingParticipant;
import com.huiyi.meeting.dao.model.MeetingStatement;

import java.io.Serializable;
import java.util.List;

public class ComparisonResultDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String companyName;
    private List<MeetingStatement> statements;
    private float statementTotal;
    private List<ExternalMeetingParticipant> participants;
    private float participantFeeTotal;
    private boolean isMatch;


    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public List<ExternalMeetingParticipant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<ExternalMeetingParticipant> participants) {
        this.participants = participants;
    }

    public boolean isMatch() {
        return isMatch;
    }

    public void setMatch(boolean match) {
        isMatch = match;
    }

    public List<MeetingStatement> getStatements() {
        return statements;
    }

    public void setStatements(List<MeetingStatement> statements) {
        this.statements = statements;
    }

    public float getStatementTotal() {
        return statementTotal;
    }

    public void setStatementTotal(float statementTotal) {
        this.statementTotal = statementTotal;
    }

    public float getParticipantFeeTotal() {
        return participantFeeTotal;
    }

    public void setParticipantFeeTotal(float participantFeeTotal) {
        this.participantFeeTotal = participantFeeTotal;
    }
}
