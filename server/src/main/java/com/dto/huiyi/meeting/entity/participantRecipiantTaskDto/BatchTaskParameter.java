package com.dto.huiyi.meeting.entity.participantRecipiantTaskDto;

import com.huiyi.meeting.dao.model.MeetingParticipantRecipiantBatchTask;
import com.huiyi.meeting.dao.model.MeetingParticipantRecipiantTask;

import java.io.Serializable;
import java.util.List;

public class BatchTaskParameter implements Serializable {

    private static final long serialVersionUID = 1L;

    private MeetingParticipantRecipiantBatchTask meetingParticipantRecipiantBatchTask;
    private List<MeetingParticipantRecipiantTask> taskItem;


    public MeetingParticipantRecipiantBatchTask getMeetingParticipantRecipiantBatchTask() {
        return meetingParticipantRecipiantBatchTask;
    }

    public void setMeetingParticipantRecipiantBatchTask(MeetingParticipantRecipiantBatchTask meetingParticipantRecipiantBatchTask) {
        this.meetingParticipantRecipiantBatchTask = meetingParticipantRecipiantBatchTask;
    }

    public List<MeetingParticipantRecipiantTask> getTaskItem() {
        return taskItem;
    }

    public void setTaskItem(List<MeetingParticipantRecipiantTask> taskItem) {
        this.taskItem = taskItem;
    }
}
