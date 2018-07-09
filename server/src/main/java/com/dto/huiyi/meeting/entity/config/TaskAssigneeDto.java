package com.dto.huiyi.meeting.entity.config;

import java.io.Serializable;
import java.util.List;

public class TaskAssigneeDto implements Serializable {

    private static final long serialVersionUID = 478113514195354529L;

    private Integer meetingId;
    private String processName;
    private String key;
    private List<TaskAssigneeSingleDto> taskSettings;


    public Integer getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(Integer meetingId) {
        this.meetingId = meetingId;
    }

    public List<TaskAssigneeSingleDto> getTaskSettings() {
        return taskSettings;
    }

    public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public void setTaskSettings(List<TaskAssigneeSingleDto> taskSettings) {
        this.taskSettings = taskSettings;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
