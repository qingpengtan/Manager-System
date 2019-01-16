package com.example.controller;


import com.example.config.util.CodeMsg;
import com.example.config.util.Result;
import com.example.entity.UserAccount;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/index/comment")
public class CommentControl {



    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public Result comment(HttpServletRequest request, HttpServletResponse response, UserAccount userAccount) {

        return  Result.success(null);
    }


    @RequestMapping(value = "/commentList", method = RequestMethod.POST)
    public Result commentList(HttpServletRequest request, HttpServletResponse response, UserAccount userAccount) {

        return  Result.success(null);
    }




}
