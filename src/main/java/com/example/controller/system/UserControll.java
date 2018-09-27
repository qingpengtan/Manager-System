package com.example.controller.system;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.example.config.util.MD5Util;
import com.example.config.util.Result;
import com.example.entity.UserAccount;
import com.example.service.impl.UserServiceImpl;
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
        List userList = userService.selectUserList(userAccount);
        return  Result.success(userList);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Result save(HttpServletRequest request, HttpServletResponse response, UserAccount userAccount) {
        if(userAccount.getUserUuid() == null){
            userAccount.setCreateTime(new Date());
            String salt = UUID.randomUUID().toString().substring(0,6);
            userAccount.setSalt(salt);
            String password = "123456qq"+salt;
            userAccount.setPassword(MD5Util.md5(MD5Util.md5(password)));
            userService.insert(userAccount);
        }else{
            userService.insertOrUpdate(userAccount);
        }

        return  Result.success(null);
    }
}
