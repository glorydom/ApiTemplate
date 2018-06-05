package com.huiyi.meeting.controller;

import com.alibaba.fastjson.TypeReference;
import com.dto.huiyi.meeting.entity.CHQSResult;
import com.dto.huiyi.meeting.entity.chqs.TaskDto;
import com.dto.huiyi.meeting.entity.chqs.TaskHistoryDto;
import com.dto.huiyi.meeting.util.Constants;
import com.huicong.upms.dao.model.UpmsUserExample;
import com.huicong.upms.rpc.api.UpmsUserService;
import com.huiyi.meeting.dao.model.MeetingParticipant;
import com.huiyi.service.HttpClientService;
import com.zheng.common.base.BaseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.identity.Group;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dto.huiyi.meeting.util.Constants.ERROR_CODE;
import static com.dto.huiyi.meeting.util.Constants.SUCCESS_CODE;

@Controller
@RequestMapping("/chqs/task")
@Api(value = "任务管理", description = "对每个人的任务的管理")
@Transactional
public class TaskController {

    @Autowired
    private HistoryService historyService;

    @Autowired
    private UpmsUserService upmsUserService;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;


    @ApiOperation(value = "查询本组的任务")
    @RequestMapping(value = "search/group", method = RequestMethod.GET)
    @ResponseBody
    public BaseResult searchMygroupTask() {
        String username = (String) SecurityUtils.getSubject().getPrincipal();

        String sql = "select g.* FROM\n" +
                "ACT_ID_GROUP g join ACT_ID_MEMBERSHIP m\n" +
                "on g.ID_ = m.GROUP_ID_\n" +
                "join ACT_ID_USER u\n" +
                "on m.USER_ID_ = u.ID_\n" +
                "where u.ID_= '" + username + "'";
        List<Group> groups  = identityService.createNativeGroupQuery().sql(sql).list();
        List<Task> tasks = new ArrayList<>();
        for(Group g:groups){
            List<Task> groupedTasks = taskService.createTaskQuery().taskCandidateGroup(g.getId()).list();
            tasks.addAll(groupedTasks);
        }

        List<TaskDto> tds = new ArrayList<>();
        for (Task task : tasks) {
            tds.add(convertToTaskDto(task));
        }

        return new BaseResult(SUCCESS_CODE, "success", tds);

    }

    @ApiOperation(value = "将某个任务分给自己，并开始执行")
    @RequestMapping(value = "claim/{taskId}", method = RequestMethod.GET)
    @ResponseBody
    public BaseResult claimTask(@PathVariable String taskId) {
        String myID = (String) SecurityUtils.getSubject().getPrincipal();
        CHQSResult result = null;
        Map<String, String> param = new HashMap<String, String>();

        taskService.claim(taskId, myID);

        return new BaseResult(Constants.SUCCESS_CODE, "success", null);
    }

    @ApiOperation(value = "查询本人的任务")
    @RequestMapping(value = "search/mine", method = RequestMethod.GET)
    @ResponseBody
    public BaseResult searchMyTask() {
        String myID = (String) SecurityUtils.getSubject().getPrincipal();


        List<Task> candidateOrAssignedTasks = taskService.createTaskQuery().taskCandidateOrAssigned(myID).list();
        // 分给我我组的任务

        List<TaskDto> tds = new ArrayList<>();
        for (Task task : candidateOrAssignedTasks) {
            tds.add(convertToTaskDto(task));
        }
        return new BaseResult(Constants.SUCCESS_CODE, "success", tds);
    }


    @ApiOperation(value = "查询任务历史的批注信息")
    @RequestMapping(value = "comment/history/{taskId}", method = RequestMethod.GET)
    @ResponseBody
    public BaseResult listComment(@PathVariable String taskId) {
        CHQSResult<List<TaskHistoryDto>> result = null;
        Map<String, String> param = new HashMap<String, String>();


        List<Comment> list = new ArrayList<>();
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();

        String processInstanceId = task.getProcessInstanceId();

        List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .list();

        if(historicTaskInstances !=null && historicTaskInstances.size()>0){
            for(HistoricTaskInstance historicTaskInstance : historicTaskInstances){
                String taskid = historicTaskInstance.getId();
                List<Comment> taskList = taskService.getTaskComments(taskid);
                list.addAll(taskList);
            }
        }

        return new BaseResult(Constants.SUCCESS_CODE, "", list);
    }


    private String getBusinessObjId(String taskId) {
        //1  获取任务对象
        Task task  =  taskService.createTaskQuery().taskId(taskId).singleResult();

        //2  通过任务对象获取流程实例
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
        //3 通过流程实例获取“业务键”
        String businessKey = pi.getBusinessKey();
        //4 拆分业务键，拆分成“业务对象名称”和“业务对象ID”的数组
        // a=b  LeaveBill.1
//        String objId = null;
//        if(StringUtils.isNotBlank(businessKey)){
//            objId = businessKey.split("\\.")[1];
//        }
        return businessKey;
    }

    private TaskDto convertToTaskDto(Task task){
        if(task == null)
            return null;

        String processInstanceId = task.getProcessInstanceId();
        //使用流程实例ID，查询历史任务，获取历史任务对应的每个任务ID
        List<HistoricTaskInstance> htiList = historyService.createHistoricTaskInstanceQuery()//历史任务表查询
                .processInstanceId(processInstanceId)//使用流程实例ID查询
                .list();

        TaskDto taskDto = new TaskDto();
        taskDto.setAssigne(task.getAssignee());
        taskDto.setDescription(task.getDescription());
        taskDto.setDueDate(task.getDueDate());
        taskDto.setName(task.getName());
        taskDto.setFormKey(task.getFormKey());
        taskDto.setOwner(task.getOwner());
        taskDto.setTaskId(task.getId());
        taskDto.setBussinessKey(getBusinessObjId(task.getId()));

        return taskDto;
    }
}
