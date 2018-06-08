package com.dto.huiyi.meeting.util;

import com.zheng.common.util.PropertiesFileUtil;

public class Constants {
    public static final String CHQSURL = PropertiesFileUtil.getInstance().get("chqsurl");
//            "http://localhost:8080/chunhuaqiushi/";


    public static final int SUCCESS_CODE = 200;
    public static final int ERROR_CODE = 500;
    public static final int ACCESS_ERROR = 430;

    // 通用任务参数
    public static final String COMMON_TASK_OWNER = "COMMON_TASK_OWNER";
    public static final String COMMON_TASK_ASSIGNEE = "COMMON_TASK_ASSIGNEE";
    public static final String COMMON_TASK_VIEWER = "COMMON_TASK_VIEWER";
    public static final String COMMON_TASK_NEED_APPROVAL = "COMMON_TASK_NEED_APPROVAL";
    public static final String COMMON_TASK_NEED_APPROVAL_POSITIVE = "YES";
    public static final String COMMON_TASK_NEED_APPROVAL_NEGATIVE = "NO";
    public static final String COMMON_TASK_APPROVER = "COMMON_TASK_APPROVER";
    public static final String COMMON_TASK_AUDIT_RESULT = "COMMON_TASK_AUDIT_RESULT";
    public static final String COMMON_TASK_AUDIT_RESULT_PASS = "YES";
    public static final String COMMON_TASK_AUDIT_RESULT_FAIL = "NO";
}
