package com.huiyi.meeting.activiti;

import com.huiyi.service.CommonTaskService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

import java.io.Serializable;

public class CommonTaskListener implements TaskListener, Serializable {

    private static final long serialVersionUID = 478113514195354529L;

    private CommonTaskService commonTaskService;

    @Override
    public void notify(DelegateTask delegateTask) {
        ApplicationContext context = ContextLoader
                .getCurrentWebApplicationContext();
        commonTaskService = (CommonTaskService) context
                .getBean("CommonTaskService");
        String assigne = "";
        if(delegateTask.getFormKey().equalsIgnoreCase("meetingCommontaskCompletion")){

            assigne = commonTaskService.getAssignee(delegateTask.getExecutionId());
        } else {
            assigne = commonTaskService.getApprover(delegateTask.getExecutionId());
        }
        delegateTask.setAssignee(assigne);
    }


}
