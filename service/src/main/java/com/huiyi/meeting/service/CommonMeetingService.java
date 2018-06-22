package com.huiyi.meeting.service;

import java.util.List;

import org.activiti.engine.history.HistoricProcessInstance;

import com.huiyi.meeting.dao.model.MeetingParticipant;

public interface CommonMeetingService {

	//根据实例对象获取business对象描述
	public String getObjectDescription(HistoricProcessInstance phi);
	
	//对账
	public List<MeetingParticipant> reconsile(String filepath);
}
