package com.dto.huiyi.meeting.entity.chqs;

import java.io.Serializable;
import java.util.Date;


/**
 * 代表一个任务的实体，主要用于查询
 */
public class TaskDto implements Serializable {

    private static final long serialVersionUID = 1L;
    private String taskId;
    private String name;
    private String description;
    private String owner;
    private String assigne;
    private Date dueDate;
    private String formKey;
    private String businessKey;
    private String meetingsubject;
    private int meetingId;

    public String getMeetingsubject() {
        return meetingsubject;
    }

    public void setMeetingsubject(String meetingsubject) {
        this.meetingsubject = meetingsubject;
    }

    public int getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(int meetingId) {
        this.meetingId = meetingId;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getAssigne() {
        return assigne;
    }

    public void setAssigne(String assigne) {
        this.assigne = assigne;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getFormKey() {
        return formKey;
    }

    public void setFormKey(String formKey) {
        this.formKey = formKey;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public TaskDto() {
    }

    @Override
    public String toString() {
        return "TaskDto{" +
                "taskId='" + taskId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", owner='" + owner + '\'' +
                ", assigne='" + assigne + '\'' +
                ", dueDate=" + dueDate +
                ", formKey='" + formKey + '\'' +
                ", bussinessKey='" + businessKey + '\'' +
                '}';
    }
}
