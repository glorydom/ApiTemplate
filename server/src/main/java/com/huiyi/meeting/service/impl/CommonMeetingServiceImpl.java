package com.huiyi.meeting.service.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.history.HistoricProcessInstance;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dto.huiyi.meeting.entity.register.ComparisonResultDto;
import com.huiyi.meeting.dao.model.MeetingMeeting;
import com.huiyi.meeting.dao.model.MeetingParticipant;
import com.huiyi.meeting.dao.model.MeetingParticipantExample;
import com.huiyi.meeting.dao.model.MeetingRegist;
import com.huiyi.meeting.rpc.api.MeetingMeetingService;
import com.huiyi.meeting.rpc.api.MeetingParticipantService;
import com.huiyi.meeting.rpc.api.MeetingRegistService;
import com.huiyi.meeting.service.CommonMeetingService;

@Service
public class CommonMeetingServiceImpl implements CommonMeetingService {

	private static Logger LOGGER = LoggerFactory.getLogger(CommonMeetingServiceImpl.class);
	
	@Autowired
	private MeetingMeetingService meetingMeetingService;
	@Autowired
	private MeetingParticipantService meetingParticipantService;
	@Autowired
	private MeetingRegistService meetingRegistService;
	
	
	@Override
	public String getObjectDescription(HistoricProcessInstance pi) {
		// TODO Auto-generated method stub
		String[] businessKeyArray = pi.getBusinessKey().split("_");
		if(businessKeyArray.length != 2) {
			return "无法解析businessKey或未指定"+pi.getBusinessKey();
		}
		String objectType = businessKeyArray[0];
		if(!StringUtils.isNumeric(businessKeyArray[1])) {
			return "业务对象ID异常"+businessKeyArray[1];
		}
		int objectId = Integer.parseInt(businessKeyArray[1]);
		LOGGER.debug("对象类型："+objectType +",对象ID："+objectId);
		if(objectType.equals(MeetingMeeting.class.getSimpleName())) {
			MeetingMeeting obj = meetingMeetingService.selectByPrimaryKey(objectId);
			return obj.toString();
		}
		else if(objectType.equals(MeetingParticipant.class.getSimpleName())) {
			MeetingParticipant obj = meetingParticipantService.selectByPrimaryKey(objectId);
			return obj.toString();
		}
		else if(objectType.equals(MeetingRegist.class.getSimpleName())) {
			MeetingRegist obj = meetingRegistService.selectByPrimaryKey(objectId);
			return obj.toString();
		}
		return "不支持的businessKey"+pi.getBusinessKey();
	}


	@Override
	public List<ComparisonResultDto> reconsile(String filepath) {
		// TODO Auto-generated method stub';
		double per = 1000.0;
		Map<String,List<MeetingParticipant>> indexes = new HashMap<>();
		Map<String,Integer> companyMap = new HashMap<>();
		try {
			InputStream is = new FileInputStream(filepath);
			XSSFWorkbook workbook = new XSSFWorkbook(is);
			XSSFSheet worksheet = workbook.getSheetAt(0);
			int rownum = worksheet.getLastRowNum();
			for(int i=1;i<rownum;i++) {
				XSSFRow row = worksheet.getRow(i);
				if(row.getCell(1)==null)
					continue;
				String companyName = row.getCell(0).toString();
				indexes.put(companyName, new ArrayList<MeetingParticipant>());
				companyMap.put(companyName, (int)(row.getCell(1).getNumericCellValue()/per));
			}
			workbook.close();
		}catch(IOException ioe) {
			LOGGER.error(ioe.getMessage());
		}
		List<MeetingParticipant> mpList = meetingParticipantService.selectByExample(new MeetingParticipantExample());
		for(MeetingParticipant mp : mpList) {
			List<MeetingParticipant> vList = indexes.get(mp.getCompany());
			if(vList == null) {
				vList = new ArrayList<>();
				indexes.put(mp.getCompany(), vList);
			}
			vList.add(mp);
			Integer left = companyMap.get(mp.getCompany());
			companyMap.put(mp.getCompany(),left==null?-1:left-1);
		}
		List<ComparisonResultDto> list = new ArrayList<>();
		for(String cn : indexes.keySet()) {
			ComparisonResultDto crd = new ComparisonResultDto();
			crd.setCompanyName(cn);
			crd.setParticipants(indexes.get(cn));
			crd.setMatch(companyMap.get(cn)==0);
			list.add(crd);
			LOGGER.debug(cn+" match? "+crd.isMatch());
		}
		return list;
	}

}
