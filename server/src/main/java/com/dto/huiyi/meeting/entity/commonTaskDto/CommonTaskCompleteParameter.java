package com.dto.huiyi.meeting.entity.commonTaskDto;

import com.dto.huiyi.meeting.entity.TaskCompleteDto;
import com.huiyi.meeting.dao.model.MeetingCommonTask;

import java.io.Serializable;
import java.util.Map;

public class CommonTaskCompleteParameter implements Serializable {

    private static final long serialVersionUID = 1L;
    private String taskId;
    private String comment;
    private Map<String, Object> completionArguments;
    private MeetingCommonTask meetingCommonTask;
    private boolean auditResult;

    public boolean isAuditResult() {
        return auditResult;
    }

    public void setAuditResult(boolean auditResult) {
        this.auditResult = auditResult;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Map<String, Object> getCompletionArguments() {
        return completionArguments;
    }

    public void setCompletionArguments(Map<String, Object> completionArguments) {
        this.completionArguments = completionArguments;
    }

    public MeetingCommonTask getMeetingCommonTask() {
        return meetingCommonTask;
    }

    public void setMeetingCommonTask(MeetingCommonTask meetingCommonTask) {
        this.meetingCommonTask = meetingCommonTask;
    }
}
