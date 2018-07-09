package com.huiyi.meeting.controller;

import java.util.*;

import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ResultCollectors;
import com.dto.huiyi.meeting.entity.config.ProcessDto;
import com.dto.huiyi.meeting.entity.config.TaskAssigneeDto;
import com.dto.huiyi.meeting.entity.config.TaskAssigneeSingleDto;
import com.dto.huiyi.meeting.util.Constants;
import com.huicong.upms.rpc.api.UpmsUserService;
import com.huiyi.meeting.dao.model.MeetingMeeting;
import com.huiyi.meeting.dao.model.MeetingTaskCandidate;
import com.huiyi.meeting.dao.model.MeetingTaskCandidateExample;
import com.huiyi.meeting.rpc.api.MeetingMeetingService;
import com.huiyi.meeting.rpc.api.MeetingTaskCandidateService;
import com.huiyi.workflow.service.BaseWorkFlowService;
import com.zheng.common.base.BaseController;
import com.zheng.common.base.BaseResult;
import com.zheng.common.validator.LengthValidator;
import com.zheng.common.validator.NumberValidator;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RequestMapping("/chqs/meetingManagement")
@Controller
@Api(value="会议管理")
public class MeetingSettingController extends BaseController {

	private static Logger LOGGER = LoggerFactory.getLogger(MeetingSettingController.class);
	@Autowired
    MeetingMeetingService meetingMeetingService;
	@Autowired
	MeetingTaskCandidateService meetingTaskCandidateService;
	@Autowired
	UpmsUserService upmsUserService;
	
	@Autowired
	BaseWorkFlowService baseWorkFlowService;


	
	@RequestMapping(value="/saveMeetingTaskCandidate", method = RequestMethod.GET)
	@ApiOperation(value="保存会议任务执行人候选人")
	@ResponseBody
	public Object saveMeetingTaskCandidate(	@RequestParam(required = false, name = "meetingId") Integer meetingId,
										   	@RequestParam("processName") String processName,
											@RequestParam("taskId") String taskId,
										   	@RequestParam("userId") String userId	) {
		ComplexResult result = FluentValidator.checkAll()
                .on(taskId, new LengthValidator(1, 64, "工作流任务ID"))
                .on(userId, new NumberValidator("用户"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!result.isSuccess()) {
            return new BaseResult(Constants.ERROR_CODE, "failed", result.getErrors());
        }

		MeetingTaskCandidate record = new MeetingTaskCandidate();
		record.setTaskid(taskId);
		record.setProcessname(processName);
		if(null != meetingId)
			record.setMeetingid(meetingId);
		record.setUserid(Integer.parseInt(userId));
		meetingTaskCandidateService.insert(record);
		return new BaseResult(Constants.SUCCESS_CODE, "success", record);
	}
	
	@RequestMapping(value="/saveWholeMeetingTaskCandidates", method = RequestMethod.POST)
	@ApiOperation(value="保存流程候选人")
	@ResponseBody
	@Transactional
	public Object saveWholeMeetingTaskCandidates(@RequestBody TaskAssigneeDto taskAssigneeDto) {
		if (taskAssigneeDto == null || taskAssigneeDto.getTaskSettings() ==null ) {
			return new BaseResult(Constants.ERROR_CODE, "failed", "没有需要保存的数据！");
		}
		String key = taskAssigneeDto.getKey();
		if(key == null){
		    key = "MeetingMeeting";
        }
		List<MeetingTaskCandidate> list = new ArrayList<>();
		for(TaskAssigneeSingleDto tasd: taskAssigneeDto.getTaskSettings()) {
			// 前端如果传来的人是空的 怎么处理？
			if(tasd.getUserList() == null)
				continue;
			String taskId = tasd.getTaskId();
			for(Integer uid : tasd.getUserList()) {
				MeetingTaskCandidate meetingTaskCandidate = new MeetingTaskCandidate();
				meetingTaskCandidate.setProcessname(key);
				if(null != taskAssigneeDto.getMeetingId()){
					meetingTaskCandidate.setMeetingid(taskAssigneeDto.getMeetingId());
				}
				meetingTaskCandidate.setTaskid(taskId);
				meetingTaskCandidate.setUserid(uid);
				list.add(meetingTaskCandidate);
			}
		}

		MeetingTaskCandidateExample example = new MeetingTaskCandidateExample();
		MeetingTaskCandidateExample.Criteria criteria = example.createCriteria().andProcessnameEqualTo(key);
		if(taskAssigneeDto.getMeetingId() != null){
			criteria.andMeetingidEqualTo(taskAssigneeDto.getMeetingId());
		}
		//先删除所有的
		meetingTaskCandidateService.deleteByExample(example);

		for(MeetingTaskCandidate candidate:list){
			meetingTaskCandidateService.insert(candidate);
		}

//		List<MeetingTaskCandidate> existing = meetingTaskCandidateService.selectByExample(example);
//		Map<MeetingTaskCandidate,Integer> existingMap = new HashMap<>();
//		List<Integer> existingIds = new ArrayList<Integer>();
//		for(MeetingTaskCandidate mtc : existing) {
//			int id = mtc.getId();
//			mtc.setId(null);
//			existingIds.add(id);
//			existingMap.put(mtc, id);
//		}
//		int total = 0;
//		List<Integer> duplicatedIds = new ArrayList<Integer>();
//		for(MeetingTaskCandidate mtc : list) {
//			if(existing.contains(mtc)) {
//				duplicatedIds.add(existingMap.get(mtc));
//				continue;
//			}
//			total += meetingTaskCandidateService.insert(mtc);
//		}
//		existingIds.removeAll(duplicatedIds);
//		if(existingIds.size()>0) {
//			String ids = StringUtils.join(existingIds, "-");
//			total += meetingTaskCandidateService.deleteByPrimaryKeys(ids);
//		}
		return new BaseResult(Constants.SUCCESS_CODE, "success", null);
	}
	
	@RequestMapping(value="/findWholeMeetingTaskCandidates", method = RequestMethod.GET)
	@ApiOperation(value="查询整个会议任务执行人候选人 //默认查询整个会议的流程")
	@ResponseBody
	public BaseResult findWholeMeetingTaskCandidates(@RequestParam(required = false, defaultValue = "MeetingMeeting") String key
													,@RequestParam(required = false, name = "meetingId") String meetingId) {
		TaskAssigneeDto taskAssigneeDto = new TaskAssigneeDto();
		List<TaskAssigneeSingleDto> taskSettings = new ArrayList<>();
		taskAssigneeDto.setTaskSettings(taskSettings);
		taskAssigneeDto.setKey(key);
		
		MeetingTaskCandidateExample example = new MeetingTaskCandidateExample();
		example.createCriteria().andProcessnameEqualTo(key);
		List<MeetingTaskCandidate> list = meetingTaskCandidateService.selectByExample(example);
		Map<String,List<Integer>> existingTaskAssignee = new HashMap<>();
		for(MeetingTaskCandidate mtc: list) {
			String taskId = mtc.getTaskid();
			List<Integer> userIds = existingTaskAssignee.get(taskId);
			if(userIds == null) {
				userIds = new ArrayList<>();
				existingTaskAssignee.put(taskId, userIds);
			}
			if(!userIds.contains(mtc.getUserid()))
				userIds.add(mtc.getUserid());
		}
		List<UserTask> allTasks = baseWorkFlowService.listAllUserTasks(key);
		for(UserTask t : allTasks) {
			TaskAssigneeSingleDto tasd = new TaskAssigneeSingleDto();
			tasd.setTaskId(t.getId());
			tasd.setTaskName(t.getName());
			tasd.setUserList(existingTaskAssignee.get(t.getId()));
			taskSettings.add(tasd);
		}
		return new BaseResult(Constants.SUCCESS_CODE, "success", taskAssigneeDto);
	}
	
	@RequestMapping(value="/listAllProcesses", method = RequestMethod.GET)
	@ApiOperation(value="列出所有流程的名字")
	@ResponseBody
	public BaseResult listAllProcesses() {
		List<ProcessDefinition> pdList = baseWorkFlowService.listAllProcesses();
		Set<ProcessDto> set = new HashSet<>();
		for(ProcessDefinition definition : pdList) {

			ProcessDto pd = new ProcessDto(definition);
			set.add(pd);
		}
		return new BaseResult(Constants.SUCCESS_CODE, "success", set);
	}
}
