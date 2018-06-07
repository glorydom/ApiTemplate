package com.huiyi.meeting.controller;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ResultCollectors;
import com.dto.huiyi.meeting.entity.ParticipantSearchParameter;
import com.dto.huiyi.meeting.util.Constants;
import com.huiyi.meeting.dao.model.MeetingParticipant;
import com.huiyi.meeting.dao.model.MeetingParticipantExample;
import com.huiyi.meeting.rpc.api.MeetingParticipantService;
import com.zheng.common.base.BaseResult;
import com.zheng.common.constants.upms.UpmsResult;
import com.zheng.common.constants.upms.UpmsResultConstant;
import com.zheng.common.util.StringUtil;
import com.zheng.common.validator.LengthValidator;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/chqs/participant")
@Api(value = "会议管理", description = "对会议进行创建，查询，挂起，取消操作")
public class MeetingParticipantController extends BaseController {
	private static Logger LOGGER = LoggerFactory.getLogger(MeetingParticipantController.class);
	
	@Autowired
	private MeetingParticipantService meetingParticipantService;
	
    @ApiOperation(value = "查询所有与会人员, 要分页")
    @RequestMapping(value = "list/{pageIndex}/{count}", method = RequestMethod.GET)
    @ResponseBody
    public BaseResult list(
    		@RequestParam(required = false, defaultValue = "0", value = "offset") int offset,
            @RequestParam(required = false, defaultValue = "10", value = "limit") int limit,
            @RequestParam(required = false, defaultValue = "", value = "search") String search,
            @RequestParam(required = false, value = "businessKey") String businessKey,
            @RequestParam(required = false, value = "sort") String sort,
            @RequestParam(required = false, value = "order") String order
    		){
    	MeetingParticipantExample example = new MeetingParticipantExample();
    	if(!StringUtils.isBlank(businessKey) && businessKey.startsWith(MeetingParticipant.class.getSimpleName()+"_")) {
    		String meetingIdStr = businessKey.substring((MeetingParticipant.class.getSimpleName()+"_").length());
    		if(StringUtils.isNumeric(meetingIdStr))
    			example.createCriteria().andMeetingidEqualTo(Integer.parseInt(meetingIdStr));
    		else
    			LOGGER.info("传入参数businessKey不能解析出会议ID");
    	}
    	if (!StringUtils.isBlank(sort) && !StringUtils.isBlank(order)) {
    		example.setOrderByClause(StringUtil.humpToLine(sort) + " " + order);
    	}
    	if (StringUtils.isNotBlank(search)) {
    		example.or().andNameLike("%" + search + "%");
    	}
    	List<MeetingParticipant> list = meetingParticipantService.selectByExampleForOffsetPage(example, offset, limit);
    	long total = meetingParticipantService.countByExample(example);
    	Map<String, Object> result = new HashMap<>();
        result.put("rows", list);
        result.put("total", total);
        return new BaseResult(Constants.SUCCESS_CODE, "", result);
    }

    @ApiOperation(value = "查询条件所有与会人员, 要分页")
    @RequestMapping(value = "conditionedList", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult conditionList(@RequestBody ParticipantSearchParameter participantSearchParameter){

        return new BaseResult(Constants.SUCCESS_CODE, "", null);
    }


    @ApiOperation(value = "人员注册")
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult regist(@RequestBody MeetingParticipant meetingParticipant){
    	ComplexResult result = FluentValidator.checkAll()
				.on(meetingParticipant.getName(), new LengthValidator(1, 20, "名字"))
				.on(meetingParticipant.getPhone(), new LengthValidator(1, 20, "电话"))
				.doValidate()
				.result(ResultCollectors.toComplex());
		if (!result.isSuccess()) {
			return new UpmsResult(UpmsResultConstant.INVALID_LENGTH, result.getErrors());
		}
    	meetingParticipant.setCreationtimestamp(new Date().getTime());
    	int ret = meetingParticipantService.insert(meetingParticipant);
    	if(ret ==0) {
    		return new BaseResult(Constants.ERROR_CODE, "人员注册失败", meetingParticipant);
    	}
        return new BaseResult(Constants.SUCCESS_CODE, "", meetingParticipant);
    }


    @ApiOperation(value = "人员撤销")
    @RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
    @ResponseBody
    /**
     * 这个API肯定是在搜寻之后才发起的，所以传入参数必须有id,如果没有就返回 Constants.ERROR_CODE
     */
    public BaseResult deregist(@PathVariable int id){
    	int ret = meetingParticipantService.deleteByPrimaryKey(id);
    	if(ret == 0) {
    		return new BaseResult(Constants.ERROR_CODE, "未查到相关人员信息", id);
    	}
        return new BaseResult(Constants.SUCCESS_CODE, "", ret);
    }

    @ApiOperation(value = "人员详情")
    @RequestMapping(value = "get/{id}", method = RequestMethod.GET)
    @ResponseBody
    /**
     * 这个API肯定是在搜寻之后才发起的，所以传入参数必须有id,如果没有就返回 Constants.ERROR_CODE
     */
    public BaseResult get(@PathVariable int id){
    	MeetingParticipant mp = meetingParticipantService.selectByPrimaryKey(id);
    	if(mp == null) {
    		return new BaseResult(Constants.ERROR_CODE, "未查到相关人员信息", mp);
    	}
        return new BaseResult(Constants.SUCCESS_CODE, "", mp);
    }


    @ApiOperation(value = "人员信息更新")
    @RequestMapping(value = "update/{id}", method = RequestMethod.POST)
    @ResponseBody
    /**
     * 这个API肯定是在搜寻之后才发起的，所以传入参数必须有id,如果没有就返回 Constants.ERROR_CODE
     */
    public BaseResult update(@RequestBody MeetingParticipant meetingParticipant){
    	if(meetingParticipant.getId() == null) {
    		return new BaseResult(Constants.ERROR_CODE, "未查到相关人员ID", meetingParticipant);
    	}
    	ComplexResult result = FluentValidator.checkAll()
				.on(meetingParticipant.getName(), new LengthValidator(1, 20, "名字"))
				.on(meetingParticipant.getPhone(), new LengthValidator(1, 20, "电话"))
				.doValidate()
				.result(ResultCollectors.toComplex());
		if (!result.isSuccess()) {
			return new UpmsResult(UpmsResultConstant.INVALID_LENGTH, result.getErrors());
		}
		int ret = meetingParticipantService.updateByPrimaryKey(meetingParticipant);
		if(ret ==0) {
    		return new BaseResult(Constants.ERROR_CODE, "更新失败", ret);
    	}
        return new BaseResult(Constants.SUCCESS_CODE, "", ret);
    }


}
