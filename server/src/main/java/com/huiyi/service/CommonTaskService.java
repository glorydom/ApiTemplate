package com.huiyi.service;

import com.dto.huiyi.meeting.util.Constants;
import com.huiyi.meeting.dao.model.MeetingCommonTask;
import org.activiti.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("CommonTaskService")
public class CommonTaskService {

    private String COMMONTASK_NAME = MeetingCommonTask.class.getSimpleName();

    @Autowired
    private RuntimeService runtimeService;

    /**
     * executionId 的样子应该是
     * @param executionId
     * @return
     */
    public String getApprover(String executionId){

        String approverId = (String) runtimeService.getVariable(executionId, Constants.COMMON_TASK_APPROVER);

        return approverId;
    }

    public String getAssignee(String executionId){

        return (String) runtimeService.getVariable(executionId, Constants.COMMON_TASK_ASSIGNEE);
    }


    public String getOwner(String executionId){

        return (String) runtimeService.getVariable(executionId, Constants.COMMON_TASK_OWNER);
    }

    public String getViewer(String executionId){

        return (String) runtimeService.getVariable(executionId, Constants.COMMON_TASK_VIEWER);
    }

}
