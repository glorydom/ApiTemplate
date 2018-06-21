package com.huiyi.meeting.service;

import org.activiti.engine.history.HistoricProcessInstance;

public interface CommonMeetingService {

	//根据实例对象获取business对象描述
	public String getObjectDescription(HistoricProcessInstance phi);
}
