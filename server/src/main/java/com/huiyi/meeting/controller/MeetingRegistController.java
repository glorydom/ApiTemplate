package com.huiyi.meeting.controller;

import java.util.*;

import com.dto.huiyi.meeting.entity.register.ComparisonResultDto;
import com.huiyi.dao.ExternalMeetingParticipant;
import com.huiyi.dao.Invoice;
import com.huiyi.dao.externalMapper.ExternalMeetingParticipantMapper;
import com.huiyi.meeting.dao.mapper.MeetingRegistMapper;
import com.huiyi.meeting.dao.model.*;
import com.huiyi.meeting.rpc.api.*;
import com.huiyi.meeting.service.FileUploadService;
import com.huiyi.meeting.service.MeetingRegisterService;
import com.huiyi.service.ActivitiTaskService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.dto.huiyi.meeting.util.Constants;
import com.zheng.common.base.BaseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/chqs/regist")
@Api(value = "嘉宾注册", description = "对嘉宾注册相关事项的管理,主要是费用确认")
public class MeetingRegistController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MeetingRegistController.class);

    private final String REGISTERFOLDER = "regist";

    private final String MATCHEDSTATEMENT = "MATCHEDSTATEMENT";

	@Autowired
	private MeetingRegistService meetingRegistService;

	@Autowired
    private TaskService taskService;

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

    @Autowired
    private MeetingRegistMapper meetingRegistMapper;

    @Autowired
    private MeetingStatementRegistService meetingStatementRegistService;

    @Autowired
    private MeetingParticipantService meetingParticipantService;

    @Autowired
    private MeetingPartiRegistService meetingPartiRegistService;

    @Autowired
    private ActivitiTaskService activitiTaskService;


    @RequestMapping(value = "uploadBankSheet/{meetingId}", method = RequestMethod.POST)
    @ApiOperation(value = "上传银行账单文件")
    @ResponseBody
    public BaseResult upload(@RequestBody List<MeetingStatement> statements, @PathVariable String meetingId){

        if(null == statements || statements.size() == 0){
            return new BaseResult(Constants.SUCCESS_CODE, "empty statement", null);
        }
        for(MeetingStatement statement:statements){
            statement.setIsdisable(false);
            statement.setMeetingid(Integer.parseInt(meetingId));
            meetingStatementService.insert(statement);
        }

        return new BaseResult(Constants.SUCCESS_CODE, "", null);
    }


    @RequestMapping(value = "statement/list/{meetingId}", method = RequestMethod.GET)
    @ApiOperation(value = "获取未确认的银行流水")
    @ResponseBody
    public BaseResult getStatements(@PathVariable String meetingId){

        MeetingStatementExample meetingStatementExample = new MeetingStatementExample();
        meetingStatementExample.createCriteria().andIsdisableEqualTo(false).andMeetingidEqualTo(Integer.parseInt(meetingId));
        List<MeetingStatement> statementList = meetingStatementService.selectByExample(meetingStatementExample);

        return new BaseResult(Constants.SUCCESS_CODE, "", statementList);
    }

    @RequestMapping(value = "statement/delete/{statementId}", method = RequestMethod.GET)
    @ApiOperation(value = "删除某条银行流水记录")
    @ResponseBody
    public BaseResult deleteStatement(@PathVariable String statementId){
        meetingStatementService.deleteByPrimaryKey(Integer.parseInt(statementId));
        return new BaseResult(Constants.SUCCESS_CODE, "", null);
    }


    @RequestMapping(value = "compare/{meetingId}", method = RequestMethod.GET)
    @ApiOperation(value = "比对银行流水与客户付款的记录")
    @ResponseBody
    /**
     * 财务人员上传来自银行的对账单 excel格式的， 有两个字段  公司 | 金额
     * 开启流程，并且完成第一个任务： 上传银行账单
     */
    public BaseResult compare(@PathVariable String meetingId) {

        int meetingId_int = Integer.parseInt(meetingId);
        // 查找历史statement记录
        MeetingStatementExample meetingStatementExample = new MeetingStatementExample();
        meetingStatementExample.createCriteria().andIsdisableEqualTo(false).andMeetingidEqualTo(meetingId_int);
        List<MeetingStatement> statementList = meetingStatementService.selectByExample(meetingStatementExample);

        Date now = new Date();
        long creationTime= new Date().getTime();

        //获取本地已经同步的用户
        MeetingParticipantExample synchronizeMeetingParticipantExample = new MeetingParticipantExample();
        List<MeetingParticipant> synchronizedMeetingParticipants = meetingParticipantService.selectByExample(synchronizeMeetingParticipantExample);


        //获取外部数据库用户注册信息 并且将数据同步至本地数据库
        List<ExternalMeetingParticipant> externalMeetingParticipants = externalMeetingParticipantMapper.getExternalMeetingParticipants(now);
        for(ExternalMeetingParticipant externalMeetingParticipant:externalMeetingParticipants){
            String phoneNumber = externalMeetingParticipant.getTelephone();
            boolean contains = false;
            for(MeetingParticipant meetingParticipant:synchronizedMeetingParticipants){
                if(meetingParticipant.getTelephone().equalsIgnoreCase(phoneNumber)){
                    contains = true;
                }
            }
            if(!contains){
                //说明该记录未被保存
                MeetingParticipant meetingParticipant = new MeetingParticipant();
                meetingParticipant.setCompany(externalMeetingParticipant.getCompanyName());
                meetingParticipant.setTelephone(externalMeetingParticipant.getTelephone());
                meetingParticipant.setCreationtimestamp(creationTime);

                meetingParticipant.setMeetingid(meetingId_int); //默认取到的所有嘉宾都是本届会议的， 在取外部嘉宾的时候，应该使用sql来确认这个会议id

                meetingParticipant.setName(externalMeetingParticipant.getParticipantName());
                meetingParticipant.setMeetingfee(externalMeetingParticipant.getFee());
                meetingParticipant.setMeetingregistertime(externalMeetingParticipant.getRegistTime());
                meetingParticipant.setPaid(false);  //同步过来的数据 均为未付款的，财务人员确认之后才会变为已付款
                meetingParticipant.setMeetingfeepaidtime(now);
                try{
                    meetingParticipantService.insert(meetingParticipant);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        }

        // 获取本地数据库里面的已经确认付费的嘉宾
        MeetingParticipantExample meetingParticipantExample = new MeetingParticipantExample();
        meetingParticipantExample.createCriteria().andPaidEqualTo(true);
        List<MeetingParticipant> paidMeetingParticipants = meetingParticipantService.selectByExample(meetingParticipantExample);
        //获取本地数据库里面 已付款但是还没有确认的嘉宾
        MeetingPartiRegistExample meetingPartiRegistExample = new MeetingPartiRegistExample();
        meetingPartiRegistExample.createCriteria();
        List<MeetingPartiRegist> meetingPartiRegists = meetingPartiRegistService.selectByExample(meetingPartiRegistExample);
        for(MeetingPartiRegist regist:meetingPartiRegists){
            int meetingParticipantId = regist.getParticipantid();
            boolean contains = false;
            for(MeetingParticipant participant:paidMeetingParticipants){
                if(participant.getId() == meetingParticipantId){
                    contains = true;
                }
            }
            if(!contains)
                paidMeetingParticipants.add(meetingParticipantService.selectByPrimaryKey(meetingParticipantId));//该id代表嘉宾已经付款，但是还没有确认，所以应该将其加入到已付款客户里
        }


        List<ExternalMeetingParticipant> unpaidMeetingParticipant = this.getUnpaidParticipant(paidMeetingParticipants, meetingId_int);

        //在未付款的列表里 将已经付款的人排除
        return new BaseResult(Constants.SUCCESS_CODE, "reconsile the fee", meetingRegisterService.reconsile(statementList, unpaidMeetingParticipant, null));
    }

    private List<ExternalMeetingParticipant> getUnpaidParticipant(List<MeetingParticipant> paidMeetingParticipants, int meetingId){
        MeetingParticipantExample meetingParticipantExample = new MeetingParticipantExample();
        meetingParticipantExample.createCriteria().andPaidEqualTo(false).andMeetingidEqualTo(meetingId);
        //从数据库中获取未付款客户，
        List<MeetingParticipant> oriUnpaidMeetingParticipants = meetingParticipantService.selectByExample(meetingParticipantExample);
        //初始化未付款客户列表
        List<MeetingParticipant> unpaidMeetingParticipants = new ArrayList<>();
        //统计未付款的客户
        for(MeetingParticipant p:oriUnpaidMeetingParticipants){
            if(!this.participantContains(paidMeetingParticipants, p)){  //已付款客户清单里面未包含该客户
                unpaidMeetingParticipants.add(p);
            }
        }

        List<ExternalMeetingParticipant> externalMeetingParticipants = new ArrayList<>();
        for(MeetingParticipant participant : unpaidMeetingParticipants){
            ExternalMeetingParticipant externalMeetingParticipant = new ExternalMeetingParticipant();
            externalMeetingParticipant.setCompanyName(participant.getCompany());
            externalMeetingParticipant.setParticipantName(participant.getName());
            externalMeetingParticipant.setFee(participant.getMeetingfee());
            externalMeetingParticipant.setRegistTime(participant.getMeetingregistertime());
            externalMeetingParticipant.setTelephone(participant.getTelephone());
            externalMeetingParticipants.add(externalMeetingParticipant);
        }

        return externalMeetingParticipants;
    }


    private boolean participantContains(List<MeetingParticipant> list, MeetingParticipant participant){
        for(MeetingParticipant p:list){
            if(p.getTelephone().equalsIgnoreCase(participant.getTelephone())){
                return true;
            }
        }
        return false;
    }

    @ApiOperation(value = "财务人员确认费用")
    @RequestMapping(value = "accountantConfirm", method = RequestMethod.POST)
    @ResponseBody
    /**
     * 将财务人员确认付过款的嘉宾保存起来，并生成相应的task assign给相关销售人员
     */
    public BaseResult confirmFeeByAccountant(@RequestBody List<ComparisonResultDto> comparisonResultDtos){

        Date now = new Date();
        long currentTime = now.getTime();
        MeetingRegist meetingRegist = new MeetingRegist();
        meetingRegist.setCreationtimestamp(currentTime);
        meetingRegistService.insert(meetingRegist);

        MeetingRegistExample meetingRegistExample = new MeetingRegistExample();
        meetingRegistExample.createCriteria().andCreationtimestampEqualTo(currentTime);
        List<MeetingRegist> meetingRegists = meetingRegistMapper.selectByExample(meetingRegistExample);
        if(meetingRegist != null && meetingRegists.size() == 1){
            meetingRegist = meetingRegists.get(0);
        }

        //更新银行流水
        for(ComparisonResultDto dto:comparisonResultDtos){
            for(MeetingStatement statement:dto.getStatements()){
                statement.setIsdisable(true); //防止以后对比账户再次出现
                MeetingStatementRegist meetingStatementRegist = new MeetingStatementRegist();
                meetingStatementRegist.setStatementid(statement.getId());
                meetingStatementRegist.setMeetingregistid(meetingRegist.getId());
                meetingStatementRegistService.insert(meetingStatementRegist);//设置关联关系
                meetingStatementService.updateByPrimaryKey(statement);

                //更新嘉宾信息
                for(ExternalMeetingParticipant externalMeetingParticipant:dto.getParticipants()){
                    MeetingPartiRegist meetingPartiRegist = new MeetingPartiRegist();
                    meetingPartiRegist.setMeetingregistid(meetingRegist.getId());

                    String phoneNumber = externalMeetingParticipant.getTelephone();
                    MeetingParticipantExample meetingParticipantExample = new MeetingParticipantExample();
                    meetingParticipantExample.createCriteria().andTelephoneEqualTo(phoneNumber);
                    List<MeetingParticipant> meetingParticipants = meetingParticipantService.selectByExample(meetingParticipantExample);
                    MeetingParticipant participant = meetingParticipants.get(0);  // phoneNumber是唯一的，因此只要第一个
                    participant.setPaid(true);
                    participant.setMeetingfeepaidtime(now);
                    participant.setMeetingfee(externalMeetingParticipant.getFee());
                    meetingParticipantService.updateByPrimaryKey(participant); // 跟新数据库

                    meetingPartiRegist.setParticipantid(participant.getId());

                    meetingPartiRegistService.insert(meetingPartiRegist);

                }

            }
        }

        Map<String, Object> parameters = new HashMap<>();
        List<String> users = new ArrayList<>();
        for(ComparisonResultDto dto : comparisonResultDtos){
            List<String> saleMans = meetingRegisterService.getSalemanForThisCompany(dto.getCompanyName());
            for(String user:saleMans){
                if(!users.contains(user))
                    users.add(user);
            }
        }
        parameters.put("Salemans", users);

        return ControllerUtil.startNewBussinessProcess(runtimeService, meetingRegist, meetingRegist.getId(), parameters);
    }


    @ApiOperation(value = "销售人员查看客户付款情况")
    @RequestMapping(value = "getParticipantFee/{taskId}", method = RequestMethod.GET)
    @ResponseBody
    public BaseResult confirm(@PathVariable String taskId){
        String userId = (String) SecurityUtils.getSubject().getPrincipal();
        String bussinessKey = activitiTaskService.getBusinessObjId(taskId);
        if(null == bussinessKey){
            return new BaseResult(Constants.ERROR_CODE, "not found the taskId:" + taskId, null);
        }

        int meetingRegistId = Integer.parseInt(bussinessKey.split("_")[1]);
        MeetingStatementRegistExample meetingStatementRegistExample = new MeetingStatementRegistExample();
        meetingStatementRegistExample.createCriteria().andMeetingregistidEqualTo(meetingRegistId);
        List<MeetingStatementRegist> meetingStatementRegists = meetingStatementRegistService.selectByExample(meetingStatementRegistExample);
        List<MeetingStatement> statementList = new ArrayList<>(); //统计该
        for(MeetingStatementRegist statementRegist:meetingStatementRegists){
            int statementId = statementRegist.getStatementid();
            statementList.add(meetingStatementService.selectByPrimaryKey(statementId));
        }

        MeetingPartiRegistExample meetingPartiRegistExample = new MeetingPartiRegistExample();
        meetingPartiRegistExample.createCriteria().andMeetingregistidEqualTo(meetingRegistId);
        Set<Integer> participantIds = new HashSet<>();
        List<MeetingPartiRegist> meetingPartiRegists = meetingPartiRegistService.selectByExample(meetingPartiRegistExample);
        for(MeetingPartiRegist meetingPartiRegist:meetingPartiRegists){
            int participantId = meetingPartiRegist.getParticipantid();
            participantIds.add(participantId);
        }

        //获取所有与之关联的嘉宾
        List<MeetingParticipant> meetingParticipants = new ArrayList<>();
        for(int id: participantIds){
            MeetingParticipant participant = meetingParticipantService.selectByPrimaryKey(id);
            meetingParticipants.add(participant);
        }

        List<ExternalMeetingParticipant> unpaidMeetingParticipant = meetingRegisterService.convertToExternalMeetingParticipants(meetingParticipants);

        List<String> filterCompanies = meetingRegisterService.getCompanyBySaleman(userId);//找到这个销售的负责的公司

        return new BaseResult(Constants.SUCCESS_CODE, "reconsile the fee", meetingRegisterService.reconsile(statementList, unpaidMeetingParticipant, filterCompanies));
    }

    @ApiOperation(value = "销售人员确认付费情况")
    @RequestMapping(value = "salesConfirm/{taskId}", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult confirmBySale(@RequestBody List<ComparisonResultDto> comparisonResultDtos,  @PathVariable String taskId){
        Map<String, Object> parameters = new HashMap<>();
//        parameters.put(MATCHEDSTATEMENT, comparisonResultDtos);
        runtimeService.setVariable(activitiTaskService.getExecutionIdByTaskId(taskId), MATCHEDSTATEMENT, comparisonResultDtos); //将变量设置到子流程里
        taskService.complete(taskId, parameters); //将匹配的流水放入流程变量
        return new BaseResult(Constants.SUCCESS_CODE, "sales confirmed", null);
    }

    @ApiOperation(value = "开票信息生成")
    @RequestMapping(value = "invoice/list/{taskId}", method = RequestMethod.GET)
    @ResponseBody
    /**
     * 开票信息会在客户数据库里面获取
     */
    public BaseResult invoiceList(@PathVariable String taskId){

        List<ComparisonResultDto> comparisonResultDtos  = (List<ComparisonResultDto>) runtimeService.getVariable(activitiTaskService.getExecutionIdByTaskId(taskId), MATCHEDSTATEMENT);
        if(null == comparisonResultDtos){
            return new BaseResult(Constants.ERROR_CODE, "system error", null);
        }

        List<Invoice> invoices = new ArrayList<>();
        for(ComparisonResultDto dto:comparisonResultDtos){
            Invoice invoice = externalMeetingParticipantMapper.getCompanyInvoiceInfo(dto.getCompanyName());
            invoices.add(invoice);
            invoice.setComparisonResultDto(dto);
        }
        return new BaseResult(Constants.SUCCESS_CODE, "got invoice info", invoices);
    }


    @ApiOperation(value = "开票完成")
    @RequestMapping(value = "invoice/complete/{taskId}", method = RequestMethod.GET)
    @ResponseBody
    /**
     * 开票信息会在客户数据库里面获取
     */
    public BaseResult invoiceComplete(@PathVariable String taskId){

        taskService.complete(taskId);

        return new BaseResult(Constants.SUCCESS_CODE, "invoice complete", null);
    }

    @ApiOperation(value = "统计--所有已经付款的客户")
    @RequestMapping(value = "summary/paidParticipants/{meetingId}", method = RequestMethod.GET)
    @ResponseBody
    public BaseResult summary(@PathVariable int meetingId){
        MeetingParticipantExample meetingParticipantExample = new MeetingParticipantExample();
        meetingParticipantExample.createCriteria().andPaidEqualTo(true).andMeetingidEqualTo(meetingId);
        List<MeetingParticipant> participants = meetingParticipantService.selectByExample(meetingParticipantExample);

        return new BaseResult(Constants.SUCCESS_CODE, "all paid participants", participants);
    }

}
