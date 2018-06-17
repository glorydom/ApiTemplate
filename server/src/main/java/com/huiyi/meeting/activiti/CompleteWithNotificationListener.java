package com.huiyi.meeting.activiti;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.task.IdentityLink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

import com.huiyi.workflow.service.BaseWorkFlowService;

public class CompleteWithNotificationListener implements TaskListener,Serializable {

	private static final long serialVersionUID = -78805721454981881L;
	
	private static Logger LOGGER = LoggerFactory.getLogger(CompleteWithNotificationListener.class);

	@Override
	public void notify(DelegateTask task) {
		ApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		BaseWorkFlowService baseWorkFlowService = (BaseWorkFlowService)context.getBean("baseWorkFlowService");
		Set<IdentityLink> cadidates = task.getCandidates();
		for(IdentityLink il : cadidates)
			LOGGER.debug(il.getUserId());
		Map<String,Object> variables = new HashMap<>();
		baseWorkFlowService.completeTask(task.getId(), variables);
		LOGGER.debug("");
	}

}
