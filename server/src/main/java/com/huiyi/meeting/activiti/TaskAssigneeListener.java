package com.huiyi.meeting.activiti;

import java.io.Serializable;
import java.util.List;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

import com.huiyi.service.TaskAssigneeService;

public class TaskAssigneeListener implements TaskListener, Serializable {

	private static Logger LOGGER = LoggerFactory.getLogger(TaskAssigneeListener.class);
	
    private static final long serialVersionUID = 478113514195354529L;

    private TaskAssigneeService taskAssigneeService;

    public void notify(DelegateTask task) {
        try {
            ApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
            taskAssigneeService = (TaskAssigneeService) context.getBean("TaskAssigneeService");
            String processName = task.getExecution().getProcessDefinitionId().split(":")[0];
            List<String> users = taskAssigneeService.getUserIdsByTaskID(processName,task.getTaskDefinitionKey());
            if(users == null)
            	return;
            LOGGER.info(task.getName() +"--"+ task.getTaskDefinitionKey() +"--->:" + users.toString());
            for(String userId: users){
                task.addCandidateUser(userId);
            }
        } catch (Exception e) {
        	LOGGER.error(e.getMessage());
            e.printStackTrace();
        }
    }

}
