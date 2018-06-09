package com.huiyi.meeting.controller;

import com.dto.huiyi.meeting.util.Constants;
import com.huicong.upms.dao.model.UpmsUser;
import com.huicong.upms.dao.model.UpmsUserExample;
import com.huicong.upms.rpc.api.UpmsUserService;
import com.zheng.common.base.BaseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/chqs/staff")
@Api(value = "会议工作人员管理", description = "对会议工作人员进行查询，注册，删除，更新的操作")
public class ChqsStaffController {

    @Autowired
    private UpmsUserService upmsUserService;

    @ApiOperation(value = "读取会议工作人员列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Object listusers() {
        List<UpmsUser> users = upmsUserService.selectByExample(new UpmsUserExample());
        return new BaseResult(Constants.SUCCESS_CODE, "success", users);
    }
}
