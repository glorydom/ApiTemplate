package com.dto.huiyi.meeting.entity.config;

import java.io.Serializable;
import java.util.List;

public class TaskAssigneeSingleDto implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String taskId;
	private String taskName;
	private List<Integer> userList;
	
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
	public List<Integer> getUserList() {
		return userList;
	}
	public void setUserList(List<Integer> userList) {
		this.userList = userList;
	}
	
	
}
