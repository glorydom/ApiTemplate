package com.huiyi.meeting.service;

import java.util.List;

import org.activiti.engine.history.HistoricProcessInstance;

import com.dto.huiyi.meeting.entity.register.ComparisonResultDto;

public interface CommonMeetingService {

	//根据实例对象获取business对象描述
	public String getObjectDescription(HistoricProcessInstance phi);
	
	//对账
	public List<ComparisonResultDto> reconsile(String filepath);
}
