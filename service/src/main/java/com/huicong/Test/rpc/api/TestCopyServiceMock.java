package com.huicong.Test.rpc.api;

import com.zheng.common.base.BaseServiceMock;
import com.huicong.Test.dao.mapper.TestCopyMapper;
import com.huicong.Test.dao.model.TestCopy;
import com.huicong.Test.dao.model.TestCopyExample;

/**
* 降级实现TestCopyService接口
* Created by shuzheng on 2018/3/19.
*/
public class TestCopyServiceMock extends BaseServiceMock<TestCopyMapper, TestCopy, TestCopyExample> implements TestCopyService {

}
