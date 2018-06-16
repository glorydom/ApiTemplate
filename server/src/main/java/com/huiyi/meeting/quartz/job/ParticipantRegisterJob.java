package com.huiyi.meeting.quartz.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component("participantRegisterJob")
public class ParticipantRegisterJob extends QuartzJobBean {

	private static Logger LOGGER = LoggerFactory.getLogger(ParticipantRegisterJob.class); 
	

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		LOGGER.debug("quartz jobs triggered....");
		
	}

}
