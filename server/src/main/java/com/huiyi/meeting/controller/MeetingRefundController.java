package com.huiyi.meeting.controller;

import com.dto.huiyi.meeting.util.Constants;
import com.huiyi.meeting.dao.model.MeetingParticipant;
import com.huiyi.meeting.dao.model.MeetingRefund;
import com.huiyi.meeting.dao.model.MeetingRefundExample;
import com.huiyi.meeting.rpc.api.MeetingParticipantService;
import com.huiyi.meeting.rpc.api.MeetingRefundService;
import com.zheng.common.base.BaseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/chqs/refund")
@Api(value = "退款", description = "嘉宾退款的流程")
public class MeetingRefundController extends BaseController {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private MeetingRefundService meetingRefundService;

    @Autowired
    private MeetingParticipantService meetingParticipantService;

    private final String[] STATUS = {"申请中", "审核中", "完成"};

    @RequestMapping(value = "apply", method = RequestMethod.POST)
    @ApiOperation(value = "销售人员申请退款")
    @ResponseBody
    public BaseResult apply(@RequestBody MeetingRefund meetingRefund){
        int participantId = meetingRefund.getParticipantid();
        MeetingParticipant meetingParticipant = meetingParticipantService.selectByPrimaryKey(participantId);
        if( participantId <= 0 || meetingParticipant == null){
            return new BaseResult(Constants.ERROR_CODE, "participant id is not valid", null);
        }

        MeetingRefundExample meetingRefundExample = new MeetingRefundExample();
        meetingRefundExample.createCriteria().andParticipantidEqualTo(participantId);
        List<MeetingRefund> meetingRefundList = meetingRefundService.selectByExample(meetingRefundExample);
        if(null != meetingRefundList && meetingRefundList.size()>0){
            return new BaseResult(Constants.ERROR_CODE, "该账户已经申请过退款了，不要再次申请", null);
        }

        Date now = new Date();
        long currentTime = now.getTime();
        meetingRefund.setCreationtimestamp(currentTime);
        //保存该记录
        meetingRefundService.insert(meetingRefund);
        MeetingRefundExample meetingRefundExample2 = new MeetingRefundExample();
        meetingRefundExample.createCriteria().andCreationtimestampEqualTo(currentTime);
        List<MeetingRefund> meetingRefundList2 = meetingRefundService.selectByExample(meetingRefundExample2);
        if(null == meetingRefundList2 || meetingRefundList2.size() == 0){
            return new BaseResult(Constants.ERROR_CODE, "system error", null);
        }

        MeetingRefund newMeetingRefund = meetingRefundList2.get(0); //应该仅仅只有一个

        Map<String, Object> parameters = new HashMap<>();  //空的流程启动参数

        return ControllerUtil.startNewBussinessProcess(runtimeService, newMeetingRefund, newMeetingRefund.getId(), parameters);
    }


    @RequestMapping(value = "audit/{taskId}", method = RequestMethod.GET)
    @ApiOperation(value = "财务人员审核")
    @ResponseBody
    public BaseResult audit(@PathVariable String taskId){
        try {
            int meetingRefundId = ControllerUtil.getBussinessKeyByTaskId(taskService,runtimeService,taskId);
            MeetingRefund meetingRefund = meetingRefundService.selectByPrimaryKey(meetingRefundId);

            meetingRefund.setStatus(STATUS[1]);  //审核中

            return new BaseResult(Constants.SUCCESS_CODE, "view ", meetingRefund);

        }catch (Exception e){
            return new BaseResult(Constants.ERROR_CODE, "taskId is not valid", null);
        }

    }

    @RequestMapping(value = "confirm/{taskId}", method = RequestMethod.GET)
    @ApiOperation(value = "财务人员确认")
    @ResponseBody
    public BaseResult confirm(@PathVariable String taskId){
        try {
            int meetingRefundId = ControllerUtil.getBussinessKeyByTaskId(taskService,runtimeService,taskId);
            MeetingRefund meetingRefund = meetingRefundService.selectByPrimaryKey(meetingRefundId);

            meetingRefund.setStatus(STATUS[2]);  //审核中
            taskService.complete(taskId);
            return new BaseResult(Constants.SUCCESS_CODE, "view ", meetingRefund);

        }catch (Exception e){
            return new BaseResult(Constants.ERROR_CODE, "taskId is not valid", null);
        }

    }

}
