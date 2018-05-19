package com.dto.huiyi.meeting.entity.meetingDto;

import java.io.Serializable;
import java.util.Map;

public class ProcessStartParameter implements Serializable {

    private static final long serialVersionUID = 1L;

    private Map<String, Object> parameters;
    private String processId;
    private String bussinessId;

    @Override
    public String toString() {
        return "ProcessStartParameter{" +
                "parameters=" + parameters +
                ", processId='" + processId + '\'' +
                ", bussinessId='" + bussinessId + '\'' +
                '}';
    }

    public ProcessStartParameter() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getBussinessId() {
        return bussinessId;
    }

    public void setBussinessId(String bussinessId) {
        this.bussinessId = bussinessId;
    }
}
