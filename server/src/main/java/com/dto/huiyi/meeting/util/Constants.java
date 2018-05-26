package com.dto.huiyi.meeting.util;

import com.zheng.common.util.PropertiesFileUtil;

public class Constants {
    public static final String CHQSURL = PropertiesFileUtil.getInstance().get("chqsurl");
//            "http://localhost:8080/chunhuaqiushi/";



    public static final int SUCCESS_CODE = 200;
    public static final int ERROR_CODE = 500;
    public static final int ACCESS_ERROR = 430;
}
