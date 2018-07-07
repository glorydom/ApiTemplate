package com.huiyi.dao.externalMapper;

import com.huiyi.dao.*;
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
    List<ExternalSales> getSalesByCompany(@Param("company")String company);

    @Select("select * from External_Sales where SALES = #{sales}")
    @Results({@Result(property="COMPANY",column="COMPANY",javaType=String.class),
            @Result(property="SALES",column="SALES",javaType=String.class),
    })
    List<ExternalSales> getCompanyBySales(@Param("sales")String sales);


    @Select("select * from JCI_ORDER_VOICE where GSMC = #{company}")
    @Results({@Result(property="GSMC",column="GSMC",javaType=String.class),
            @Result(property="NSR",column="NSR",javaType=String.class),
            @Result(property="DZ",column="DZ",javaType=String.class),
            @Result(property="DH",column="DH",javaType=String.class),
            @Result(property="KHH",column="KHH",javaType=String.class),
            @Result(property="ZH",column="ZH",javaType=String.class)
    })
    Invoice getCompanyInvoiceInfo(@Param("company")String company);


    @Insert(" insert into CZH values (#{ID,jdbcType=INTEGER}, #{NO,jdbcType=VARCHAR}, #{L_NO,jdbcType=VARCHAR}, " +
            "#{COMPANY,jdbcType=VARCHAR}, #{COMPANY_EN,jdbcType=VARCHAR}, #{PERSON,jdbcType=VARCHAR}, " +
            "#{PERSON_EN,jdbcType=VARCHAR}, #{ZHIWU,jdbcType=VARCHAR}, #{ZHIWU_EN,jdbcType=VARCHAR}, " +
            "#{TEL,jdbcType=VARCHAR}, #{FAX,jdbcType=VARCHAR}, #{MOBILE,jdbcType=VARCHAR}, #{LXDH,jdbcType=VARCHAR}, " +
            "#{EMAIL,jdbcType=VARCHAR}, #{SFJB,jdbcType=VARCHAR}, #{SFXC,jdbcType=VARCHAR}, #{XCFF,jdbcType=VARCHAR}, " +
            "#{CM,jdbcType=VARCHAR}, #{SM,jdbcType=VARCHAR}, #{FM,jdbcType=VARCHAR}, #{SFHY,jdbcType=VARCHAR}, " +
            "#{CASH,jdbcType=INTEGER}, #{SFDK,jdbcType=VARCHAR}, #{SFCJWY,jdbcType=VARCHAR}, #{WYZW,jdbcType=VARCHAR}, " +
            "#{HOTEL,jdbcType=VARCHAR}, #{ORDER_NO,jdbcType=VARCHAR}, #{SFQD,jdbcType=VARCHAR}, #{SFLQZL,jdbcType=VARCHAR}, " +
            "#{SCZT,jdbcType=VARCHAR}, #{RE_DATE,jdbcType=DATE}, #{IF_DEL,jdbcType=VARCHAR}, #{NOTE,jdbcType=VARCHAR}, " +
            "#{CODE,jdbcType=VARCHAR}, #{LW,jdbcType=VARCHAR}, #{PASS,jdbcType=VARCHAR}, #{WXH,jdbcType=VARCHAR}, " +
            "#{PIC,jdbcType=VARCHAR}, #{YJDZ,jdbcType=VARCHAR}, #{CG,jdbcType=VARCHAR}, #{JDNO,jdbcType=VARCHAR}, #{TJ,jdbcType=VARCHAR})")
    int insertIntoCZH(CZH czh);


    @Delete("delete from CZH where NO = #{NO}")
    int deleteMeetingFee(@Param("NO")String NO);

    @Delete("delete from JCI_ORDER_HOTEL where NO = #{NO}")
    int deleteHotelFee(@Param("NO")String NO);


    @Select("select * from CZH where MOBILE = #{phoneNumber}")
    CZH getByPhone(@Param("phoneNumber")String phoneNumber);

    @Select("select * from CZH where NO = #{NO}")
    CZH getCZHOrderByOrderno(@Param("NO")String NO);

    @Select("select * from CZH")
    List<CZH> getAllCzh();

    @Select("select * from CZH where COMPANY = #{company}")
    List<CZH> getAllCzh(@Param("company") String company);

    @Select("select * from JCI_ORDER where NO not in " +
            "(select NO from CZH where SFDK = '是')")
    List<JCI_ORDER> getAllUnpaidOrders();

    @Update("update CZH set SFDK = '否' where NO = #{NO}")
    int updateSFDKinCZHasFalse(@Param("NO")String NO);


    @Select("select * from JCI_ORDER_HOTEL where NO = #{NO}")
    JCI_ORDER_HOTEL getHotelOrderByOrderno(@Param("NO")String NO);

    @Update("update JCI_ORDER_HOTEL set ST= #{st} where NO= #{NO}")
    int updateHotelOrder(@Param("st") String st, @Param("NO")String NO);

}
