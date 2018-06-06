package com.dto.huiyi.meeting.entity.config;

import com.huiyi.meeting.dao.model.CustomMeetingTask;

import java.io.Serializable;
import java.util.List;

public class TaskAssigneeDto implements Serializable {

    private static final long serialVersionUID = 478113514195354529L;

    private int meetingId;
    private String meetingsubject;
    private List<TaskAssigneeSingleDto> taskSettings;

    public int getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(int meetingId) {
        this.meetingId = meetingId;
    }

    public String getMeetingsubject() {
        return meetingsubject;
    }

    public void setMeetingsubject(String meetingsubject) {
        this.meetingsubject = meetingsubject;
    }

    public List<TaskAssigneeSingleDto> getTaskSettings() {
        return taskSettings;
    }

    public void setTaskSettings(List<TaskAssigneeSingleDto> taskSettings) {
        this.taskSettings = taskSettings;
    }
}
