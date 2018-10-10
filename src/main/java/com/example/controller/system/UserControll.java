package com.example.controller.system;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.example.config.util.MD5Util;
import com.example.config.util.Result;
import com.example.entity.UserAccount;
import com.example.service.impl.UserServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@RequestMapping("/sys/user")
public class UserControll {

    @Autowired
    UserServiceImpl userService;

//    @RequestMapping(value = "/test", method = RequestMethod.POST)
//    public void test(HttpServletRequest request) {
//        HashMap map = new HashMap();
//        map.put("province",request.getParameter("province"));
//        map.put("city",request.getParameter("city"));
//        map.put("area",request.getParameter("area"));
//        map.put("value",request.getParameter("value"));
//        map.put("parent",request.getParameter("parent"));
//        map.put("short",request.getParameter("short"));
//        userService.tests(map);
//    }


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result login(HttpServletRequest request, HttpServletResponse response, UserAccount userAccount) {
        userAccount.setRoleId(3);
        Map map = userService.login(request,response, userAccount);
        return  Result.success(map);
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Result List(HttpServletRequest request, HttpServletResponse response, UserAccount userAccount) {
        String pageN= request.getParameter("page");
        if(StringUtils.isEmpty(pageN)){
            pageN = "1";
        }
        Page page = new Page();
        page.setCurrent(Integer.parseInt(pageN));
        page.setSize(10);
        List userList = userService.selectUserList(page,userAccount);
        page.setRecords(userList);
        HashMap map = new HashMap();
        map.put("totalPage",page.getPages());
        map.put("current",page.getCurrent());
        map.put("totalSize",page.getTotal());
        map.put("userList",page.getRecords());
        return  Result.success(map);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Result save(HttpServletRequest request, HttpServletResponse response, UserAccount userAccount) {
        userService.save(userAccount);
        return  Result.success(null);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Result delete(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        userService.deleteById(Integer.parseInt(id));
        return  Result.success(null);
    }
}
