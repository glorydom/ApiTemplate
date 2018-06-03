package com.huiyi.meeting;

import java.util.List;

import org.activiti.bpmn.model.UserTask;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.huiyi.meeting.dao.model.MeetingTaskCandidate;
import com.huiyi.meeting.dao.model.MeetingTaskCandidateExample;
import com.huiyi.meeting.rpc.api.MeetingTaskCandidateService;
import com.huiyi.workflow.service.BaseWorkFlowService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({
        "classpath*:applicationContext*.xml",
        "classpath*:spring/*.xml",
        "classpath*:spring*.xml"
})
public class MeetingTaskCandidateTesting {

	@Autowired
	private MeetingTaskCandidateService meetingTaskCandidateService;
	@Autowired
	private BaseWorkFlowService baseWorkFlowService;
	
	@Test
	public void saveWholeMeetingTaskCandidatesTest(){
		int meetingId = 1;
		MeetingTaskCandidateExample example = new MeetingTaskCandidateExample();
		example.createCriteria().andMeetingidEqualTo(meetingId);
		List<MeetingTaskCandidate> existing = meetingTaskCandidateService.selectByExample(example);
		for(MeetingTaskCandidate mtc : existing) {
			mtc.setId(null);
		}
		MeetingTaskCandidate toBeAdded = new MeetingTaskCandidate();
		toBeAdded.setMeetingid(meetingId);
		toBeAdded.setTaskid("2");
		toBeAdded.setUserid(3);
		
		System.out.println(existing.contains(toBeAdded));
	}
	
	@Test
	public void testListUserTask() {
		List<UserTask> list = baseWorkFlowService.listAllUserTasks("MeetingMeeting");
		for(UserTask task : list) {
			System.out.println(task.getName());
		}
	}
}
