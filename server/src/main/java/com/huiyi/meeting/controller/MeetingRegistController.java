package com.huiyi.meeting.controller;

import com.dto.huiyi.meeting.util.Constants;
import com.huiyi.meeting.dao.model.MeetingRegist;
import com.huiyi.meeting.dao.model.MeetingRoom;
import com.zheng.common.base.BaseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/chqs/regist")
@Api(value = "嘉宾注册", description = "对嘉宾注册相关事项的管理")
public class MeetingRegistController {

    @ApiOperation(value = "上传对账单")
    @RequestMapping(value = "uploadSheet", method = RequestMethod.POST)
    @ResponseBody
    /**
     * 财务人员上传来自银行的对账单 excel格式的， 有两个字段  公司 | 金额
     * 开启流程，并且完成第一个任务： 上传银行账单
     */
    public BaseResult uploadFile(@RequestBody MeetingRegist meetingRegist){
        //todo

        return new BaseResult(Constants.SUCCESS_CODE, "", null);
    }

    @ApiOperation(value = "会务款项比对")
    @RequestMapping(value = "compare/{taskId}", method = RequestMethod.GET)
    @ResponseBody
    /**
     * taskID是activiti的Taskid 根据taskId可以找到businessKey 从而找到MeetingRegist 读取Excel内容 将公司信息跟注册人员付款信息比对。
     */
    public BaseResult compare(@PathVariable String taskId){
        //todo

        return new BaseResult(Constants.SUCCESS_CODE, "", null);
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
