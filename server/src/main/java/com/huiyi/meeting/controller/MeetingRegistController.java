package com.huiyi.meeting.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dto.huiyi.meeting.entity.register.ComparisonResultDto;
import com.dto.huiyi.meeting.util.Constants;
import com.huiyi.meeting.dao.model.MeetingRegist;
import com.huiyi.meeting.rpc.api.MeetingRegistService;
import com.huiyi.meeting.service.CommonMeetingService;
import com.huiyi.workflow.service.BaseWorkFlowService;
import com.zheng.common.base.BaseResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/chqs/regist")
@Api(value = "嘉宾注册", description = "对嘉宾注册相关事项的管理")
public class MeetingRegistController {

	@Autowired
	private BaseWorkFlowService baseWorkFlowService;
	@Autowired
	private MeetingRegistService meetingRegistService;
	@Autowired
	private CommonMeetingService commonMeetingService;
	
    @ApiOperation(value = "会务款项比对")
    @RequestMapping(value = "compare/{taskId}", method = RequestMethod.GET)
    @ResponseBody
    /**
     * taskID是activiti的Taskid 根据taskId可以找到businessKey 从而找到MeetingRegist 读取Excel内容 将公司信息跟注册人员付款信息比对。
     */
    public BaseResult compare(@PathVariable String taskId){
        //todo
//        MeetingParticipant participant = new MeetingParticipant();
//        participant.getMeetingfee();
//        participant.getMeetingfeepaidtime();
//        participant.getMeetingid();
//        participant.getCharged();
//        participant.getName();
//        participant.getTelephone();
        //以上字段为participant返回必须有的字段

    	int registId = baseWorkFlowService.findBusinessIdbyTaskId(taskId);
    	MeetingRegist mr = meetingRegistService.selectByPrimaryKey(registId);
        List<ComparisonResultDto> resultDtos = commonMeetingService.reconsile(mr.getFeesheetexcel());
        return new BaseResult(Constants.SUCCESS_CODE, "", resultDtos);
    }

    @ApiOperation(value = "款项确认")
    @RequestMapping(value = "confirm/{taskId}", method = RequestMethod.GET)
    @ResponseBody
    /**
     *   读取Excel内容 将公司信息跟注册人员付款信息比对。
     */
    public BaseResult confirm(@PathVariable String taskId){
        //todo

        return new BaseResult(Constants.SUCCESS_CODE, "", null);
    }


    @ApiOperation(value = "开票信息生成")
    @RequestMapping(value = "invoice/list/{taskId}", method = RequestMethod.GET)
    @ResponseBody
    /**
     * 开票信息会在客户数据库里面获取
     */
    public BaseResult invoiceList(@PathVariable String taskId){
        //todo

        return new BaseResult(Constants.SUCCESS_CODE, "", null);
    }


}
