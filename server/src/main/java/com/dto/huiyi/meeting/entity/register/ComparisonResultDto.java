package com.dto.huiyi.meeting.entity.register;

import com.huiyi.dao.ExternalMeetingParticipant;
import com.huiyi.dao.JCI_ORDER;
import com.huiyi.dao.JCI_ORDER_HOTEL;
import com.huiyi.meeting.dao.model.MeetingParticipant;
import com.huiyi.meeting.dao.model.MeetingStatement;

import java.io.Serializable;
import java.util.List;

public class ComparisonResultDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String companyName;
    private List<MeetingStatement> statements;
    private float statementTotal;
//    private List<ExternalMeetingParticipant> participants;
    private List<JCI_ORDER> jci_orders;
    private List<JCI_ORDER_HOTEL> jci_order_hotels;
    private float participantFeeTotal;
    private boolean isMatch;


    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public List<JCI_ORDER> getJci_orders() {
        return jci_orders;
    }

    public void setJci_orders(List<JCI_ORDER> jci_orders) {
        this.jci_orders = jci_orders;
    }

    public List<JCI_ORDER_HOTEL> getJci_order_hotels() {
        return jci_order_hotels;
    }

    public void setJci_order_hotels(List<JCI_ORDER_HOTEL> jci_order_hotels) {
        this.jci_order_hotels = jci_order_hotels;
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
