package com.huiyi.workflow.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.huiyi.meeting.dao.model.MeetingParticipant;
import com.huiyi.meeting.dao.model.MeetingRegist;
import com.huiyi.workflow.service.BaseWorkFlowService;

@Service
public class BaseWorkFlowServiceImpl implements BaseWorkFlowService{
private Logger LOGGER = LoggerFactory.getLogger(BaseWorkFlowService.class);
	
	@Autowired
	RepositoryService repositoryService;
	@Autowired
	RuntimeService runtimeService;
	@Autowired
	TaskService taskService;
	@Autowired
	HistoryService historyService;
	
	@Override
	public List<Deployment> getAllDeployments(){
		List<Deployment> wfList = repositoryService.createDeploymentQuery().list();
		return wfList;
	}
	
	@Override
	public int createDeployment(MultipartFile file,String name) {
		try {
			File serverFile = File.createTempFile("wf_", "");
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
			stream.write(file.getBytes());
			stream.close();
			//2：将File类型的文件转化成ZipInputStream流
			ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(serverFile.getAbsoluteFile()));
			repositoryService.createDeployment()//创建部署对象
							.name(StringUtils.defaultIfBlank(name, file.getName()))//添加部署名称
							.addZipInputStream(zipInputStream)//
							.deploy();//完成部署
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	@Override
	public void deleteDeployment(String deploymentId) {
		repositoryService.deleteDeployment(deploymentId);
	}
	
	@Override
	public boolean startMeetingProcess(int meetingId) {
		String processDefinitionKey = "MeetingMeeting";
		String businessKey = String.valueOf(meetingId);
		Map<String,Object> param = new HashMap<String,Object>(1);
		param.put("beginAt", (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")).format(new Date()));
		param.put("secondWeek", (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")).format(new Date()));
		return startProcess(processDefinitionKey, businessKey, param);
	}
	
	@Override
	public boolean startRegisterProcess(int participantId) {
		// TODO Auto-generated method stub
		String processDefinitionKey = "MeetingRegister";
		String businessKey = MeetingParticipant.class.getSimpleName()+"_"+ String.valueOf(participantId);
		Map<String,Object> param = new HashMap<String,Object>(1);
		return startProcess(processDefinitionKey, businessKey, param);
	}
	
	@Override
	public boolean startRegistProcess(int registId) {
		// TODO Auto-generated method stub
		String processDefinitionKey = "Regist";
		String businessKey = MeetingRegist.class.getSimpleName()+"_"+ String.valueOf(registId);
		Map<String,Object> param = new HashMap<String,Object>(1);
		return startProcess(processDefinitionKey, businessKey, param);
	}

	@Override
	public boolean startProcess(String processDefinitionKey, String businessKey,Map<String,Object> param) {
		List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(businessKey, processDefinitionKey).list();
		LOGGER.debug(processDefinitionKey+"-"+businessKey + "流程图数量："+list.size());
		if(list != null && list.size()> 0) {
			LOGGER.error("流程已经启动");
			return false;
		}
		runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey, param);
		return true;
	}

	@Override
	public List<UserTask> listAllUserTasks(String processName){
		ProcessDefinition pd = findProcessDefinition(processName);
        BpmnModel model = repositoryService.getBpmnModel(pd.getId());
        List<UserTask> taskList = new ArrayList<UserTask>();
        if(model != null) {
            Collection<FlowElement> flowElements = model.getMainProcess().getFlowElements();
            for(FlowElement e : flowElements) {
                if( e instanceof UserTask) {
                	taskList.add((UserTask) e);
                }
            }
        }

        return taskList;
    }
	
	@Override
	public ProcessDefinition findProcessDefinition(String processName) {
		List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
				.processDefinitionKey(processName)
				.orderByProcessDefinitionVersion()
				.desc()
				.list();

		if(list != null && list.size()>0){
			ProcessDefinition pd = list.get(0);
			return list.get(0);
		}
		return null;
	}
	
	@Override
	public InputStream getProcessImage(String processName) {
		ProcessDefinition pd = findProcessDefinition(processName);
		Deployment d = repositoryService.createDeploymentQuery().deploymentId(pd.getDeploymentId()).singleResult();
		return repositoryService.getResourceAsStream(d.getId(), pd.getDiagramResourceName());
	}

	@Override
	public List<Task> findTasksByUserName(String userName) {
		List<Task> taskList = taskService.createTaskQuery().taskCandidateOrAssigned(userName).orderByExecutionId().desc().list();
		return taskList;
	}

	@Override
	public boolean completeTask(String taskId, Map<String, Object> variables) {
		try {
			taskService.complete(taskId,variables);
		}catch (Exception e) {
			LOGGER.error(e.getMessage());
			return false;
		}
		return true;
	}

	@Override
	public HistoricProcessInstance findProcessInstance(String executionId) {
		// TODO Auto-generated method stub
		Execution e = runtimeService.createExecutionQuery().executionId(executionId).singleResult();
		HistoricProcessInstance hpi = historyService.createHistoricProcessInstanceQuery().processInstanceId(e.getProcessInstanceId()).singleResult();;
		return hpi;
	}

	@Override
	public boolean checkProcessInstance(String processName, String businessKey) {
		// TODO Auto-generated method stub
		List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery().processDefinitionKey(processName).processInstanceBusinessKey(businessKey).list();
		return list.size()>0;
	}

	@Override
	public List<ProcessDefinition> listAllProcesses() {
		// TODO Auto-generated method stub
		List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().orderByProcessDefinitionVersion().asc().list();
		Map<String,ProcessDefinition> uniq = new HashMap<>();
		for(ProcessDefinition pd : list) {
			uniq.put(pd.getKey(), pd);
		}
		return new ArrayList<ProcessDefinition>(uniq.values());
	}

	@Override
	public int completeTasks(String taskIds, String comments) {
		// TODO Auto-generated method stub
		int cnt = 0;
		for(String id: taskIds.split("-")) {
			Map<String,Object> variables = new HashMap<>();
			variables.put("comment", comments);
			if(completeTask(id, variables))
				cnt ++;
		}
		return cnt;
	}

	@Override
	public int findBusinessIdbyTaskId(String taskId) {
		// TODO Auto-generated method stub
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		HistoricProcessInstance hpi = findProcessInstance(task.getExecutionId());
		if(hpi.getBusinessKey().contains("_")) {
			String bk = hpi.getBusinessKey().split("_")[1];
			if(StringUtils.isNumeric(bk))
				return Integer.parseInt(bk);
		}
		return -1;
	}
}
