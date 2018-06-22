package com.huiyi.meeting.service.impl;

import java.util.List;

import org.activiti.engine.history.HistoricProcessInstance;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huiyi.meeting.dao.model.MeetingMeeting;
import com.huiyi.meeting.dao.model.MeetingParticipant;
import com.huiyi.meeting.rpc.api.MeetingMeetingService;
import com.huiyi.meeting.rpc.api.MeetingParticipantService;
import com.huiyi.meeting.service.CommonMeetingService;

@Service
public class CommonMeetingServiceImpl implements CommonMeetingService {

	private static Logger LOGGER = LoggerFactory.getLogger(CommonMeetingServiceImpl.class);
	
	@Autowired
	private MeetingMeetingService meetingMeetingService;
	@Autowired
	private MeetingParticipantService meetingParticipantService;
	
	
	@Override
	public String getObjectDescription(HistoricProcessInstance pi) {
		// TODO Auto-generated method stub
		String[] businessKeyArray = pi.getBusinessKey().split("_");
		if(businessKeyArray.length != 2) {
			return "无法解析businessKey或未指定"+pi.getBusinessKey();
		}
		String objectType = businessKeyArray[0];
		if(!StringUtils.isNumeric(businessKeyArray[1])) {
			return "业务对象ID异常"+businessKeyArray[1];
		}
		int objectId = Integer.parseInt(businessKeyArray[1]);
		LOGGER.debug("对象类型："+objectType +",对象ID："+objectId);
		if(objectType.equals(MeetingMeeting.class.getSimpleName())) {
			MeetingMeeting obj = meetingMeetingService.selectByPrimaryKey(objectId);
			return obj.toString();
		}
		else if(objectType.equals(MeetingParticipant.class.getSimpleName())) {
			MeetingParticipant obj = meetingParticipantService.selectByPrimaryKey(objectId);
			return obj.toString();
		}
		return "不支持的businessKey"+pi.getBusinessKey();
	}


	@Override
	public List<MeetingParticipant> reconsile(String filepath) {
		// TODO Auto-generated method stub';.
		return null;
	}

}
