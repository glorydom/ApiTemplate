package com.huiyi.service;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service("TaskAssigneeService")
public class TaskAssigneeService {

    /**
     * 根据任务id返回任务执行人
     * @param taskId
     * @return
     */
    public List<String> getUserIdsByTaskID (String taskId){
        List list =  new ArrayList();
        list.add("姚晶");
        return list;
    }

}
