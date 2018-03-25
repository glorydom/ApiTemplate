package com.huicong.Test.rpc.service.impl;

import com.zheng.common.annotation.BaseService;
import com.zheng.common.base.BaseServiceImpl;
import com.huicong.Test.dao.mapper.TestCopyMapper;
import com.huicong.Test.dao.model.TestCopy;
import com.huicong.Test.dao.model.TestCopyExample;
import com.huicong.Test.rpc.api.TestCopyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* TestCopyService实现
* Created by shuzheng on 2018/3/19.
*/
@Service
@Transactional
@BaseService
public class TestCopyServiceImpl extends BaseServiceImpl<TestCopyMapper, TestCopy, TestCopyExample> implements TestCopyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestCopyServiceImpl.class);

    @Autowired
    TestCopyMapper testCopyMapper;

}