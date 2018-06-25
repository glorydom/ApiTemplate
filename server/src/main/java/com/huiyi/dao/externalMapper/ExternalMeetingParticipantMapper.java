package com.huiyi.dao.externalMapper;

import com.huiyi.dao.ExternalMeetingParticipant;
import com.huiyi.dao.ExternalSales;
import com.huiyi.dao.Invoice;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

public interface ExternalMeetingParticipantMapper {

//    @Select("select * from MEETING_Participant where meetingRegisterTime >= #{registerTime}")
    @Select("select * from External_participant")
    @Results({@Result(property="telephone",column="telephone",javaType=String.class),
             @Result(property="companyName",column="company",javaType=String.class),
             @Result(property="participantName",column="name",javaType=String.class),
             @Result(property="fee",column="meetingFee",javaType=Float.class),
             @Result(property="registTime",column="meetingRegisterTime",javaType=Date.class)
                 })
    /**
     * 获取注册时间比当前时间早的嘉宾
     */
    List<ExternalMeetingParticipant> getExternalMeetingParticipants(@Param("registerTime")Date registerTime);


    @Select("select * from External_participant where COMPANY = #{company}")
    @Results({@Result(property="COMPANY",column="COMPANY",javaType=String.class),
            @Result(property="SALES",column="SALES",javaType=String.class),
                })
    List<ExternalSales> getSalesByCompany(String company);

    @Select("select * from External_Sales where SALES = #{sales}")
    @Results({@Result(property="COMPANY",column="COMPANY",javaType=String.class),
            @Result(property="SALES",column="SALES",javaType=String.class),
    })
    List<ExternalSales> getCompanyBySales(String sales);

    @Select("select * from JCI_ORDER_VOICE where GSMC = #{company}")
    @Results({@Result(property="GSMC",column="GSMC",javaType=String.class),
            @Result(property="NSR",column="NSR",javaType=String.class),
            @Result(property="DZ",column="DZ",javaType=String.class),
            @Result(property="DH",column="DH",javaType=String.class),
            @Result(property="KHH",column="KHH",javaType=String.class),
            @Result(property="ZH",column="ZH",javaType=String.class)
    })
    Invoice getCompanyInvoiceInfo(String company);

}
