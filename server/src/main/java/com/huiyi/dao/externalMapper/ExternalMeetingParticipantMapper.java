package com.huiyi.dao.externalMapper;

import com.huiyi.dao.ExternalMeetingParticipant;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

public interface ExternalMeetingParticipantMapper {

    @Select("select * from MEETING_Participant where meetingRegisterTime >= #{registerTime}")
//    @ResultMap("ExternalMeetingParticipantMap")
    @Results({@Result(property="telephone",column="telephone",javaType=String.class),
             @Result(property="companyName",column="company",javaType=String.class),
             @Result(property="participantName",column="name",javaType=String.class),
             @Result(property="fee",column="meetingFee",javaType=Double.class),
             @Result(property="registTime",column="meetingRegisterTime",javaType=Date.class)
                 })
    /**
     * 获取注册时间比当前时间早的嘉宾
     */
    List<ExternalMeetingParticipant> getExternalMeetingParticipants(@Param("registerTime")Date registerTime);


}
