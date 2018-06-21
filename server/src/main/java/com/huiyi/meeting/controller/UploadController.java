package com.huiyi.meeting.controller;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.dto.huiyi.meeting.util.Constants;
import com.huiyi.meeting.dao.model.MeetingRegist;
import com.huiyi.meeting.rpc.api.MeetingRegistService;
import com.zheng.common.base.BaseResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/file")
@Api(value = "上传文件", description = "文件上传统一管理，上传的文件部分类型")
public class UploadController {
	private static Logger LOGGER = LoggerFactory.getLogger(UploadController.class);
	
	@Autowired
	private MeetingRegistService meetingRegistService;
	
	@RequestMapping(value = "uploadBankSheet", method = RequestMethod.POST)
    @ApiOperation(value = "上传银行账单文件")
    @ResponseBody
    public BaseResult uploadBankSheet(@RequestParam("file") MultipartFile file) throws IOException {
        String path = getFileUploadFolder();
        String originalFilename = file.getOriginalFilename();
        String fileName = new Date().getTime()+ "_"+ originalFilename;
        
//        XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
//        XSSFSheet worksheet = workbook.getSheetAt(0);
//        int rownum = worksheet.getLastRowNum();
//        for(int i=1;i<rownum;i++) {
//        	XSSFRow row = worksheet.getRow(i);
//        	LOGGER.debug(row.getCell(0).toString());
//        }
//        workbook.close();
        
        File dir = new File(path, fileName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        //MultipartFile自带的解析方法
        file.transferTo(dir);
        LOGGER.debug(path+File.separator+fileName);
        
        MeetingRegist record = new MeetingRegist();
        record.setCreationtimestamp(new Date().getTime());
        record.setFeesheetexcel(path+File.separator+fileName);
        record.setIsinvoiced("NO");
        int ret = meetingRegistService.insert(record);
        
        return new BaseResult(Constants.SUCCESS_CODE, fileName, ret);
    }
	
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    @ApiOperation(value = "上传文件")
    @ResponseBody
    public BaseResult upload(HttpServletRequest request,
                             @RequestParam("file") MultipartFile file) throws IOException {
        String path = getFileUploadFolder();
        String originalFilename = file.getOriginalFilename();
        String fileName = new Date().getTime()+ "_"+ originalFilename;
        File dir = new File(path, fileName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        //MultipartFile自带的解析方法
        file.transferTo(dir);
        return new BaseResult(Constants.SUCCESS_CODE, fileName, null);
    }

    @RequestMapping(value="download", method = RequestMethod.GET)
    @ApiOperation(value = "下载文件")
    public ResponseEntity<byte[]> download(String fileName) throws IOException {
        String dfileName = new String(fileName.getBytes("utf-8"), "utf-8");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", dfileName);
        String path= getFileUploadFolder();
        File file = new File(path, dfileName);
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
    }

    private String getFileUploadFolder(){
        String classPath = this.getClass().getClassLoader().getResource("").getPath();
        Calendar cal = Calendar.getInstance();
		String subFolders = File.separator + cal.get(Calendar.YEAR) + File.separator + cal.get(Calendar.MONTH) + File.separator + cal.get(Calendar.DAY_OF_MONTH);
        String path = classPath.substring(0, classPath.indexOf("server")) + "upload"+subFolders;
        return path;
    }

}
