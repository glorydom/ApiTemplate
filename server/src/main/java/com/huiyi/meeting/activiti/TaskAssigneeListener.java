package com.huiyi.meeting.activiti;

import com.huiyi.service.TaskAssigneeService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

import java.io.Serializable;
import java.util.List;

public class TaskAssigneeListener implements TaskListener, Serializable {

    private static final long serialVersionUID = 478113514195354529L;

    private TaskAssigneeService taskAssigneeService;

    public void notify(DelegateTask task) {
        try {
            String taskId = task.getId();

            ApplicationContext context = ContextLoader
                    .getCurrentWebApplicationContext();
            taskAssigneeService = (TaskAssigneeService) context
                    .getBean("TaskAssigneeService");
            List<String> users = taskAssigneeService.getUserIdsByTaskID(taskId);
            for(String userId: users){
                task.addCandidateUser(userId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
