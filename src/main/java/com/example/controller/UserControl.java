package com.example.controller;

import com.example.config.redis.RedisService;
import com.example.config.redis.UserKey;
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

    @Autowired
    RedisService redisService;


    @RequestMapping(value = "/regist", method = RequestMethod.POST)
    public Result registUser(HttpServletRequest request, HttpServletResponse response, UserAccount userAccount) {
        userAccount.setRoleId(1);
        userService.regist(userAccount);
        return  Result.success(null);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result login(HttpServletRequest request, HttpServletResponse response, UserAccount userAccount) {
        userAccount.setRoleId(1);
        Map map = userService.login(request,response, userAccount);
        return  Result.success(map);
    }

    @RequestMapping(value = "/personInfo", method = RequestMethod.POST)
    public Result personInfo(HttpServletRequest request, HttpServletResponse response, UserAccount userAccount) {
        String token = request.getHeader("token");
        UserAccount user = redisService.get(UserKey.token, token, UserAccount.class);
        List list = userService.selectUserList(user);
        return  Result.success(list.get(0));
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Result save(HttpServletRequest request, HttpServletResponse response, UserAccount userAccount) {

        userService.insertOrUpdate(userAccount);

        return  Result.success(null);
    }
}


