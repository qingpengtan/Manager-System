package com.example.controller.system;

import com.baomidou.mybatisplus.plugins.Page;
import com.example.config.UploadProperties;
import com.example.config.util.CodeMsg;
import com.example.config.util.Result;
import com.example.controller.IndexControl;
import com.example.entity.UserAccount;
import com.example.service.impl.UserServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping("/sys/user")
public class UserControll {

    private final static Logger log = LoggerFactory.getLogger(UserControll.class);

    @Autowired
    UserServiceImpl userService;

    @Resource
    private ResourceLoader resourceLoader;
    @Resource
    private UploadProperties uploadProperties;

    @RequestMapping(value = "/test", method = RequestMethod.POST)
//    public Result test(HttpServletRequest request,@RequestParam("img") MultipartFile myfil) throws IOException {
////        String filePath = request.getSession().getServletContext().getRealPath("imgupload/");
//        File file = new File("D:\\24.png");
//        OutputStream outputStream = new FileOutputStream(file);
////        myfile.transferTo(file);
//        IOUtils.copy(myfil.getInputStream(),outputStream);
//        return Result.success("D:\\24.png");
//    }
    public Result upload(@RequestParam("img") MultipartFile file, HttpServletRequest request) {
        String basePath = uploadProperties.getBasePath();
        String location = new Date().getTime() + "/";
        File targetFile = new File(basePath +file.getOriginalFilename());
//        if (!targetFile.exists()) {
//            targetFile.mkdirs();
//        }
            try {
                OutputStream outputStream = new FileOutputStream(targetFile);
//        myfile.transferTo(file);
                IOUtils.copy(file.getInputStream(), outputStream);
//                org.springframework.core.io.Resource resource =resourceLoader.getResource("file:" + Paths.get(uploadProperties.getBasePath() +"yy.png").toString());
//                return Result.success(resource.getFilename());
                return Result.success("http://119.29.230.48/ROO/upload/image/"+file.getOriginalFilename());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return Result.error(CodeMsg.UPLOAD_FAIL);
    }


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result login(HttpServletRequest request, HttpServletResponse response, UserAccount userAccount) {
        userAccount.setRoleId(3);
        Map map = userService.login(request, response, userAccount);
        return Result.success(map);
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Result List(HttpServletRequest request, HttpServletResponse response, UserAccount userAccount) {
        String pageN = request.getParameter("page");
        if (StringUtils.isEmpty(pageN)) {
            pageN = "1";
        }
        Page page = new Page();
        page.setCurrent(Integer.parseInt(pageN));
        page.setSize(10);
        List userList = userService.selectUserList(page, userAccount);
        page.setRecords(userList);
        HashMap map = new HashMap();
        map.put("totalPage", page.getPages());
        map.put("current", page.getCurrent());
        map.put("totalSize", page.getTotal());
        map.put("userList", page.getRecords());
        return Result.success(map);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Result save(HttpServletRequest request, HttpServletResponse response, UserAccount userAccount) {
        userService.save(userAccount);
        return Result.success(null);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Result delete(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        userService.deleteById(Integer.parseInt(id));
        return Result.success(null);
    }
}
