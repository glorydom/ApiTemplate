package com.huiyi.meeting.service;

import com.huiyi.dao.CZH;
import com.huiyi.dao.ExternalSales;
import com.huiyi.dao.externalMapper.ExternalMeetingParticipantMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
/**
 * 这个代表了与会嘉宾相关的操作
 */
public class CZHService {
    private static Logger LOGGER = LoggerFactory.getLogger(CZHService.class);
    @Autowired
    private ExternalMeetingParticipantMapper externalMeetingParticipantMapper;

    /**
     * 因为每次会议之前，数据库会被手动清空，所以不用传meetingId过来
     * @param saleMan
     * @return
     */
    public List<CZH> getParticipants(String saleMan){
        List<CZH> list = externalMeetingParticipantMapper.getAllCzh();
        if(null == saleMan)
            return list;

        List<ExternalSales> externalSales = externalMeetingParticipantMapper.getCompanyBySales(saleMan);

        List<CZH> participantsInChargeBySaleman = new ArrayList<>();
        for(ExternalSales es:externalSales){
            String company = es.getCOMPANY();
            participantsInChargeBySaleman.addAll(externalMeetingParticipantMapper.getAllCzh(company));
        }

        return participantsInChargeBySaleman;
    }
}
