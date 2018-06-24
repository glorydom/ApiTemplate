package com.huiyi.meeting.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;


@Service
public class FileUploadService {

    /**
     * 将文件保存在该文件夹下
     * @param file
     * @param folder
     */
    public String saveFile(MultipartFile file, String folder){
        String path = getFileUploadBaseFolder();
        String originalFilename = file.getOriginalFilename();
        String fileName = new Date().getTime()+ "_"+ originalFilename;
        String pathDest = path + File.separator + folder;
        File dir = new File(pathDest, fileName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        //MultipartFile自带的解析方法
        try {
            file.transferTo(dir);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return fileName;
    }

    /**
     * 该方法仅仅返回该文件的全路径+文件名
     * @param subFolder  该方法应该参考 saveFile的方法。 这个路径必须跟saveFile方法传进来的路径是一样的，否则可能找不到文件
     * @param fileName
     * @return
     */
    public String getFile(String subFolder, String fileName){
        String basePath = getFileUploadBaseFolder();
        return basePath + File.separator + subFolder + File.separator + fileName;
    }


    private String getFileUploadBaseFolder(){
        String classPath = this.getClass().getClassLoader().getResource("").getPath();
        Calendar cal = Calendar.getInstance();
        String subFolders = File.separator + cal.get(Calendar.YEAR) + File.separator + cal.get(Calendar.MONTH) + File.separator + cal.get(Calendar.DAY_OF_MONTH) + File.separator ;
        String path = classPath.substring(0, classPath.indexOf("server")) + "upload"+ File.separator +subFolders;
        return path;
    }


}
