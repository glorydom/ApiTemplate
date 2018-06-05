package com.huiyi.meeting.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ResultCollectors;
import com.dto.huiyi.meeting.entity.CHQSResult;
import com.dto.huiyi.meeting.entity.ManualScriptDto.ManualScriptTaskParameter;
import com.dto.huiyi.meeting.entity.TaskCompleteDto;
import com.dto.huiyi.meeting.util.Constants;
import com.huiyi.meeting.dao.mapper.MeetingScriptmanualMapper;
import com.huiyi.meeting.dao.model.MeetingScriptmanual;
import com.huiyi.meeting.dao.model.MeetingScriptmanualExample;
import com.huiyi.meeting.rpc.api.MeetingScriptmanualService;
import com.huiyi.service.HttpClientService;
import com.zheng.common.base.BaseResult;
import com.zheng.common.validator.NotNullValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.identity.Authentication;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dto.huiyi.meeting.util.Constants.ERROR_CODE;
import static com.dto.huiyi.meeting.util.Constants.SUCCESS_CODE;


@Controller
@RequestMapping("/chqs/MeetingScriptmanual")
@Api(value = "会议演讲稿管理", description = "对会议演讲稿进行创建，查询，删除等操作")
@Transactional
public class MeetingScriptmanualController {

    private final String chqsUrlbase = Constants.CHQSURL + "task";

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private MeetingScriptmanualService meetingScriptmanualService;

    @Autowired
    private MeetingScriptmanualMapper meetingScriptmanualMapper;


    @ApiOperation(value = "启动创建一个新的演讲稿的流程")
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult createScriptManual(@RequestBody MeetingScriptmanual meetingScriptmanual) {

        //insert into database
        long timestamp = new Date().getTime();
        meetingScriptmanual.setCreationtimestamp(timestamp);
        meetingScriptmanualService.insert(meetingScriptmanual);
        MeetingScriptmanualExample example = new MeetingScriptmanualExample();
        example.createCriteria().andCreationtimestampEqualTo(timestamp);
        List<MeetingScriptmanual> ms = meetingScriptmanualMapper.selectByExample(example);
        meetingScriptmanual = ms.get(0);

        //process related parameters
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("entity", meetingScriptmanual);
        return ControllerUtil.startNewBussinessProcess(runtimeService, meetingScriptmanual, meetingScriptmanual.getId(), parameters);
    }

    @ApiOperation(value = "执行有关演讲稿的任务")
    @RequestMapping(value = "complete", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult executeScriptManual(@RequestBody ManualScriptTaskParameter parameter) {
        ComplexResult result = FluentValidator.checkAll()
                                .on(parameter.getTaskId(), new NotNullValidator("taskId"))
                                .doValidate()
                                .result(ResultCollectors.toComplex());

        if (!result.isSuccess()) {
            return new BaseResult(ERROR_CODE, "taskId is null", null);
        }

        // save Scriptmanual task
        MeetingScriptmanual scriptmanual = parameter.getMeetingScriptmanual();
        if(scriptmanual == null || scriptmanual.getId() == null){
            return new BaseResult(ERROR_CODE, "nothing is done, you should not close this task", null);
        }

        String userId = (String) SecurityUtils.getSubject().getPrincipal();
        String taskId = parameter.getTaskId();

        Map<String, Object> p = new HashMap<>();

        Authentication.setAuthenticatedUserId(userId);//批注人的名称  一定要写，不然查看的时候不知道人物信息
        // 添加批注信息
        taskService.addComment(taskId, null, parameter.getComment());//comment为批注内容
        // 完成任务
        taskService.complete(taskId,p);//vars是一些变量

        meetingScriptmanualService.updateByPrimaryKey(scriptmanual);
        return new BaseResult(Constants.SUCCESS_CODE, "success", null);

    }

    @ApiOperation(value = "根据ID获取演讲稿任务")
    @RequestMapping(value = "get/{bussinessKey}", method = RequestMethod.GET)
    @ResponseBody
    public BaseResult getScriptManual(@PathVariable String bussinessKey) {
        // bussinessKey:  MeetingScriptmanual.2
        if(null == bussinessKey){
            return new BaseResult(Constants.ERROR_CODE, "parameter is not correct, should be like: MeetingScriptmanual_2", null);
        }

        int id = 0;
        try {
            String ids = bussinessKey.split("_")[1];
            id = Integer.parseInt(ids);
        }catch (Exception e){
            return new BaseResult(Constants.ERROR_CODE, "parameter is not correct, should be like: MeetingScriptmanual_2", null);
        }

        MeetingScriptmanual scriptmanual = meetingScriptmanualService.selectByPrimaryKey(id);

        return new BaseResult(Constants.SUCCESS_CODE, "", scriptmanual);
    }

    @ApiOperation(value = "根据会议ID列出所有的演讲稿")
    @RequestMapping(value = "list/{meetingKey}", method = RequestMethod.GET)
    @ResponseBody
    public BaseResult listScriptManual(@PathVariable String meetingKey) {
        // bussinessKey:  MeetingScriptmanual.2
        if(null == meetingKey){
            return new BaseResult(Constants.ERROR_CODE, "parameter is not correct, should be like: MeetingScriptmanual_2", null);
        }
        List<MeetingScriptmanual> ms = null;

        int meetingId = 0;
        try {
            String ids = meetingKey.split("_")[1];
            meetingId = Integer.parseInt(ids);

            MeetingScriptmanualExample example = new MeetingScriptmanualExample();
            example.createCriteria().andMeetingidEqualTo(meetingId);
            ms = meetingScriptmanualMapper.selectByExample(example);
        }catch (Exception e){
            return new BaseResult(Constants.ERROR_CODE, "parameter is not correct, should be like: MeetingScriptmanual_2", null);
        }

        return new BaseResult(Constants.SUCCESS_CODE, "", ms);
    }



}
