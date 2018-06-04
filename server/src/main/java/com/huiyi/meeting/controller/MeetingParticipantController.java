package com.huiyi.meeting.controller;


import com.dto.huiyi.meeting.entity.ParticipantSearchParameter;
import com.dto.huiyi.meeting.util.Constants;
import com.huiyi.meeting.dao.model.MeetingParticipant;
import com.zheng.common.base.BaseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/chqs/participant")
@Api(value = "会议管理", description = "对会议进行创建，查询，挂起，取消操作")
public class MeetingParticipantController extends BaseController {

    @ApiOperation(value = "查询所有与会人员, 要分页")
    @RequestMapping(value = "list/{pageIndex}/{count}", method = RequestMethod.GET)
    @ResponseBody
    public BaseResult list(@PathVariable int pageIndex, @PathVariable int count){

        return new BaseResult(Constants.SUCCESS_CODE, "", null);
    }

    @ApiOperation(value = "查询条件所有与会人员, 要分页")
    @RequestMapping(value = "conditionedList", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult conditionList(@RequestBody ParticipantSearchParameter participantSearchParameter){

        return new BaseResult(Constants.SUCCESS_CODE, "", null);
    }


    @ApiOperation(value = "人员注册")
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult regist(@RequestBody MeetingParticipant meetingParticipant){

        return new BaseResult(Constants.SUCCESS_CODE, "", null);
    }


    @ApiOperation(value = "人员撤销")
    @RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
    @ResponseBody
    /**
     * 这个API肯定是在搜寻之后才发起的，所以传入参数必须有id,如果没有就返回 Constants.ERROR_CODE
     */
    public BaseResult deregist(@PathVariable int id){

        return new BaseResult(Constants.SUCCESS_CODE, "", null);
    }


    @ApiOperation(value = "人员信息更新")
    @RequestMapping(value = "update/{id}", method = RequestMethod.POST)
    @ResponseBody
    /**
     * 这个API肯定是在搜寻之后才发起的，所以传入参数必须有id,如果没有就返回 Constants.ERROR_CODE
     */
    public BaseResult deregist(@RequestBody MeetingParticipant meetingParticipant){

        return new BaseResult(Constants.SUCCESS_CODE, "", null);
    }


}
