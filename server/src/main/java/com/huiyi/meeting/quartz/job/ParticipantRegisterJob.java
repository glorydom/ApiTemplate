package com.huiyi.meeting.quartz.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.huiyi.meeting.dao.model.MeetingParticipant;
import com.huiyi.meeting.dao.model.MeetingParticipantExample;
import com.huiyi.meeting.rpc.api.MeetingParticipantService;
import com.huiyi.workflow.service.BaseWorkFlowService;

public class ParticipantRegisterJob extends QuartzJobBean{

	private static Logger LOGGER = LoggerFactory.getLogger(ParticipantRegisterJob.class); 
	
	@Autowired
	private JdbcTemplate externalJdbcTemplate;
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
		List<MeetingParticipant> mpList = loadFromExternal();
		for(MeetingParticipant mp : mpList) {
			if(mp.getId() == null) { //新的参会人员，需要存入DB
				mp.setCreationtimestamp(currenttime);
				meetingParticipantService.insert(mp);
				MeetingParticipantExample example = new MeetingParticipantExample();
				//手机号+人名作为唯一标识
				example.createCriteria().andTelephoneEqualTo(mp.getTelephone()).andNameEqualTo(mp.getName()).andCreationtimestampEqualTo(currenttime);
				List<MeetingParticipant> list = meetingParticipantService.selectByExample(example);
				if(list != null && list.size()>0) {
					mp = list.get(0);
				}
			}
			LOGGER.info("准备启动参会人员(id:"+mp.getId()+")工作流");
			baseWorkFlowService.startRegisterProcess(mp.getId());
		}
	}
	
	private List<MeetingParticipant> loadFromExternal() {
		LOGGER.debug(externalJdbcTemplate.getDataSource().toString());
		String sql = "select * from Exteral_testing where create_time>? ";
		List<Map<String,Object>> list = externalJdbcTemplate.queryForList(sql, "20180601");
		List<MeetingParticipant> mpList = new ArrayList<>();
		for(Map<String,Object> map : list) {
			MeetingParticipant mp = new MeetingParticipant();
			mp.setCompany(map.get("company")+"");
			mp.setTelephone(map.get("telephone")+"");
			mp.setName(map.get("name")+"");
			mp.setProductofinterest("大豆");
			MeetingParticipantExample example = new MeetingParticipantExample();
			//手机号+人名作为唯一标识
			example.createCriteria().andTelephoneEqualTo(mp.getTelephone()).andNameEqualTo(mp.getName());
			List<MeetingParticipant> existing = meetingParticipantService.selectByExample(example);
			if(existing.size() == 0) {//新的参会人员
				mp.setReceptionstatus("未指定");
			}
			else {
				mp = existing.get(0);
				boolean processStarted = baseWorkFlowService.checkProcessInstance("MeetingRegister", MeetingParticipant.class.getSimpleName()+"_"+mp.getId());
				if(processStarted) {
					mp.setReceptionstatus("重复需跟踪确认");
					LOGGER.warn("此手机号公司已经注册过，请核对后继续");
					continue;
				}
				else {
					LOGGER.info("参与人员{}已经存入数据库，但是流程未启动",mp.getId());
				}
			}
			mpList.add(mp);
		}
		return mpList;
	}

}
