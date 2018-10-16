package com.example.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.example.config.UploadProperties;
import com.example.config.redis.RedisService;
import com.example.config.redis.UserKey;
import com.example.config.util.CodeMsg;
import com.example.config.util.Result;
import com.example.config.util.UploadImgUtils;
import com.example.controller.system.UserControll;
import com.example.entity.UserAccount;
import com.example.service.impl.UserServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/upload")
public class UploadControl {

    private final static Logger log = LoggerFactory.getLogger(UploadControl.class);

    @Resource
    private UploadProperties uploadProperties;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private RedisService redisService;

    @RequestMapping(value = "/image", method = RequestMethod.POST)
    public Result upload(@RequestParam("img") MultipartFile file, HttpServletRequest request) {
        String basePath = uploadProperties.getBasePath();
        String imgNames = request.getParameter("imgeFileName");
        try {
            String path = UploadImgUtils.UploadImg(basePath,file,imgNames);
            return  Result.success(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.error(CodeMsg.UPLOAD_FAIL);
    }

    @RequestMapping(value = "/uploadAvater", method = RequestMethod.POST)
    public Result uploadAvater(@RequestParam("img") MultipartFile file, HttpServletRequest request) {
        String basePath = uploadProperties.getBasePath();
        String imgNames = request.getParameter("imgeFileName");
        try {
            String path = UploadImgUtils.UploadImg(basePath,file,imgNames);
            String token = request.getHeader("token");
            UserAccount user = redisService.get(UserKey.token, token, UserAccount.class);
            UserAccount userAccount = userService.selectOne(new EntityWrapper<UserAccount>().eq("user_phone",user.getUserPhone()));
//            userAccount.setUserPic(path);
            userService.updateById(userAccount);
            return  Result.success(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.error(CodeMsg.UPLOAD_FAIL);
    }




    @RequestMapping(value = "/baseImg", method = RequestMethod.POST)
    public Result upload( HttpServletRequest request) {
        String basePath = uploadProperties.getBasePath();
        String img = request.getParameter("baseImg");
        int size = imageSize(img);
        if(size > 500000){
            return Result.error(CodeMsg.UPLOAD_FAIL);
        }
        String imageName = request.getParameter("imageName");
        img = img.split(",")[1];

        String imgName = new Date().getTime() + "&" + UUID.randomUUID();
        File dir = new File(basePath);
        File targetFile = new File(basePath + imgName + imageName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            OutputStream outputStream = new FileOutputStream(targetFile);
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] b = decoder.decodeBuffer(img);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {//调整异常数据
                    b[i] += 256;
                }
            }
            outputStream.write(b);
            outputStream.flush();
            outputStream.close();
            return Result.success("http://119.29.230.48/ROO/upload/image/" + imgName + imgName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.error(CodeMsg.UPLOAD_FAIL);
    }



    /**
     *通过图片base64流判断图片等于多少字节
     *image 图片流
     */
    public static Integer imageSize(String image){
        String str=image.substring(22); // 1.需要计算文件流大小，首先把头部的data:image/png;base64,（注意有逗号）去掉。
        Integer equalIndex= str.indexOf("=");//2.找到等号，把等号也去掉
        if(str.indexOf("=")>0) {
            str=str.substring(0, equalIndex);
        }
        Integer strLength=str.length();//3.原来的字符流大小，单位为字节
        Integer size=strLength-(strLength/8)*2;//4.计算后得到的文件流大小，单位为字节
        return size;
    }
}
