package com.huiyi.workflow.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ResultCollectors;
import com.dto.huiyi.meeting.util.Constants;
import com.huiyi.meeting.dao.model.MeetingMeeting;
import com.huiyi.meeting.dao.model.MeetingMeetingExample;
import com.huiyi.meeting.dao.model.MeetingTaskCandidate;
import com.huiyi.meeting.rpc.api.MeetingMeetingService;
import com.huiyi.meeting.rpc.api.MeetingTaskCandidateService;
import com.huiyi.workflow.service.BaseWorkFlowService;
import com.zheng.common.base.BaseController;
import com.zheng.common.base.BaseResult;
import com.zheng.common.validator.LengthValidator;
import com.zheng.common.validator.NumberValidator;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RequestMapping("/manage/meeting")
@Controller
@Api(value="会议管理")
public class MeetingV2Controller extends BaseController {

	private static Logger LOGGER = LoggerFactory.getLogger(MeetingV2Controller.class);
	@Autowired
    MeetingMeetingService meetingMeetingService;
	@Autowired
	MeetingTaskCandidateService meetingTaskCandidateService;
	
	@Autowired
	BaseWorkFlowService baseWorkFlowService;
	
	@RequestMapping(value="/index", method = RequestMethod.GET)
	public String index(ModelMap modelMap) {
		return "/meeting/list.jsp";
	}
	
	@RequestMapping(value="/list", method = RequestMethod.GET)
	@ApiOperation(value="工作流列表")
	@ResponseBody
	public Object list(ModelMap modelMap) {
		List<MeetingMeeting> list = meetingMeetingService.selectByExample(new MeetingMeetingExample());
		Map<String, Object> result = new HashMap<>();
        result.put("rows", list);
        result.put("total", list.size());
        return result;
	}
	
	@RequestMapping(value="/create", method = RequestMethod.GET)
	@ApiOperation(value="新增")
	public String create(ModelMap modelMap) {
		return "/meeting/create.jsp";
	}
	
	@ApiOperation(value = "新增")
	@RequiresPermissions("meeting:index")
	@ResponseBody
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Object create(MeetingMeeting meeting) {
		LOGGER.info(meeting.getMeetingsubject());
		meeting.setCreationtimestamp(new Date().getTime());
		meetingMeetingService.insert(meeting);
		return new BaseResult(1, "success", 1);
	}

	@ApiOperation(value = "删除系统")
	@RequiresPermissions("meeting:index")
	@RequestMapping(value = "/delete/{ids}",method = RequestMethod.GET)
	@ResponseBody
	public Object delete(@PathVariable("ids") String ids) {
		return new BaseResult(1, "success", 1);
	}
	
	@ApiOperation(value="启动流程")
	@RequestMapping(value="/process/{ids}", method = RequestMethod.GET)
	@RequiresPermissions("meeting:index")
	@ResponseBody
	public Object process(@PathVariable("ids") String ids) {
		for(String id : StringUtils.split(ids,"-")) {
			boolean ret = baseWorkFlowService.startMeetingProcess(Integer.parseInt(id));
			if(! ret) {
				return new BaseResult(0, "fail",0);
			}
		}
		return new BaseResult(1, "success", 1);
	}
	
	@RequestMapping(value="/saveMeetingTaskCandidate", method = RequestMethod.GET)
	@ApiOperation(value="保存会议任务执行人候选人")
	@ResponseBody
	public Object saveMeetingTaskCandidate(@RequestParam("meetingId") String meetingId, @RequestParam("taskId") String taskId, @RequestParam("userId") String userId) {
		ComplexResult result = FluentValidator.checkAll()
                .on(meetingId, new NumberValidator("会议ID"))
                .on(taskId, new LengthValidator(1, 64, "工作流任务ID"))
                .on(userId, new NumberValidator("用户"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!result.isSuccess()) {
            return new BaseResult(Constants.ERROR_CODE, "failed", result.getErrors());
        }
		MeetingTaskCandidate record = new MeetingTaskCandidate();
		record.setMeetingid(Integer.parseInt(meetingId));
		record.setTaskid(taskId);
		record.setUserid(Integer.parseInt(userId));
		meetingTaskCandidateService.insert(record);
		return new BaseResult(Constants.SUCCESS_CODE, "success", record);
	}
	
	@RequestMapping(value="/saveWholeMeetingTaskCandidates", method = RequestMethod.GET)
	@ApiOperation(value="保存整个会议任务执行人候选人")
	@ResponseBody
	public Object saveWholeMeetingTaskCandidates(@RequestParam("list") List<MeetingTaskCandidate> list) {
		if (list == null || list.size() == 1) {
			return new BaseResult(Constants.ACCESS_ERROR, "failed", "没有需要保存的数据！");
		}
		for(MeetingTaskCandidate mtc : list) {
			ComplexResult result = FluentValidator.checkAll()
					.on(mtc.getTaskid(), new LengthValidator(1, 64, "工作流任务ID"))
					.doValidate()
					.result(ResultCollectors.toComplex());
			if (!result.isSuccess()) {
				return new BaseResult(Constants.ERROR_CODE, "failed", result.getErrors());
			}
		}
		for(MeetingTaskCandidate mtc : list) {
			meetingTaskCandidateService.insert(mtc);
		}
		return new BaseResult(1, "success", null);
	}
}
