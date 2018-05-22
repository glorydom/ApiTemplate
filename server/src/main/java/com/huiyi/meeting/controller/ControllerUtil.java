package com.huiyi.meeting.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.dto.huiyi.meeting.entity.CHQSResult;
import com.dto.huiyi.meeting.entity.meetingDto.ProcessStartParameter;
import com.dto.huiyi.meeting.util.Constants;
import com.huiyi.service.HttpClientService;
import com.zheng.common.base.BaseResult;

import java.io.IOException;
import java.util.Map;

import static com.dto.huiyi.meeting.util.Constants.ERROR_CODE;
import static com.dto.huiyi.meeting.util.Constants.SUCCESS_CODE;

public class ControllerUtil {

    public static BaseResult startNewBussinessProcess(Object entity, int bussinessId, Map<String, Object> parameters,
                                                      HttpClientService httpClientService){
        String process_id = entity.getClass().getSimpleName();
        String bussiness_key = process_id + "." + bussinessId;
        String chqsUrl = Constants.CHQSURL + "process/start";
        CHQSResult result = null;
        // prepare the parameters
        // date format: 2011-03-11T12:13:14
        ProcessStartParameter processStartParameter = new ProcessStartParameter();
        processStartParameter.setBussinessId(bussiness_key);
        processStartParameter.setParameters(parameters);
        processStartParameter.setProcessId(process_id);
        try {
            result = httpClientService.postCHQSJson(chqsUrl, JSON.toJSONString(processStartParameter), new TypeReference<CHQSResult>(){});
        } catch (IOException e) {
            e.printStackTrace();
            return new BaseResult(ERROR_CODE, "system error", null);
        }

        if(result.code == Constants.SUCCESS_CODE){
            return new BaseResult(SUCCESS_CODE, "success", entity);
        }

        return new BaseResult(ERROR_CODE, "system error", null);
    }


    public static BaseResult completeTask(){
        String completeUrl = Constants.CHQSURL + "task/complete";

        return new BaseResult(SUCCESS_CODE, "success", null);
    }
}
