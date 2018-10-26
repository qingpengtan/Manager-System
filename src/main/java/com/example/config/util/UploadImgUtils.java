package com.example.config.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class UploadImgUtils {

    public static String UploadImg(String basePath, MultipartFile file,String imgNames,String fileType) throws Exception{
        Calendar calendar = Calendar.getInstance();
        String year = calendar.get(Calendar.YEAR)+"";
        String month = (calendar.get(Calendar.MONTH)+1)+"";
        String date = calendar.get(Calendar.DAY_OF_MONTH)+"";

        String imgName =  year+month+date + "&" + UUIDUtil.uuid();
        String suffix = file.getOriginalFilename();
        if(!StringUtils.isEmpty(imgNames)){
            suffix = imgNames;
        }
        suffix = suffix.replace("-", "");
        suffix = suffix.replace("%", "");
        suffix = suffix.replace(" ", "");
        File dir = new File(basePath);
        File targetFile = new File(basePath + imgName + suffix);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        OutputStream outputStream = new FileOutputStream(targetFile);
        //        myfile.transferTo(file);
        IOUtils.copy(file.getInputStream(), outputStream);

        return "http://119.29.230.48/ROO/upload/"+fileType+"/" + imgName + suffix;
    }
}
