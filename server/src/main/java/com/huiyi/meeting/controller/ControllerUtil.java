package com.huiyi.meeting.controller;

import com.dto.huiyi.meeting.util.Constants;
import com.zheng.common.base.BaseResult;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import java.util.Map;
import static com.dto.huiyi.meeting.util.Constants.SUCCESS_CODE;

public class ControllerUtil {

    public static BaseResult startNewBussinessProcess(RuntimeService runtimeService, Object entity, int bussinessId, Map<String, Object> parameters
                                                      ){
        String process_id = entity.getClass().getSimpleName();
        String bussiness_key = process_id + "_" + bussinessId;
        // prepare the parameters
        // date format: 2011-03-11T12:13:14

        if(null != parameters){
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(process_id,
                    bussiness_key, parameters);
            return new BaseResult(Constants.SUCCESS_CODE, "success", null);
        }else {
            return new BaseResult(Constants.ERROR_CODE, "no parameters", parameters);
        }

    }


    public static BaseResult completeTask(){
        String completeUrl = Constants.CHQSURL + "task/complete";

        return new BaseResult(SUCCESS_CODE, "success", null);
    }

    public static int getBussinessKeyByTaskId(TaskService taskService, RuntimeService runtimeService, String taskId){
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();

        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(task.getProcessInstanceId())
                .singleResult();

        String bussinessKey = processInstance.getBusinessKey();

        return Integer.parseInt(bussinessKey.split("_")[1]);
    }
}
