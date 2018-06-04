package com.dto.huiyi.meeting.entity.commonTaskDto;

import com.dto.huiyi.meeting.entity.TaskCompleteDto;
import com.huiyi.meeting.dao.model.MeetingCommonTask;

import java.io.Serializable;

public class CommonTaskCompleteParameter implements Serializable {

    private static final long serialVersionUID = 1L;
    private TaskCompleteDto taskCompleteDto;
    private MeetingCommonTask meetingCommonTask;

    public TaskCompleteDto getTaskCompleteDto() {
        return taskCompleteDto;
    }

    public void setTaskCompleteDto(TaskCompleteDto taskCompleteDto) {
        this.taskCompleteDto = taskCompleteDto;
    }

    public MeetingCommonTask getMeetingCommonTask() {
        return meetingCommonTask;
    }

    public void setMeetingCommonTask(MeetingCommonTask meetingCommonTask) {
        this.meetingCommonTask = meetingCommonTask;
    }
}
