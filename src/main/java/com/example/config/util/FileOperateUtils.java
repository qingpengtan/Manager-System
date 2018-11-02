package com.example.config.util;

import com.example.config.exception.GlobalException;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

public class FileOperateUtils {

    public static String UploadFile(String basePath, MultipartFile file, String imgNames, String fileType) throws Exception{
        Calendar calendar = Calendar.getInstance();
        String year = calendar.get(Calendar.YEAR)+"";
        String month = (calendar.get(Calendar.MONTH)+1)+"";
        String date = calendar.get(Calendar.DAY_OF_MONTH)+"";

        String imgName =  year+month+date + "&" + UUIDUtil.uuid();
        String contentType = file.getContentType();
        String suffix = FileOperateUtils.fileType(contentType);
//        String suffix = file.getOriginalFilename();
//        if(!StringUtils.isEmpty(imgNames)){
//            suffix = imgNames;
//        }
//        suffix = suffix.replace("-", "");
//        suffix = suffix.replace("%", "");
//        suffix = suffix.replace(" ", "");
        File dir = new File(basePath);
        File targetFile = new File(basePath + imgName + suffix);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        OutputStream outputStream = new FileOutputStream(targetFile);
        IOUtils.copy(file.getInputStream(), outputStream);
//        myfile.transferTo(file);
//        byte b[] = new byte[1024];
//        int n;
//        while(( n =file.getInputStream().read(b)) != -1){
//            outputStream.write(b, 0, n);
//        }
//        outputStream.close();
        return "http://119.29.230.48/ROO/upload/"+fileType+"/" + imgName + suffix;
    }


    public static String fileType(String type){
        HashMap<String,String> typeMap = new HashMap<String, String>();
        typeMap.put("audio/mp3",".mp3");
        typeMap.put("image/jpeg",".jpg");
        typeMap.put("image/png",".png");
        typeMap.put("image/gif",".gif");

        Iterator<Map.Entry<String, String>> entries = typeMap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, String> entry = entries.next();
            if(entry.getKey().equals(type)){
                return entry.getValue();
            }
        }
        throw new GlobalException(CodeMsg.UPLOAD_FAIL);
    }

//    下载文件
    public static void Dowmload(HttpServletResponse resp, String basePath, String fileName) throws Exception{

        File targetFile = new File(basePath + fileName);
//        targetFile.delete();
        resp.setContentType("application/x-download");
        //设置头信息
        resp.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
        InputStream inputStream = new FileInputStream(targetFile);
        OutputStream outputStream = resp.getOutputStream();
        IOUtils.copy(inputStream, outputStream);
        outputStream.flush();

//        byte b[] = new byte[1024];
//        int n ;
//        while((n = inputStream.read(b)) != -1){
//            outputStream.write(b,0,n);
//        }
//        //关闭流、释放资源
//        outputStream.close();
//        inputStream.close();
    }
}
