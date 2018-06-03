package com.huiyi.meeting.dao.model;

import java.util.HashSet;
import java.util.Set;

import com.huicong.upms.dao.model.UpmsUser;

public class CustomMeetingTask {

	private String taskId;
	private String taskName;
	private Set<UpmsUser> userList = new HashSet<>();
	
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public Set<UpmsUser> getUserList() {
		return userList;
	}
	public void setUserList(Set<UpmsUser> userList) {
		this.userList = userList;
	}
	
}
