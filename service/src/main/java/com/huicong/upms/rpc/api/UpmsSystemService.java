package com.huicong.upms.rpc.api;

import com.zheng.common.base.BaseService;
import com.huicong.upms.dao.model.UpmsSystem;
import com.huicong.upms.dao.model.UpmsSystemExample;

/**
* UpmsSystemService接口
* Created by shuzheng on 2018/3/12.
*/
public interface UpmsSystemService extends BaseService<UpmsSystem, UpmsSystemExample> {

    /**
     * 根据name获取UpmsSystem
     * @param name
     * @return
     */
    UpmsSystem selectUpmsSystemByName(String name);
}