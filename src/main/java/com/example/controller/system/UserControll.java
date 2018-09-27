package com.example.controller.system;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.example.config.util.MD5Util;
import com.example.config.util.Result;
import com.example.entity.User;
import com.example.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/sys/user")
public class UserControll {

    @Autowired
    UserServiceImpl userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result login(HttpServletRequest request, HttpServletResponse response, User user) {
        user.setRoleId(3);
        Map map = userService.login(request,response,user);
        return  Result.success(map);
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Result List(HttpServletRequest request, HttpServletResponse response, User user) {
        List userList = userService.selectList(new EntityWrapper<>());
        return  Result.success(userList);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Result save(HttpServletRequest request, HttpServletResponse response, User user) {
        List userList = userService.selectList(new EntityWrapper<>());
        user.setCreateTime(new Date());
        String salt = UUID.randomUUID().toString().substring(0,6);
        user.setSalt(salt);
        String password = "123456qq"+salt;
        user.setPassword(MD5Util.md5(MD5Util.md5(password)));
        userService.insertOrUpdate(user);
        return  Result.success(null);
    }
}
