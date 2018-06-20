package com.huiyi.workflow.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.springframework.web.multipart.MultipartFile;

public interface BaseWorkFlowService {

	//获取所有发布的流程
	public List<Deployment> getAllDeployments();
	
	//发布一个流程图
	public int createDeployment(MultipartFile file,String name);
	
	//删除一个流程图
	public void deleteDeployment(String deploymentId);
	
	//启动流程
	public boolean startProcess(String processDefinitionKey, String businessKey,Map<String,Object> param);
	
	//启动一个会议的流程
	public boolean startMeetingProcess(int meetingId);
	
	//启动参会人员注册流程
	public boolean startRegisterProcess(int participantId);
	
	//获取一个流程所有的用户任务
	public List<UserTask> listAllUserTasks(String processName);
	
	//获取流程定义信息
	public ProcessDefinition findProcessDefinition(String processName);
	
	//获取流程图片
	public InputStream getProcessImage(String processName);
	
	//根据用户名获取正在执行中的任务
	public List<Task> findTasksByUserName(String userName);
	
	//完成指定任务
	public boolean completeTask(String taskId, Map<String,Object> variables);
	
	//根据执行ID获取流程实例对象
	public HistoricProcessInstance findProcessInstance(String executionId);
	
	//根据流程名和业务key查询是否已经启动相应实例
	public boolean checkProcessInstance(String processName,String businessKey);
	
	//列出所有流程图定义
	public List<ProcessDefinition> listAllProcesses();
	
	//完成任务
	public int completeTasks(String taskIds, String comments);
}
