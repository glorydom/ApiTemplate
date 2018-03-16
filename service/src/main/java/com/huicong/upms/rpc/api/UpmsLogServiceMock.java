package com.huicong.upms.rpc.api;

import com.zheng.common.base.BaseServiceMock;
import com.huicong.upms.dao.mapper.UpmsLogMapper;
import com.huicong.upms.dao.model.UpmsLog;
import com.huicong.upms.dao.model.UpmsLogExample;

/**
* 降级实现UpmsLogService接口
* Created by shuzheng on 2018/3/12.
*/
public class UpmsLogServiceMock extends BaseServiceMock<UpmsLogMapper, UpmsLog, UpmsLogExample> implements UpmsLogService {

}
