package com.huicong.Test.dao.mapper;

import com.huicong.Test.dao.model.TestCopy;
import com.huicong.Test.dao.model.TestCopyExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TestCopyMapper {
    long countByExample(TestCopyExample example);

    int deleteByExample(TestCopyExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TestCopy record);

    int insertSelective(TestCopy record);

    List<TestCopy> selectByExample(TestCopyExample example);

    TestCopy selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TestCopy record, @Param("example") TestCopyExample example);

    int updateByExample(@Param("record") TestCopy record, @Param("example") TestCopyExample example);

    int updateByPrimaryKeySelective(TestCopy record);

    int updateByPrimaryKey(TestCopy record);
}