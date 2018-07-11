package com.huiyi.meeting.controller;


import java.util.*;

import com.dto.huiyi.meeting.entity.participantDto.ParticipantCZHdto;
import com.dto.huiyi.meeting.entity.participantDto.ParticipantFeeSummary;
import com.dto.huiyi.meeting.util.TimeDateFormat;
import com.huiyi.dao.CZH;
import com.huiyi.dao.ExternalSales;
import com.huiyi.dao.JCI_ORDER;
import com.huiyi.dao.externalMapper.ExternalMeetingParticipantMapper;
import com.huiyi.meeting.service.CZHService;
import com.huiyi.meeting.service.MeetingRegisterService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ResultCollectors;
import com.dto.huiyi.meeting.entity.ParticipantSearchParameter;
import com.dto.huiyi.meeting.entity.participantDto.ParticipantStatisticsDto;
import com.dto.huiyi.meeting.util.Constants;
import com.huiyi.meeting.dao.model.MeetingParticipant;
import com.huiyi.meeting.dao.model.MeetingParticipantExample;
import com.huiyi.meeting.rpc.api.MeetingParticipantService;
import com.zheng.common.base.BaseResult;
import com.zheng.common.constants.upms.UpmsResult;
import com.zheng.common.constants.upms.UpmsResultConstant;
import com.zheng.common.util.StringUtil;
import com.zheng.common.validator.LengthValidator;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/chqs/participant")
@Api(value = "与会人员管理", description = "对与会嘉宾的注册，撤销")
public class MeetingParticipantController extends BaseController {
	private static Logger LOGGER = LoggerFactory.getLogger(MeetingParticipantController.class);

	private static String[] STATUS = new String[]{"未付款", "已付款", "已退款", "改为下一届"};
	
	@Autowired
	private CZHService czhService;

	@Autowired
    private MeetingRegisterService meetingRegisterService;

	@Autowired
    private ExternalMeetingParticipantMapper externalMeetingParticipantMapper;
	
    @ApiOperation(value = "查询所有与会人员, 要分页")
    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ResponseBody
    public BaseResult list(
    		@RequestParam(required = false, defaultValue = "0", value = "pageIndex") int pageIndex,
            @RequestParam(required = false, defaultValue = "10", value = "pageSize") int pageSize,
            @RequestParam(required = false, defaultValue = "", value = "search") String search,
            @RequestParam(required = true, value = "businessKey") String businessKey,
            @RequestParam(required = false, value = "sort") String sort,
            @RequestParam(required = false, value = "order") String order
    		){
    	int offset = pageIndex * pageSize;
    	int limit = pageSize;


    	return null;
    }

    @ApiOperation(value = "查询条件所有与会人员, 要分页")
    @RequestMapping(value = "conditionedList", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult conditionList(@RequestBody ParticipantSearchParameter participantSearchParameter){

        return new BaseResult(Constants.SUCCESS_CODE, "", null);
    }

    @ApiOperation(value = "统计所有与会人员")
    @RequestMapping(value = "list/{meetingId}", method = RequestMethod.GET)
    @ResponseBody
    public BaseResult statistics(@PathVariable String meetingId, @RequestParam boolean getAllIndicator){
        //meetingId是不必要的，因为开会前，历史订单会被删掉的
        //getAllIndicator 就是说是否查询所有的， 如果用户点击某个任务，则传递为false， 如果客户点击dashboard，则传递true
        if(meetingId.contains("_")){
            meetingId = meetingId.split("_")[1];
        }
        int meetingIdint = Integer.parseInt(meetingId);

        String userId = (String) SecurityUtils.getSubject().getPrincipal();
        List<CZH> resultCzh = new ArrayList<>();
        if(getAllIndicator){
            resultCzh = czhService.getParticipants(null);
        } else {
            resultCzh = czhService.getParticipants(userId);
        }
        Set<String> orders = new HashSet<>();
        List<ParticipantCZHdto> dtos = new ArrayList<>();
        for(CZH czh : resultCzh){
            ParticipantCZHdto participantCZHdto = new ParticipantCZHdto();
            BeanUtils.copyProperties(czh, participantCZHdto);
            if("是".equalsIgnoreCase(czh.getSFDK()))
                participantCZHdto.setStatus(STATUS[1]); //已付款
            else
                participantCZHdto.setStatus(STATUS[2]); //再CZH表中，并且是否付款为否，则代表该客户是退款的

            dtos.add(participantCZHdto);
            orders.add(participantCZHdto.getNO());
        }

        List<ExternalSales> sales = externalMeetingParticipantMapper.getCompanyBySales(userId);
        Set<String> companies = new HashSet<>();
        for(ExternalSales es:sales){
            companies.add(es.getCOMPANY());
        }

        List<JCI_ORDER> jci_orders = externalMeetingParticipantMapper.getAllUnpaidOrders();
        for(JCI_ORDER order:jci_orders){
            ParticipantCZHdto participantCZHdto = new ParticipantCZHdto();
            CZH czh = meetingRegisterService.convertFromJCItoCZH(order);
            BeanUtils.copyProperties(czh, participantCZHdto);
            participantCZHdto.setStatus(STATUS[0]);
            if(companies.contains(order.getGSMC()) &&(!orders.contains(participantCZHdto.getNO()))) // 该销售负责该公司，并且该订单没有再CZH表中
                dtos.add(participantCZHdto);
        }

        return new BaseResult(Constants.SUCCESS_CODE, "", dtos);
    }



}
