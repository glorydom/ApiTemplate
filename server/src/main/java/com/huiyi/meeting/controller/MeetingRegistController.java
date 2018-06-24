package com.huiyi.meeting.controller;

import java.util.Date;
import java.util.List;

import com.huiyi.dao.ExternalMeetingParticipant;
import com.huiyi.dao.externalMapper.ExternalMeetingParticipantMapper;
import com.huiyi.meeting.dao.model.*;
import com.huiyi.meeting.rpc.api.MeetingStatementService;
import com.huiyi.meeting.service.FileUploadService;
import com.huiyi.meeting.service.MeetingRegisterService;
import org.activiti.engine.RuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.dto.huiyi.meeting.util.Constants;
import com.huiyi.meeting.rpc.api.MeetingRegistService;
import com.zheng.common.base.BaseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/chqs/regist")
@Api(value = "嘉宾注册", description = "对嘉宾注册相关事项的管理,主要是费用确认")
public class MeetingRegistController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MeetingRegistController.class);

    private final String REGISTERFOLDER = "regist";

	@Autowired
	private MeetingRegistService meetingRegistService;

	@Autowired
	private MeetingRegisterService meetingRegisterService;

	@Autowired
    private FileUploadService fileUploadService;

	@Autowired
    private RuntimeService runtimeService;

	@Autowired
    private MeetingStatementService meetingStatementService;

    @Autowired
    private ExternalMeetingParticipantMapper externalMeetingParticipantMapper;


    @RequestMapping(value = "uploadBankSheet", method = RequestMethod.POST)
    @ApiOperation(value = "上传银行账单文件")
    @ResponseBody
    /**
     * 财务人员上传来自银行的对账单 excel格式的， 有两个字段  公司 | 金额
     * 开启流程，并且完成第一个任务： 上传银行账单
     */
    public BaseResult uploadBankSheet(@RequestBody List<MeetingStatement> statements) {

        if(null == statements || statements.size() == 0){
            return new BaseResult(Constants.SUCCESS_CODE, "empty statement", null);
        }

        Date now = new Date();
        long creationTime= new Date().getTime();

        //获取外部数据库用户注册信息
        List<ExternalMeetingParticipant> externalMeetingParticipants = externalMeetingParticipantMapper.getExternalMeetingParticipants(now);

        return new BaseResult(Constants.SUCCESS_CODE, "reconsile the fee", meetingRegisterService.reconsile(statements, externalMeetingParticipants));

    }

    @ApiOperation(value = "会务款项比对")
    @RequestMapping(value = "compare/{taskId}", method = RequestMethod.GET)
    @ResponseBody
    /**
     * taskID是activiti的Taskid 根据taskId可以找到businessKey 从而找到MeetingRegist 读取Excel内容 将公司信息跟注册人员付款信息比对。
     */
    public BaseResult compare(@PathVariable String taskId){
        //todo
//        MeetingParticipant participant = new MeetingParticipant();
//        participant.getMeetingfee();
//        participant.getMeetingfeepaidtime();
//        participant.getMeetingid();
//        participant.getCharged();
//        participant.getName();
//        participant.getTelephone();
        //以上字段为participant返回必须有的字段

//    	int registId = baseWorkFlowService.findBusinessIdbyTaskId(taskId);
//    	MeetingRegist mr = meetingRegistService.selectByPrimaryKey(registId);
//        List<ComparisonResultDto> resultDtos = meetingRegisterService.reconsile(mr.getFeesheetexcel());
//        return new BaseResult(Constants.SUCCESS_CODE, "", resultDtos);
        return null;
    }

    @ApiOperation(value = "款项确认")
    @RequestMapping(value = "confirm/{taskId}", method = RequestMethod.GET)
    @ResponseBody
    /**
     *   读取Excel内容 将公司信息跟注册人员付款信息比对。
     */
    public BaseResult confirm(@PathVariable String taskId){
        //todo

        return new BaseResult(Constants.SUCCESS_CODE, "", null);
    }


    @ApiOperation(value = "开票信息生成")
    @RequestMapping(value = "invoice/list/{taskId}", method = RequestMethod.GET)
    @ResponseBody
    /**
     * 开票信息会在客户数据库里面获取
     */
    public BaseResult invoiceList(@PathVariable String taskId){
        //todo

        return new BaseResult(Constants.SUCCESS_CODE, "", null);
    }


}
