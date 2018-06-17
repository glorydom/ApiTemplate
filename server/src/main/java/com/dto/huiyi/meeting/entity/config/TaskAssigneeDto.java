package com.dto.huiyi.meeting.entity.config;

import java.io.Serializable;
import java.util.List;

public class TaskAssigneeDto implements Serializable {

    private static final long serialVersionUID = 478113514195354529L;

//    private int meetingId;
    private String processName;
    private List<TaskAssigneeSingleDto> taskSettings;

//    public int getMeetingId() {
//        return meetingId;
//    }
//
//    public void setMeetingId(int meetingId) {
//        this.meetingId = meetingId;
//    }
//

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
}
