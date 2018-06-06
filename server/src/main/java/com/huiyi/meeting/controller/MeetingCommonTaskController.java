package com.huiyi.meeting.controller;

import com.dto.huiyi.meeting.entity.commonTaskDto.CommonTaskCompleteParameter;
import com.dto.huiyi.meeting.entity.commonTaskDto.CommonTaskStartParameter;
import com.dto.huiyi.meeting.util.Constants;
import com.huiyi.meeting.dao.model.MeetingCommonTask;
import com.zheng.common.base.BaseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/chqs/commonTask")
@Api(value = "通用任务管理", description = "对通用任务的处理： 启动，关闭，执行历史查询")
public class MeetingCommonTaskController extends BaseController {

    @ApiOperation(value = "开启任务")
//    @RequiresPermissions("chqs:meeting:manage")
    @RequestMapping(value = "start", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult startTask(@RequestBody MeetingCommonTask meetingCommonTask){

        return  new BaseResult(Constants.SUCCESS_CODE, "", null);
    }

    @ApiOperation(value = "完成任务")
    @RequestMapping(value = "complete", method = RequestMethod.POST)
    @ResponseBody
    /**
     * 该方法不同于普通的任务完成，该方法要主要检测 完成者是否是任务的拥有者，如果是就结束该task,如果不是就要将任务从新分给任务拥有者
     */
    public BaseResult partialCompleteTask(@RequestBody CommonTaskCompleteParameter commonTaskCompleteParameter){


        return  new BaseResult(Constants.SUCCESS_CODE, "", null);
    }


}
