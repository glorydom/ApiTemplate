package com.huiyi.meeting.quartz.job;

import java.util.Date;
import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.huiyi.meeting.dao.model.MeetingParticipant;
import com.huiyi.meeting.dao.model.MeetingParticipantExample;
import com.huiyi.meeting.rpc.api.MeetingParticipantService;
import com.huiyi.workflow.service.BaseWorkFlowService;

public class ParticipantRegisterJob extends QuartzJobBean{

	private static Logger LOGGER = LoggerFactory.getLogger(ParticipantRegisterJob.class); 
	
	@Autowired
	private BaseWorkFlowService baseWorkFlowService;
	@Autowired
	private MeetingParticipantService meetingParticipantService;
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		LOGGER.debug("quartz jobs triggered...."+baseWorkFlowService);
		long currenttime = new Date().getTime();
		MeetingParticipant mp = loadFromExternal();
		if(mp == null) {
			return;
		}
		mp.setCreationtimestamp(currenttime);
		meetingParticipantService.insert(mp);
		MeetingParticipantExample example = new MeetingParticipantExample();
		//手机号+公司+人名作为唯一标识
		example.createCriteria().andTelephoneEqualTo(mp.getTelephone()).andCompanyEqualTo(mp.getCompany()).andNameEqualTo(mp.getName())
			.andCreationtimestampEqualTo(currenttime);
		List<MeetingParticipant> list = meetingParticipantService.selectByExample(example);
		if(list != null && list.size()>0) {
			int participantId = list.get(0).getId();
			LOGGER.info("准备启动参会人员(id:"+participantId+")工作流");
			baseWorkFlowService.startRegisterProcess(participantId);
		}
	}
	
	private MeetingParticipant loadFromExternal() {
		MeetingParticipant mp = new MeetingParticipant();
		mp.setGender("1");
		mp.setCompany("测试公司");
		mp.setTelephone("1345678588");
		mp.setName("自动生成用户");
		mp.setProductofinterest("大豆");
		
		MeetingParticipantExample example = new MeetingParticipantExample();
		//手机号+公司+人名作为唯一标识
		example.createCriteria().andTelephoneEqualTo(mp.getTelephone()).andCompanyEqualTo(mp.getCompany()).andNameEqualTo(mp.getName());
		int cnt = meetingParticipantService.countByExample(example);
		if(cnt ==0) {
			mp.setReceptionstatus("未指定");
		}
		else {
			mp.setReceptionstatus("重复需跟踪确认");
			LOGGER.warn("此手机号公司已经注册过，请核对后继续");
			return null;
		}
		return mp;
	}

}
