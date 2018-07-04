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


    @Select("select * from External_Sales where COMPANY = #{company}")
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


    @Insert(" insert into CZH values (#{ID,jdbcType=INTEGER}, #{NO,jdbcType=VARCHAR}, #{L_NO,jdbcType=VARCHAR}, " +
            "#{COMPANY,jdbcType=VARCHAR}, #{COMPANY_EN,jdbcType=VARCHAR}, #{PERSON,jdbcType=VARCHAR}, " +
            "#{PERSON_EN,jdbcType=VARCHAR}, #{ZHIWU,jdbcType=VARCHAR}, #{ZHIWU_EN,jdbcType=VARCHAR}, " +
            "#{TEL,jdbcType=VARCHAR}, #{FAX,jdbcType=VARCHAR}, #{MOBIL,jdbcType=VARCHAR}, #{LXDH,jdbcType=VARCHAR}, " +
            "#{EMAIL,jdbcType=VARCHAR}, #{SFJB,jdbcType=VARCHAR}, #{SFXC,jdbcType=VARCHAR}, #{XCFF,jdbcType=VARCHAR}, " +
            "#{CM,jdbcType=VARCHAR}, #{SM,jdbcType=VARCHAR}, #{FM,jdbcType=VARCHAR}, #{SFHY,jdbcType=VARCHAR}, " +
            "#{CASH,jdbcType=INTEGER}, #{SFDK,jdbcType=VARCHAR}, #{SFCJW,jdbcType=VARCHAR}, #{WYZW,jdbcType=VARCHAR}, " +
            "#{HOTEL,jdbcType=VARCHAR}, #{ORDER,jdbcType=VARCHAR}, #{SFQD,jdbcType=VARCHAR}, #{SFLQZ,jdbcType=VARCHAR}, " +
            "#{SCZT,jdbcType=VARCHAR}, #{RE_DATE,jdbcType=}, #{IF_DE,jdbcType=VARCHAR}, #{NOTE,jdbcType=VARCHAR}, " +
            "#{CODE,jdbcType=VARCHAR}, #{LW,jdbcType=VARCHAR}, #{PASS,jdbcType=VARCHAR}, #{WXH,jdbcType=VARCHAR}, " +
            "#{PIC,jdbcType=VARCHAR}, #{YJDZ,jdbcType=VARCHAR}, #{CG,jdbcType=VARCHAR}, #{JDNO,jdbcType=VARCHAR}, #{TJ,jdbcType=VARCHAR})")
    int confirmParticipantFee(CZH czh);


    @Select("select * from CZH where MOBILE = #{phoneNumber}")
    CZH getByPhone(String phoneNumber);

    @Select("select * from JCI_ORDER")
    List<JCI_ORDER> getAll();

}
