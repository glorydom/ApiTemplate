package com.dto.huiyi.meeting.entity;

import java.io.Serializable;
import java.util.Map;

public class TaskCompleteDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String taskId;
    private String userId;
    private String comment;
    private Map<String, Object> completionArguments;


    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
}
