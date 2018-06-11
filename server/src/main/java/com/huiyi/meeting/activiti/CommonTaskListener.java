package com.huiyi.meeting.activiti;

import com.huiyi.service.CommonTaskService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.task.IdentityLink;
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
            //
            String assigneeCandidates = commonTaskService.getAssignee(delegateTask.getExecutionId());
            for(IdentityLink id: delegateTask.getCandidates()){
                //先删除之前的用户
                delegateTask.deleteCandidateUser(id.getUserId());
            }
            for(String candidate : assigneeCandidates.split(",")){
                delegateTask.addCandidateUser(candidate);
            }
        } else {
            String approverCandidates = commonTaskService.getApprover(delegateTask.getExecutionId());
            for(IdentityLink id: delegateTask.getCandidates()){
                delegateTask.deleteCandidateUser(id.getUserId());
            }
            for(String candidate : approverCandidates.split(",")){
                delegateTask.addCandidateUser(candidate);
            }
        }
    }

}
