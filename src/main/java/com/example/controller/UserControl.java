package com.example.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.example.config.util.MD5Util;
import com.example.config.util.Result;
import com.example.entity.User;
import com.example.service.impl.UserServiceImpl;
import org.apache.commons.codec.digest.Md5Crypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/*
    参数怎么作为对象接收（无）
    参数怎么校验（不合理）
    登陆拦截（不合理）
 */
@RestController
@RequestMapping("/user")
public class UserControl {

    @Autowired
    UserServiceImpl userService;


    @RequestMapping(value = "/regist", method = RequestMethod.POST)
    public Result registUser(HttpServletRequest request, HttpServletResponse response, User user) {
        user.setRoleId(1);
        userService.regist(user);
        return  Result.success(null);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result login(HttpServletRequest request, HttpServletResponse response, User user) {
        user.setRoleId(1);
        Map map = userService.login(request,response,user);
        return  Result.success(map);
    }
}


