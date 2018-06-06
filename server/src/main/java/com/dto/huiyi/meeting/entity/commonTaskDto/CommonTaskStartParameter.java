package com.dto.huiyi.meeting.entity.commonTaskDto;

import com.huiyi.meeting.dao.model.MeetingCommonTask;

import java.io.Serializable;

public class CommonTaskStartParameter implements Serializable {

    private static final long serialVersionUID = 1L;

    private MeetingCommonTask meetingCommonTask;
    private String delegate; // 代办人 userId
    private String owner; // userId

    public MeetingCommonTask getMeetingCommonTask() {
        return meetingCommonTask;
    }

    public void setMeetingCommonTask(MeetingCommonTask meetingCommonTask) {
        this.meetingCommonTask = meetingCommonTask;
    }

    public String getDelegate() {
        return delegate;
    }

    public void setDelegate(String delegate) {
        this.delegate = delegate;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
