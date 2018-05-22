package com.dto.huiyi.meeting.entity.ManualScriptDto;

import com.huiyi.meeting.dao.model.MeetingScriptmanual;

import java.io.Serializable;

public class ManualScriptTaskParameter implements Serializable {

    private static final long serialVersionUID = 1L;

    private String comment;
    private String taskId;
    private MeetingScriptmanual meetingScriptmanual;


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public MeetingScriptmanual getMeetingScriptmanual() {
        return meetingScriptmanual;
    }

    public void setMeetingScriptmanual(MeetingScriptmanual meetingScriptmanual) {
        this.meetingScriptmanual = meetingScriptmanual;
    }
}
