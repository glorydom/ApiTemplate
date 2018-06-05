package com.huiyi.meeting.controller;

import com.dto.huiyi.meeting.util.Constants;
import com.dto.huiyi.meeting.util.TimeDateFormat;
import com.huiyi.meeting.dao.mapper.MeetingMeetingMapper;
import com.huiyi.meeting.dao.model.MeetingMeeting;
import com.huiyi.meeting.dao.model.MeetingMeetingExample;
import com.huiyi.meeting.rpc.api.MeetingMeetingService;
import com.zheng.common.base.BaseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import static com.dto.huiyi.meeting.util.Constants.SUCCESS_CODE;

@Controller
@RequestMapping("/chqs/meeting")
@Api(value = "会议管理", description = "对会议进行创建，查询，挂起，取消操作")
public class MeetingController extends BaseController {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    MeetingMeetingMapper meetingMeetingMapper;


    @Autowired
    MeetingMeetingService meetingMeetingService;

    @ApiOperation(value = "查询所有正在进行中的会议")
//    @RequiresPermissions("chqs:meeting:manage")
    @RequestMapping(value = "listActives", method = RequestMethod.GET)
    @ResponseBody
    public BaseResult listMeeting(){
        List<MeetingMeeting> activeMeetings = new ArrayList<>();

        List<ProcessInstance> list = runtimeService.createProcessInstanceQuery()
                .active()
                .list();
        if(list != null ){
            for(ProcessInstance ps : list){
                String bussinessKey = ps.getBusinessKey();
                if(bussinessKey.split("_")[0].equalsIgnoreCase(MeetingMeeting.class.getSimpleName())){
                    int id = Integer.parseInt(bussinessKey.split("_")[1]);
                    activeMeetings.add(meetingMeetingService.selectByPrimaryKey(id));
                }
            }
        }
        return new BaseResult(Constants.SUCCESS_CODE, "success", activeMeetings);
    }

    @ApiOperation(value = "开始一场会议")
//    @RequiresPermissions("chqs:meeting:create")
    @RequestMapping(value = "startMeeting", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult startMeeting(@RequestBody MeetingMeeting meeting){

        int id = -1;
        //save one meeting
        if(meeting != null){
            if(meeting.getBeginat() == null) meeting.setBeginat(new Date()); // 开始时间为空的话，设置为立即开始
            // set timestamp to retrive it
            long timestamp = new Date().getTime();
            meeting.setCreationtimestamp(timestamp);
            int affectCount = meetingMeetingService.insert(meeting);

            if(affectCount == 1){
                MeetingMeetingExample example = new MeetingMeetingExample();
                example.createCriteria().andCreationtimestampEqualTo(timestamp);
                List<MeetingMeeting> ms = meetingMeetingMapper.selectByExample(example);
                meeting = ms.get(0);
            }
            id = meeting.getId();
        }

        // start the meeting process
        String process_id = MeetingMeeting.class.getSimpleName();
        String bussiness_key = process_id + "_" + id;
        // prepare the parameters
        // date format: 2011-03-11T12:13:14
        Date beginAt = meeting.getBeginat();
        String activitiTime = TimeDateFormat.formatTime(beginAt);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("beginAt", activitiTime);  //

        return ControllerUtil.startNewBussinessProcess(runtimeService, meeting, id, parameters);
    }

    @ApiOperation(value = "取消一场会议")
//    @RequiresPermissions("chqs:meeting:delete")
    @RequestMapping(value = "cancel/{id}", method = RequestMethod.GET)
    @ResponseBody
    public BaseResult cancelMeeting(@PathVariable int id){
        String bussinessKey = MeetingMeeting.class.getSimpleName() + "_" + id;
        ProcessInstance ps = runtimeService.createProcessInstanceQuery()
                .processInstanceBusinessKey(bussinessKey)
                .singleResult();
        String processId = ps.getProcessInstanceId();
        runtimeService.deleteProcessInstance(processId, "取消");
        return new BaseResult(Constants.SUCCESS_CODE, "success", null);
    }

    @ApiOperation(value = "查看一场会议")
//    @RequiresPermissions("chqs:meeting:read")
    @RequestMapping(value = "checkMeeting/{id}", method = RequestMethod.GET)
    @ResponseBody
    public BaseResult viewMeeting(@PathVariable int id){
        MeetingMeeting result = meetingMeetingService.selectByPrimaryKey(id);
        return new BaseResult(SUCCESS_CODE, "success", result);
    }
}
