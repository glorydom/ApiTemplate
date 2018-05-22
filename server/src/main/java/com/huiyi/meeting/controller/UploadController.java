package com.huiyi.meeting.controller;

import com.dto.huiyi.meeting.util.Constants;
import com.zheng.common.base.BaseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.Date;

@Controller
@RequestMapping("/file")
@Api(value = "上传文件", description = "文件上传统一管理，上传的文件部分类型")
public class UploadController {

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

    @RequestMapping("download")
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
        String path = classPath.substring(0, classPath.indexOf("server")) + "upload";
        return path;
    }

}
