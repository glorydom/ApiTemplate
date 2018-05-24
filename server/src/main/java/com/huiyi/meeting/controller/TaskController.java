package com.huiyi.meeting.controller;

import com.alibaba.fastjson.TypeReference;
import com.dto.huiyi.meeting.entity.CHQSResult;
import com.dto.huiyi.meeting.util.Constants;
import com.huicong.upms.dao.model.UpmsUserExample;
import com.huicong.upms.rpc.api.UpmsUserService;
import com.huiyi.service.HttpClientService;
import com.zheng.common.base.BaseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dto.huiyi.meeting.util.Constants.ERROR_CODE;
import static com.dto.huiyi.meeting.util.Constants.SUCCESS_CODE;

@Controller
@RequestMapping("/chqs/task")
@Api(value = "任务管理", description = "对每个人的任务的管理")
@Transactional
public class TaskController {

    private String chqsUrlbase = Constants.CHQSURL + "task";

    @Autowired
    private UpmsUserService upmsUserService;

    @Autowired
    HttpClientService httpClientService;

    @ApiOperation(value = "查询本组的任务")
    @RequestMapping(value = "search/group", method = RequestMethod.GET)
    @ResponseBody
    public BaseResult searchMygroupTask() {
        String username = (String) SecurityUtils.getSubject().getPrincipal();

        CHQSResult result = null;
        String chqsUrl = chqsUrlbase + "/list/group/" + username;
        try {
            result = httpClientService.getCHQSData(chqsUrl, null, new TypeReference<CHQSResult>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            return new BaseResult(ERROR_CODE, "system error", null);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return new BaseResult(ERROR_CODE, "system error", null);
        }

        return new BaseResult(SUCCESS_CODE, "Success", result.getData());
    }

    @ApiOperation(value = "将某个任务分给自己，并开始执行")
    @RequestMapping(value = "claim/{taskId}", method = RequestMethod.GET)
    @ResponseBody
    public BaseResult claimTask(@PathVariable String taskId) {
        String myID = (String) SecurityUtils.getSubject().getPrincipal();
        CHQSResult result = null;
        Map<String, String> param = new HashMap<String, String>();
        String chqsUrl = chqsUrlbase + "/claim/" + myID + "/" + taskId;
        try {
            result = httpClientService.getCHQSData(chqsUrl, param, new TypeReference<CHQSResult>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            return new BaseResult(ERROR_CODE, "system error", null);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return new BaseResult(ERROR_CODE, "system error", null);
        }

        if(result.getCode() == SUCCESS_CODE){
            return new BaseResult(SUCCESS_CODE, "Success", result.getData());
        } else {
            return new BaseResult(ERROR_CODE, "System error", null);
        }

    }

    @ApiOperation(value = "查询本人的任务")
    @RequestMapping(value = "search/mine", method = RequestMethod.GET)
    @ResponseBody
    public BaseResult searchMyTask() {
        String myID = (String) SecurityUtils.getSubject().getPrincipal();
        CHQSResult result = null;
        Map<String, String> param = new HashMap<String, String>();
        String chqsUrl = chqsUrlbase + "/listBySingleUser/" + myID;
        try {
            result = httpClientService.getCHQSData(chqsUrl, param, new TypeReference<CHQSResult>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            return new BaseResult(ERROR_CODE, "system error", null);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return new BaseResult(ERROR_CODE, "system error", null);
        }
        return new BaseResult(SUCCESS_CODE, "Success", result.getData());
    }



//    @ApiOperation(value = "完成任务")
//    @RequestMapping(value = "complete", method = RequestMethod.POST)
//    @ResponseBody
//    public BaseResult completeMyTask(@RequestBody TaskCompleteDto completeDto) {
//        String chqsUrl = chqsUrlbase + "/complete";
//
//        try {
//            result = httpClientService.getCHQSData(chqsUrl, p, new TypeReference<CHQSResult>() {
//            });
//        } catch (IOException e) {
//            e.printStackTrace();
//            return new BaseResult(ERROR_CODE, "system error", null);
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//            return new BaseResult(ERROR_CODE, "system error", null);
//        }
//
//        if(null != result){
//            return new BaseResult(SUCCESS_CODE, "success", result.getData());
//        }else{
//            return new BaseResult(ERROR_CODE, "system error", null);
//        }
//    }



}
