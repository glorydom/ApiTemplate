package com.huicong.upms.rpc.api;

import com.zheng.common.base.BaseService;
import com.huicong.upms.dao.model.UpmsUserRole;
import com.huicong.upms.dao.model.UpmsUserRoleExample;

/**
* UpmsUserRoleService接口
* Created by shuzheng on 2018/3/12.
*/
public interface UpmsUserRoleService extends BaseService<UpmsUserRole, UpmsUserRoleExample> {

    /**
     * 用户角色
     * @param roleIds 角色ids
     * @param id 用户id
     * @return
     */
    int role(String[] roleIds, int id);
}