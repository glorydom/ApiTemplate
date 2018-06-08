package com.huiyi.workflow.service;

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
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class BaseWorkFlowService {

	private Logger LOGGER = LoggerFactory.getLogger(BaseWorkFlowService.class);
	
	@Autowired
	RepositoryService repositoryService;
	@Autowired
	RuntimeService runtimeService;
	
	public List<Deployment> getAllDeployments(){
		List<Deployment> wfList = repositoryService.createDeploymentQuery().list();
		return wfList;
	}
	
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
	
	public void deleteDeployment(String deploymentId) {
		repositoryService.deleteDeployment(deploymentId);
	}
	
	public boolean startMeetingProcess(int meetingId) {
		String processDefinitionKey = "MeetingMeeting";
		String businessKey = String.valueOf(meetingId);
		List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(businessKey, processDefinitionKey).list();
		if(list != null && list.size()> 0) {
			LOGGER.error("该会议已经流程已经启动");
			return false;
		}
		Map<String,Object> param = new HashMap<String,Object>(1);
		param.put("beginAt", (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")).format(new Date()));
		runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey, param);
		return true;
	}
	
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
	
	public ProcessDefinition findProcessDefinition(String processName) {
		List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().processDefinitionKey(processName).orderByProcessDefinitionVersion().desc().list();
		if(list != null && list.size()>0)
			return list.get(0);
		return null;
	}
	
	public InputStream getProcessImage(String processName) {
		ProcessDefinition pd = findProcessDefinition(processName);
		Deployment d = repositoryService.createDeploymentQuery().deploymentId(pd.getDeploymentId()).singleResult();
		return repositoryService.getResourceAsStream(d.getId(), pd.getDiagramResourceName());
	}
}
