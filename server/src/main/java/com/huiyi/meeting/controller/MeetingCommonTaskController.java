package com.huiyi.meeting.controller;

import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ResultCollectors;
import com.dto.huiyi.meeting.entity.commonTaskDto.CommonTaskCompleteParameter;
import com.dto.huiyi.meeting.util.Constants;
import com.huiyi.meeting.dao.mapper.MeetingCommonTaskMapper;
import com.huiyi.meeting.dao.model.*;
import com.huiyi.meeting.rpc.api.MeetingCommonTaskService;
import com.zheng.common.base.BaseResult;
import com.zheng.common.validator.NotNullValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.dto.huiyi.meeting.util.Constants.ERROR_CODE;

@Controller
@RequestMapping("/chqs/commonTask")
@Api(value = "通用任务管理", description = "对通用任务的处理： 启动，关闭，执行历史查询")
public class MeetingCommonTaskController extends BaseController {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private MeetingCommonTaskService meetingCommonTaskService;

    @Autowired
    private MeetingCommonTaskMapper meetingCommonTaskMapper;

    @ApiOperation(value = "开启任务")
    @RequestMapping(value = "start", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    /**
     * 启动通用任务，任务拥有者默认为本人，执行者如果没有值，就默认为自己。
     */
    public BaseResult startTask(@RequestBody MeetingCommonTask meetingCommonTask){

        int id = -1;
        //save one meeting
        if(meetingCommonTask != null){

            // set timestamp to retrieve it
            long timestamp = new Date().getTime();
            meetingCommonTask.setCreationtimestamp(timestamp);
            int affectCount = meetingCommonTaskService.insert(meetingCommonTask);

            if(affectCount == 0){
               return new BaseResult(Constants.ERROR_CODE, "failed to start common job", meetingCommonTask);
            }

            MeetingCommonTaskExample example = new MeetingCommonTaskExample();
            example.createCriteria().andCreationtimestampEqualTo(timestamp);
            List<MeetingCommonTask> meetingCommonTasks =  meetingCommonTaskService.selectByExample(example);
            MeetingCommonTask meetingCommonTask1 = meetingCommonTasks.get(0);
            id = meetingCommonTask1.getId();
        }

        // prepare the parameters
        // date format: 2011-03-11T12:13:14
        String myID = (String) SecurityUtils.getSubject().getPrincipal();
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(Constants.COMMON_TASK_OWNER, myID);
        if(StringUtils.isBlank(meetingCommonTask.getTaskexecutors())){// taskExecutors就是 assignee, 只能是一个人
            parameters.put(Constants.COMMON_TASK_ASSIGNEE, myID);
        }
        else {

            parameters.put(Constants.COMMON_TASK_ASSIGNEE, meetingCommonTask.getTaskexecutors());
        }

        if(meetingCommonTask.getNeedapproval().equalsIgnoreCase("YES")){
            parameters.put(Constants.COMMON_TASK_APPROVER, meetingCommonTask.getTaskapprover());
        }

        parameters.put(Constants.COMMON_TASK_NEED_APPROVAL, Constants.COMMON_TASK_NEED_APPROVAL_NEGATIVE); //默认不需要审批

        return ControllerUtil.startNewBussinessProcess(runtimeService, meetingCommonTask, id, parameters);
    }

    @ApiOperation(value = "通用任务完成")
    @RequestMapping(value = "complete", method = RequestMethod.POST)
    @ResponseBody
    /**
     * 该方法不同于普通的任务完成，该方法要主要检测 完成者是否是任务的拥有者，如果是就结束该task,如果不是就要将任务从新分给任务拥有者
     */
    public BaseResult partialCompleteTask(@RequestBody CommonTaskCompleteParameter commonTaskCompleteParameter){
        ComplexResult result = FluentValidator.checkAll()
                .on(commonTaskCompleteParameter.getTaskId(), new NotNullValidator("taskId"))
                .doValidate()
                .result(ResultCollectors.toComplex());

        if (!result.isSuccess()) {
            return new BaseResult(ERROR_CODE, "taskId is null", null);
        }

        MeetingCommonTask meetingCommonTask = commonTaskCompleteParameter.getMeetingCommonTask();

        if(meetingCommonTask == null || meetingCommonTask.getId() == null){
            return new BaseResult(ERROR_CODE, "nothing is done, you should not close this task", null);
        }

        String userId = (String) SecurityUtils.getSubject().getPrincipal();
        String taskId = commonTaskCompleteParameter.getTaskId();

        Map<String, Object> p = new HashMap<>();

        Authentication.setAuthenticatedUserId(userId);//批注人的名称  一定要写，不然查看的时候不知道人物信息
        // 添加批注信息
        taskService.addComment(taskId, null, commonTaskCompleteParameter.getComment());//comment为批注内容

        //设置execution parameters
        Task task = taskService.createTaskQuery().taskId(commonTaskCompleteParameter.getTaskId()).singleResult();
        taskService.setVariable(taskId, Constants.COMMON_TASK_OWNER, commonTaskCompleteParameter.getMeetingCommonTask().getTaskowner());
        taskService.setVariable(taskId, Constants.COMMON_TASK_ASSIGNEE, commonTaskCompleteParameter.getMeetingCommonTask().getTaskexecutors());
        if(commonTaskCompleteParameter.getMeetingCommonTask().getNeedapproval().equalsIgnoreCase("YES")){
            taskService.setVariable(taskId, Constants.COMMON_TASK_APPROVER, commonTaskCompleteParameter.getMeetingCommonTask().getTaskapprover());
            taskService.setVariable(taskId, Constants.COMMON_TASK_NEED_APPROVAL, Constants.COMMON_TASK_NEED_APPROVAL_POSITIVE);
        }else{
            taskService.setVariable(taskId, Constants.COMMON_TASK_NEED_APPROVAL, Constants.COMMON_TASK_NEED_APPROVAL_NEGATIVE); //不需要审核
        }
        taskService.setVariable(taskId, Constants.COMMON_TASK_VIEWER, commonTaskCompleteParameter.getMeetingCommonTask().getTaskviewers());
        // 完成任务
        taskService.complete(taskId,p);//vars是一些变量

        meetingCommonTaskService.updateByPrimaryKey(meetingCommonTask);
        return new BaseResult(Constants.SUCCESS_CODE, "success", null);
    }

    @ApiOperation(value = "通用任务审核")
    @RequestMapping(value = "audit", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public BaseResult audit(@RequestBody CommonTaskCompleteParameter commonTaskCompleteParameter){
        ComplexResult result = FluentValidator.checkAll()
                .on(commonTaskCompleteParameter.getTaskId(), new NotNullValidator("taskId"))
                .doValidate()
                .result(ResultCollectors.toComplex());

        if (!result.isSuccess()) {
            return new BaseResult(ERROR_CODE, "taskId is null", null);
        }

        MeetingCommonTask meetingCommonTask = commonTaskCompleteParameter.getMeetingCommonTask();

        if(meetingCommonTask == null || meetingCommonTask.getId() == null){
            return new BaseResult(ERROR_CODE, "nothing is done, you should not close this task", null);
        }

        String userId = (String) SecurityUtils.getSubject().getPrincipal();
        String taskId = commonTaskCompleteParameter.getTaskId();

        Map<String, Object> p = new HashMap<>();

        Authentication.setAuthenticatedUserId(userId);//批注人的名称  一定要写，不然查看的时候不知道人物信息
        // 添加批注信息
        taskService.addComment(taskId, null, commonTaskCompleteParameter.getComment());//comment为批注内容

        //设置execution parameters
//        Task task = taskService.createTaskQuery().taskId(commonTaskCompleteParameter.getTaskId()).singleResult();
        if(commonTaskCompleteParameter.isAuditResult()){
            taskService.setVariable(taskId, Constants.COMMON_TASK_AUDIT_RESULT, Constants.COMMON_TASK_AUDIT_RESULT_PASS);
        } else {
            taskService.setVariable(taskId, Constants.COMMON_TASK_AUDIT_RESULT, Constants.COMMON_TASK_AUDIT_RESULT_FAIL);
        }

        taskService.complete(taskId,p);//vars是一些变量

        meetingCommonTaskService.updateByPrimaryKey(meetingCommonTask);
        return new BaseResult(Constants.SUCCESS_CODE, "success", null);
    }

    @ApiOperation(value = "获取给我的通用任务")
    @RequestMapping(value = "list/assigntome", method = RequestMethod.GET)
    @ResponseBody
    public BaseResult getMyTasks(@RequestParam final String meetingId){

        String userId = (String) SecurityUtils.getSubject().getPrincipal();

        MeetingCommonTaskExample example = new MeetingCommonTaskExample();
        example.createCriteria().andMeetingidEqualTo(Integer.parseInt(meetingId));
        List<MeetingCommonTask> meetingCommonTasks = meetingCommonTaskService.selectByExample(example);
        List<MeetingCommonTask> result = new ArrayList<MeetingCommonTask>();
        // get the task list whose assignee or candidate is me
        for(MeetingCommonTask task : meetingCommonTasks){
            String candidates = task.getTaskexecutors();
            List<String> candidateList = Arrays.asList(candidates.split(","));
            if(candidateList.contains(userId)){
                result.add(task);
            }
        }
        return  new BaseResult(Constants.SUCCESS_CODE, "success", result);
    }

    @ApiOperation(value = "获取给其他人的任务")
    @RequestMapping(value = "list/assign2others", method = RequestMethod.GET)
    @ResponseBody
    public BaseResult getOtherTasks(@RequestParam final String meetingId){
        String userId = (String) SecurityUtils.getSubject().getPrincipal();

        MeetingCommonTaskExample example = new MeetingCommonTaskExample();
        example.createCriteria().andMeetingidEqualTo(Integer.parseInt(meetingId)).andTaskownerEqualTo(userId);
        List<MeetingCommonTask> meetingCommonTasks = meetingCommonTaskService.selectByExample(example);
        return  new BaseResult(Constants.SUCCESS_CODE, "", meetingCommonTasks);
    }

    @ApiOperation(value = "获取我能看到的任务")
    @RequestMapping(value = "list/icanview", method = RequestMethod.GET)
    @ResponseBody
    public BaseResult getViewTasks(@RequestParam final String meetingId){

        String userId = (String) SecurityUtils.getSubject().getPrincipal();

        MeetingCommonTaskExample example = new MeetingCommonTaskExample();
        example.createCriteria().andMeetingidEqualTo(Integer.parseInt(meetingId));
        List<MeetingCommonTask> meetingCommonTasks = meetingCommonTaskService.selectByExample(example);
        List<MeetingCommonTask> result = new ArrayList<MeetingCommonTask>();
        // I can view
        for(MeetingCommonTask task : meetingCommonTasks){
            String viewers = task.getTaskviewers();
            List<String> viewList = Arrays.asList(viewers.split(","));
            if(viewList.contains(userId)){
                result.add(task);
            }
        }
        return  new BaseResult(Constants.SUCCESS_CODE, "", result);
    }


    @ApiOperation(value = "获取任务详情")
    @RequestMapping(value = "detail/{commonTaskId}", method = RequestMethod.GET)
    @ResponseBody
    public BaseResult getDetails(@RequestParam final String commonTaskId){

        MeetingCommonTask task = meetingCommonTaskService.selectByPrimaryKey(Integer.parseInt(commonTaskId));

        return  new BaseResult(Constants.SUCCESS_CODE, "got one commont task", task);
    }


}
