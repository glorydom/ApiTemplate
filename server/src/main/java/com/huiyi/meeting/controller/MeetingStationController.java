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
import com.dto.huiyi.meeting.util.Constants;
import com.huiyi.meeting.dao.model.MeetingStation;
import com.huiyi.meeting.dao.model.MeetingStationExample;
import com.huiyi.meeting.rpc.api.MeetingStationService;
import com.zheng.common.base.BaseResult;
import com.zheng.common.constants.upms.UpmsResult;
import com.zheng.common.constants.upms.UpmsResultConstant;
import com.zheng.common.util.StringUtil;
import com.zheng.common.validator.LengthValidator;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@Controller
@RequestMapping("/chqs/station")
@Api(value = "机场车站", description = "对机场车站的增删改查")
public class MeetingStationController extends BaseController {
	private static Logger LOGGER = LoggerFactory.getLogger(MeetingStationController.class);
	
	@Autowired
	private MeetingStationService meetingStationService;
	
	
	@ApiOperation(value = "查询机场车站, 要分页")
    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ResponseBody
    public BaseResult list(
    		@RequestParam(required = false, defaultValue = "0", value = "pageIndex") int pageIndex,
            @RequestParam(required = false, defaultValue = "10", value = "pageSize") int pageSize,
            @RequestParam(required = false, defaultValue = "", value = "search") String search,
            @RequestParam(required = false, value = "sort") String sort,
            @RequestParam(required = false, value = "order") String order
    		){
    	int offset = pageIndex * pageSize;
    	int limit = pageSize;Map<String, Object> result = new HashMap<>();
    	MeetingStationExample example = new MeetingStationExample();
    	if (!StringUtils.isBlank(sort) && !StringUtils.isBlank(order)) {
    		example.setOrderByClause(StringUtil.humpToLine(sort) + " " + order);
    	}
    	if (StringUtils.isNotBlank(search)) {
    		example.or().andNameLike("%" + search + "%");
    		example.or().andAddressLike("%" + search + "%");
    	}
    	List<MeetingStation> list = meetingStationService.selectByExampleForOffsetPage(example, offset, limit);
    	long total = meetingStationService.countByExample(example);
        result.put("rows", list);
        result.put("total", total);
        return new BaseResult(Constants.SUCCESS_CODE, "", result);
    }
	
	@ApiOperation(value = "添加机场车站")
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult regist(@RequestBody MeetingStation MeetingStation){
		ComplexResult result = validateObject(MeetingStation);
		if (!result.isSuccess()) {
			return new UpmsResult(UpmsResultConstant.INVALID_LENGTH, result.getErrors());
		}
		MeetingStation.setCreationtimestamp(new Date().getTime());
    	int ret = meetingStationService.insert(MeetingStation);
    	if(ret ==0) {
    		LOGGER.error("添加失败");
    		return new BaseResult(Constants.ERROR_CODE, "添加机场车站失败", MeetingStation);
    	}
        return new BaseResult(Constants.SUCCESS_CODE, "", MeetingStation);
    }


    @ApiOperation(value = "删除机场车站")
    @RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
    @ResponseBody
    /**
     * 这个API肯定是在搜寻之后才发起的，所以传入参数必须有id,如果没有就返回 Constants.ERROR_CODE
     */
    public BaseResult deregist(@PathVariable int id){
    	int ret = meetingStationService.deleteByPrimaryKey(id);
    	if(ret == 0) {
    		return new BaseResult(Constants.ERROR_CODE, "未查到相关信息", id);
    	}
        return new BaseResult(Constants.SUCCESS_CODE, "", ret);
    }

    @ApiOperation(value = "机场车站详情")
    @RequestMapping(value = "get/{id}", method = RequestMethod.GET)
    @ResponseBody
    /**
     * 这个API肯定是在搜寻之后才发起的，所以传入参数必须有id,如果没有就返回 Constants.ERROR_CODE
     */
    public BaseResult get(@PathVariable int id){
    	MeetingStation mh = meetingStationService.selectByPrimaryKey(id);
    	if(mh == null) {
    		return new BaseResult(Constants.ERROR_CODE, "未查到相关信息", mh);
    	}
        return new BaseResult(Constants.SUCCESS_CODE, "", mh);
    }


    @ApiOperation(value = "机场车站信息更新")
    @RequestMapping(value = "update/{id}", method = RequestMethod.POST)
    @ResponseBody
    /**
     * 这个API肯定是在搜寻之后才发起的，所以传入参数必须有id,如果没有就返回 Constants.ERROR_CODE
     */
    public BaseResult update(@RequestBody MeetingStation MeetingStation){
    	if(MeetingStation.getId() == null) {
    		return new BaseResult(Constants.ERROR_CODE, "未查到相关信息", MeetingStation);
    	}
    	ComplexResult result = validateObject(MeetingStation);
		if (!result.isSuccess()) {
			return new UpmsResult(UpmsResultConstant.INVALID_LENGTH, result.getErrors());
		}
		int ret = meetingStationService.updateByPrimaryKey(MeetingStation);
		if(ret ==0) {
    		return new BaseResult(Constants.ERROR_CODE, "更新失败", ret);
    	}
        return new BaseResult(Constants.SUCCESS_CODE, "", ret);
    }
    
    private ComplexResult validateObject(MeetingStation MeetingStation) {
    	ComplexResult result = FluentValidator.checkAll()
				.on(MeetingStation.getAddress(), new LengthValidator(1, 200, "地址"))
				.on(MeetingStation.getName(), new LengthValidator(1, 40, "机场车站名称"))
				.on(MeetingStation.getStationtype(), new LengthValidator(1, 10, "机场车站名称"))
				.doValidate()
				.result(ResultCollectors.toComplex());
    	return result;
    }
}
