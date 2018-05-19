package com.dto.huiyi.meeting.entity;

public class DeployedProcess {
    private String processKey;

    public String getProcessKey() {
        return processKey;
    }

    public void setProcessKey(String processKey) {
        this.processKey = processKey;
    }

    @Override
    public String toString() {
        return "DeployedProcess{" +
                "processKey='" + processKey + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeployedProcess that = (DeployedProcess) o;

        return processKey != null ? processKey.equals(that.processKey) : that.processKey == null;
    }

    @Override
    public int hashCode() {
        return processKey != null ? processKey.hashCode() : 0;
    }
}
