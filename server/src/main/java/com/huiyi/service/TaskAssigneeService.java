package com.huiyi.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huicong.upms.dao.model.UpmsUser;
import com.huicong.upms.rpc.api.UpmsUserService;
import com.huiyi.meeting.dao.model.MeetingTaskCandidate;
import com.huiyi.meeting.dao.model.MeetingTaskCandidateExample;
import com.huiyi.meeting.rpc.api.MeetingTaskCandidateService;

@Service("TaskAssigneeService")
public class TaskAssigneeService {

	@Autowired
	MeetingTaskCandidateService meetingTaskCandidateService;
	@Autowired
	UpmsUserService upmsUserService;
    /**
     * 根据任务id返回任务执行人
     * @param taskId
     * @return
     */
    public List<String> getUserIdsByTaskID (String taskId){
    	MeetingTaskCandidateExample example = new MeetingTaskCandidateExample();
    	example.createCriteria().andTaskidEqualTo(taskId);
    	List<MeetingTaskCandidate> mtcList = meetingTaskCandidateService.selectByExample(example);
    	List<String> list =  new ArrayList<>();
    	for(MeetingTaskCandidate mtc : mtcList) {
    		UpmsUser user = upmsUserService.selectByPrimaryKey(mtc.getUserid());
    		list.add(user.getUsername());
    	}
        return list;
    }

}
