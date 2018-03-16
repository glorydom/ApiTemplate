package com.huicong.upms.rpc.api;

import com.zheng.common.base.BaseService;
import com.huicong.upms.dao.model.UpmsUser;
import com.huicong.upms.dao.model.UpmsUserExample;

/**
* UpmsUserService接口
* Created by shuzheng on 2018/3/12.
*/
public interface UpmsUserService extends BaseService<UpmsUser, UpmsUserExample> {

    UpmsUser createUser(UpmsUser upmsUser);
}