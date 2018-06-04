package com.dto.huiyi.meeting.entity.commonTaskDto;

import com.huiyi.meeting.dao.model.MeetingCommonTask;

import java.io.Serializable;

public class CommonTaskStartParameter implements Serializable {

    private static final long serialVersionUID = 1L;

    private MeetingCommonTask meetingCommonTask;
    private String assignee; // userId

    public MeetingCommonTask getMeetingCommonTask() {
        return meetingCommonTask;
    }

    public void setMeetingCommonTask(MeetingCommonTask meetingCommonTask) {
        this.meetingCommonTask = meetingCommonTask;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }
}
