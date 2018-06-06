package com.huiyi.meeting.controller;

import com.dto.huiyi.meeting.entity.participantRecipiantTaskDto.BatchTaskParameter;
import com.dto.huiyi.meeting.util.Constants;
import com.zheng.common.base.BaseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/chqs/participantArrangeBatchTask")
@Api(value = "嘉宾安排", description = "嘉宾安排主要包括：接机，安排酒店，安排会议室")
public class MeetingParticipantRecipiantBatchTaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @ApiOperation(value = "启动人员安排的任务流程")
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult createBatchTask(@RequestBody BatchTaskParameter batchTaskParameter) {
        // 将他们组装起来 保存到数据库
        return new BaseResult(Constants.SUCCESS_CODE, "", null);
    }

    @ApiOperation(value = "人员安排完成")
    @RequestMapping(value = "complete", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult completeBatchTask(@RequestBody BatchTaskParameter batchTaskParameter) {
        // 将他们组装起来 保存到数据库
        return new BaseResult(Constants.SUCCESS_CODE, "", null);
    }

}
