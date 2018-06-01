package com.huiyi.meeting.controller;

import com.huiyi.meeting.dao.model.MeetingTopic;
import com.huiyi.meeting.dao.model.MeetingTopicExample;
import com.huiyi.meeting.rpc.api.MeetingTopicService;
import com.huiyi.service.HttpClientService;
import com.zheng.common.base.BaseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.dto.huiyi.meeting.util.Constants.SUCCESS_CODE;

@Controller
@RequestMapping("/chqs/topic")
@Api(value = "专题研讨会话题管理", description = "专场论坛征询10个问题")
@Transactional
public class MeetingTopicController {

    @Autowired
    HttpClientService httpClientService;

    @Autowired
    MeetingTopicService meetingTopicService;

    @ApiOperation(value = "查询与某次会议有关的所有的研讨会话题")
    @RequestMapping(value = "list/{meetingId}", method = RequestMethod.GET)
    @ResponseBody
    public BaseResult list(@PathVariable String meetingId){

        MeetingTopicExample meetingTopicExample = new MeetingTopicExample();
        meetingTopicExample.createCriteria().andMeetingidEqualTo(Integer.parseInt(meetingId));

        List<MeetingTopic> topics = meetingTopicService.selectByExample(meetingTopicExample);

        return new BaseResult(SUCCESS_CODE, "get all topics belong to meetingID:" + meetingId, topics);
    }


    @ApiOperation(value = "创建一个新的MeetingTopic")
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult create(@RequestBody MeetingTopic meetingTopic){
//        ComplexResult result = FluentValidator.checkAll()
//                .on(meetingTopic.get, new NotNullValidator("taskId"))
//                .doValidate()
//                .result(ResultCollectors.toComplex());

        //save meetingtopic
        long timestamp = new Date().getTime();
        meetingTopic.setCreationtimestamp(timestamp);
        meetingTopicService.insert(meetingTopic);
        MeetingTopicExample example = new MeetingTopicExample();
        example.createCriteria().andCreationtimestampEqualTo(timestamp);
        List<MeetingTopic> ms = meetingTopicService.selectByExample(example);
        meetingTopic = ms.get(0);

        // start the meeting process
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("entity", meetingTopic);
        return ControllerUtil.startNewBussinessProcess(meetingTopic, meetingTopic.getId(), parameters, httpClientService);
    }

    @ApiOperation(value = "完成MeetingTopic任务")
    @RequestMapping(value = "complete", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult complete(@RequestBody MeetingTopic meetingTopic){

        return null;
    }





}
