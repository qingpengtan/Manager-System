package com.example.controller;

import com.example.config.util.Result;
import com.example.entity.UserAccount;
import com.example.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


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
}


