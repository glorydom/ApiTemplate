package com.huiyi.meeting.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import com.huiyi.dao.ExternalMeetingParticipant;
import com.huiyi.dao.ExternalSales;
import com.huiyi.dao.externalMapper.ExternalMeetingParticipantMapper;
import com.huiyi.meeting.dao.model.*;
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
import com.huiyi.meeting.rpc.api.MeetingMeetingService;
import com.huiyi.meeting.rpc.api.MeetingParticipantService;
import com.huiyi.meeting.rpc.api.MeetingRegistService;

@Service
public class MeetingRegisterService {

	private static Logger LOGGER = LoggerFactory.getLogger(MeetingRegisterService.class);
	
	@Autowired
	private MeetingMeetingService meetingMeetingService;
	@Autowired
	private MeetingParticipantService meetingParticipantService;
	@Autowired
	private MeetingRegistService meetingRegistService;
	@Autowired
	private ExternalMeetingParticipantMapper externalMeetingParticipantMapper;
	
	
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

	public List<ComparisonResultDto> reconsile(List<MeetingStatement> statements, List<ExternalMeetingParticipant> externalMeetingParticipants, List<String> companies) {
		List<ComparisonResultDto> comparisonResultDtos = new ArrayList<>();
		if(statements == null || statements.size() ==0){
			return comparisonResultDtos;
		}

		Set<String> companyies = new HashSet<>();
		for(MeetingStatement statement:statements){
			companyies.add(statement.getCompanyname());
		} // 统计所有的公司

		for(String company: companyies){
			ComparisonResultDto comparisonResultDto = new ComparisonResultDto();
			comparisonResultDto.setCompanyName(company);
			float participantShoulPay = this.sumParticipantRegistFee(externalMeetingParticipants, company);
			comparisonResultDto.setParticipantFeeTotal(participantShoulPay);
			float bankStaementTotal = this.sumStatementbyCompany(statements, company);
			comparisonResultDto.setStatementTotal(bankStaementTotal);
			if(participantShoulPay == bankStaementTotal)
				comparisonResultDto.setMatch(true);
			else
				comparisonResultDto.setMatch(false);
			List<MeetingStatement> company_statements = new ArrayList<>();
			for(MeetingStatement statement:statements){
				if(statement.getCompanyname().equalsIgnoreCase(company))
					company_statements.add(statement);
			}
			comparisonResultDto.setStatements(company_statements);

			List<ExternalMeetingParticipant> company_participants = new ArrayList<>();
			for(ExternalMeetingParticipant participant:externalMeetingParticipants){
				if(participant.getCompanyName().equalsIgnoreCase(company))
					company_participants.add(participant);
			}
			comparisonResultDto.setParticipants(company_participants);
			comparisonResultDtos.add(comparisonResultDto);
		}

		//如果有公司需要排除， 就放这里
		if(companies != null){
			for(ComparisonResultDto dto:comparisonResultDtos){
				boolean contains = companies.contains(dto.getCompanyName());
				if(!contains){
					comparisonResultDtos.remove(dto);
				}
			}
		}
		return comparisonResultDtos;
	}

	private float sumStatementbyCompany(List<MeetingStatement> statements, String company){
		float result = 0;
		for(MeetingStatement statement:statements){
			if(company.equalsIgnoreCase(statement.getCompanyname()))
				result += statement.getFee();
		}

		return result;
	}

	private float sumParticipantRegistFee(List<ExternalMeetingParticipant> participants, String company){

		float result = 0;
		for(ExternalMeetingParticipant participant:participants){
			if(company.equalsIgnoreCase(participant.getCompanyName()))
				result += participant.getFee();
		}
		return result;
	}

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
			crd.setMatch(companyMap.get(cn)==0);
			list.add(crd);
			LOGGER.debug(cn+" match? "+crd.isMatch());
		}
		return list;
	}


	public List<ExternalMeetingParticipant> convertToExternalMeetingParticipants(List<MeetingParticipant> participants){

		List<ExternalMeetingParticipant> externalMeetingParticipants = new ArrayList<>();
		for(MeetingParticipant participant : participants){
			ExternalMeetingParticipant externalMeetingParticipant = new ExternalMeetingParticipant();
			externalMeetingParticipant.setCompanyName(participant.getCompany());
			externalMeetingParticipant.setParticipantName(participant.getName());
			externalMeetingParticipant.setFee(participant.getMeetingfee());
			externalMeetingParticipant.setRegistTime(participant.getMeetingregistertime());
			externalMeetingParticipants.add(externalMeetingParticipant);
		}
		return externalMeetingParticipants;
	}


	public List<String> getSalemanForThisCompany(String company){
		List<ExternalSales> externalSales = externalMeetingParticipantMapper.getSalesByCompany(company);
		List<String> sales = new ArrayList<String>();
		for(ExternalSales sale:externalSales){
			sales.add(sale.getSALES());
		}

		return sales;
	}

	public List<String> getCompanyBySaleman(String saleMan){
		List<ExternalSales> externalSales = externalMeetingParticipantMapper.getCompanyBySales(saleMan);
		List<String> companies = new ArrayList<String>();
		for(ExternalSales sale:externalSales){
			companies.add(sale.getCOMPANY());
		}
		return companies;
	}

}
