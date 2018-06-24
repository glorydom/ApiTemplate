package com.huiyi.workflow.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.huiyi.meeting.service.MeetingRegisterService;
import com.huiyi.workflow.service.BaseWorkFlowService;
import com.zheng.common.base.BaseController;
import com.zheng.common.base.BaseResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@Controller
@RequestMapping("/chqs/workflow")
@Api(value="工作流管理")
public class WorkflowController extends BaseController{

	private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowController.class);
			
	@Autowired
	BaseWorkFlowService baseWorkFlowService;
	@Autowired
    MeetingRegisterService meetingRegisterService;
	
	
	@RequestMapping(value="/mytasks", method = RequestMethod.GET)
	@ApiOperation(value="工作流列表")
	@ResponseBody
	public Object mytasks() {
		Subject subject = SecurityUtils.getSubject();
		String userName = (String)subject.getPrincipal();
		List<Task> taskList = baseWorkFlowService.findTasksByUserName(userName);
		List<Map<String,Object>> list = new ArrayList<>(taskList.size());
		for(Task task : taskList) {
			Map<String,Object> taskMap = new HashMap<>();
			
			HistoricProcessInstance pi = baseWorkFlowService.findProcessInstance(task.getExecutionId());
			String objDes = meetingRegisterService.getObjectDescription(pi);
			taskMap.put("id", task.getId());
			taskMap.put("key", task.getTaskDefinitionKey()+task.getName());
			taskMap.put("name", objDes);
			taskMap.put("dueDate", task.getDueDate());
			list.add(taskMap);
		}
			
		LOGGER.debug("list size:"+list.size());
		Map<String, Object> result = new HashMap<>();
        result.put("rows", list);
        result.put("total", list.size());
        return result;
	}
	
	@RequestMapping(value="/tasks", method = RequestMethod.GET)
	@ApiOperation(value="任务列表")
	public String taskList() {
		return "/workflow/tasklist.jsp";
	}
	
	@RequestMapping(value="/completeTask/{taskIds}", method = RequestMethod.GET)
	@ApiOperation(value="完成当前任务")
	public String completeTask(@PathVariable("taskIds") String taskIds, ModelMap modelMap) {
		modelMap.put("taskIds", taskIds);
		return "/workflow/completeTask.jsp";
	}
	
	@RequestMapping(value="/completeTask", method = RequestMethod.POST)
	@ApiOperation(value="完成当前任务")
	@ResponseBody
	public Object complete(@RequestParam("taskIds") String taskIds,@RequestParam("comments") String comments) {
		LOGGER.debug(taskIds +"="+ comments);
		int cnt = baseWorkFlowService.completeTasks(taskIds, comments);
		return new BaseResult(1, "success", cnt);
	}
	
	@RequestMapping(value="/deployment", method = RequestMethod.POST)
	@ApiOperation(value="发布工作流")
	@ResponseBody
	public Object deployment(@RequestParam("file") MultipartFile file,@RequestParam("name") String name) {
		LOGGER.debug(name);
		int ret = baseWorkFlowService.createDeployment(file, name);
		if(ret == 1)
			return new BaseResult(1, "success", null);
		return new BaseResult(0,"error",null);
	}
	
	@RequestMapping(value="/list", method = RequestMethod.GET)
	@ApiOperation(value="工作流列表")
	@ResponseBody
	public Object list(ModelMap modelMap) {
		List<Deployment> wfList =  baseWorkFlowService.getAllDeployments();
		List<Map<String,Object>> rows = new ArrayList<Map<String,Object>>(wfList.size());
		for(Deployment d : wfList) {
			Map<String,Object> deploymentMap = new HashMap<String,Object>(3);
			deploymentMap.put("id", d.getId());
			deploymentMap.put("name", d.getName());
			deploymentMap.put("deploymentTime", DateFormatUtils.format(d.getDeploymentTime(), "yyyy-MM-dd HH:mm:ss"));
			rows.add(deploymentMap);
		}
		LOGGER.debug("deployment size:"+wfList.size());
		Map<String, Object> result = new HashMap<>();
        result.put("rows", rows);
        result.put("total", wfList.size());
        return result;
	}
	
	@RequestMapping(value="/index", method = RequestMethod.GET)
	@ApiOperation(value="工作流首页")
	public String index(ModelMap modelMap) {
		return "/workflow/list.jsp";
	}

	@RequestMapping(value="/delete/{ids}", method = RequestMethod.GET)
	@ApiOperation(value="删除工作流")
	@ResponseBody
	public Object delete(@PathVariable("ids") String ids) {
		LOGGER.info(ids);
		String[] idArray = StringUtils.split(ids, "-");
		for(String id : idArray) {
			baseWorkFlowService.deleteDeployment(id);
		}
		return new BaseResult(1, "success", null);
	}
	
	@RequestMapping(value="/create", method = RequestMethod.GET)
	@ApiOperation(value="新增工作流")
	public String create(ModelMap modelMap) {
		return "/workflow/create.jsp";
	}
	
	@ApiOperation(value="显示流程图")
	@RequestMapping(value="/display", method = RequestMethod.GET)
	public String displayProcessImage(@RequestParam("processName") String processName,@RequestParam("businessId") String businessId, ModelMap modelMap)throws IOException {
		ProcessDefinition pd = baseWorkFlowService.findProcessDefinition(processName);
		modelMap.put("processName", pd.getName());
		modelMap.put("businessId", businessId);
		
		return "/workflow/display.jsp";
	}
	
	@ApiOperation(value="显示流程图")
	@RequestMapping(value="/image", method = RequestMethod.GET,  produces = MediaType.IMAGE_PNG_VALUE)
	@ResponseBody
	public Object Image(@RequestParam("processName") String processName)throws IOException {
		ProcessDefinition pd = baseWorkFlowService.findProcessDefinition(processName);
		InputStream is = baseWorkFlowService.getProcessImage(processName);
		String resourceName = pd.getDiagramResourceName();
		System.out.println("process->"+resourceName);
		return IOUtils.toByteArray(is);
	}
}