package com.huiyi.meeting.controller;

import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ResultCollectors;
import com.dto.huiyi.meeting.entity.commonTaskDto.CommonTaskCompleteParameter;
import com.dto.huiyi.meeting.entity.commonTaskDto.CommonTaskStartParameter;
import com.dto.huiyi.meeting.util.Constants;
import com.dto.huiyi.meeting.util.TimeDateFormat;
import com.huiyi.meeting.dao.mapper.MeetingCommonTaskMapper;
import com.huiyi.meeting.dao.model.*;
import com.huiyi.meeting.rpc.api.MeetingCommonTaskService;
import com.zheng.common.base.BaseResult;
import com.zheng.common.util.StringUtil;
import com.zheng.common.validator.NotNullValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
    private MeetingCommonTaskService meetingCommonTaskService;

    @Autowired
    private MeetingCommonTaskMapper meetingCommonTaskMapper;

    @ApiOperation(value = "开启任务")
    @RequestMapping(value = "start", method = RequestMethod.POST)
    @ResponseBody
    /**
     * 启动通用任务，任务拥有者默认为本人，执行者如果没有值，就默认为自己。
     */
    public BaseResult startTask(@RequestBody MeetingCommonTask meetingCommonTask){

        if(meetingCommonTask == null && meetingCommonTask.getMeetingId() == null){
            return new BaseResult(Constants.ERROR_CODE, "必须有meeting_id", null);
        }

        ComplexResult result = FluentValidator.checkAll()
                .on(meetingCommonTask.getMeetingId(), new NotNullValidator("meetingId"))
                .doValidate()
                .result(ResultCollectors.toComplex());

        if (!result.isSuccess()) {
            return new BaseResult(ERROR_CODE, "meetingId is null", null);
        }


        int id = -1;
        //save one meeting
        if(meetingCommonTask != null){

            // set timestamp to retrieve it
            long timestamp = new Date().getTime();
            meetingCommonTask.setCreationtimestamp(timestamp);
            int affectCount = meetingCommonTaskService.insert(meetingCommonTask);

            if(affectCount == 1){
                MeetingCommonTaskExample example = new MeetingCommonTaskExample();
                example.createCriteria().andCreationtimestampEqualTo(timestamp);
                List<MeetingCommonTask> ms = meetingCommonTaskMapper.selectByExample(example);
                meetingCommonTask = ms.get(0);
            }
            id = meetingCommonTask.getId();
        }

        // prepare the parameters
        // date format: 2011-03-11T12:13:14
        String myID = (String) SecurityUtils.getSubject().getPrincipal();
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(Constants.COMMON_TASK_OWNER, myID);
        if(StringUtils.isBlank(meetingCommonTask.getTaskexecutors())) // taskExecutors就是 assignee, 只能是一个人
            parameters.put(Constants.COMMON_TASK_ASSIGNEE, myID);
        else
            parameters.put(Constants.COMMON_TASK_ASSIGNEE, meetingCommonTask.getTaskexecutors());

        if(meetingCommonTask.getNeedapproval().equalsIgnoreCase("YES")){
            parameters.put(Constants.COMMON_TASK_APPROVER, meetingCommonTask.getTaskapprover());
        }

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
                .on(commonTaskCompleteParameter.getTaskCompleteDto().getTaskId(), new NotNullValidator("taskId"))
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
        String taskId = commonTaskCompleteParameter.getTaskCompleteDto().getTaskId();

        Map<String, Object> p = new HashMap<>();

        Authentication.setAuthenticatedUserId(userId);//批注人的名称  一定要写，不然查看的时候不知道人物信息
        // 添加批注信息
        taskService.addComment(taskId, null, commonTaskCompleteParameter.getTaskCompleteDto().getComment());//comment为批注内容
        // 完成任务
        taskService.complete(taskId,p);//vars是一些变量

        meetingCommonTaskService.updateByPrimaryKey(meetingCommonTask);
        return new BaseResult(Constants.SUCCESS_CODE, "success", null);
    }

    @ApiOperation(value = "获取给我的通用任务")
    @RequestMapping(value = "list/assigntome", method = RequestMethod.GET)
    @ResponseBody
    public BaseResult getMyTasks(@RequestParam final String meetingkey, @RequestParam String historyIndicator){
        String userId = (String) SecurityUtils.getSubject().getPrincipal();
        if(historyIndicator.equalsIgnoreCase("YES")){

            return  new BaseResult(Constants.SUCCESS_CODE, "", null);
        }else{
            List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery()
                    .processInstanceNameLikeIgnoreCase(MeetingCommonTask.class.getSimpleName())
                    .list();
            List<MeetingCommonTask> commonTaskBussinessKeys = new ArrayList<>();
            for(ProcessInstance processInstance : processInstances){
                String bussinessKey = processInstance.getBusinessKey();
                int id = Integer.parseInt(bussinessKey.split("_")[1]);
                MeetingCommonTask meetingCommonTask = meetingCommonTaskService.selectByPrimaryKey(id);
                if(meetingCommonTask.getMeetingId().equalsIgnoreCase(meetingkey)){
                    commonTaskBussinessKeys.add(meetingCommonTask);
                }
            }

            return  new BaseResult(Constants.SUCCESS_CODE, "", commonTaskBussinessKeys);
        }
    }

    @ApiOperation(value = "获取给其他人的任务")
    @RequestMapping(value = "list/assign2others", method = RequestMethod.GET)
    @ResponseBody
    public BaseResult getOtherTasks(){

        return  new BaseResult(Constants.SUCCESS_CODE, "", null);
    }

    @ApiOperation(value = "获取我能看到的任务")
    @RequestMapping(value = "list/icanview", method = RequestMethod.GET)
    @ResponseBody
    public BaseResult getViewTasks(){

        return  new BaseResult(Constants.SUCCESS_CODE, "", null);
    }


}
