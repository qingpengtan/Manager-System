package com.example.controller.api;


import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.example.config.exception.GlobalException;
import com.example.config.redis.IpKey;
import com.example.config.redis.RedisService;
import com.example.config.redis.UserKey;
import com.example.config.util.CodeMsg;
import com.example.config.util.MD5Util;
import com.example.config.util.Result;
import com.example.dao.UserDao;
import com.example.entity.Article;
import com.example.entity.Comments;
import com.example.entity.UserAccount;
import com.example.service.impl.ArticleServiceImpl;
import com.example.service.impl.CommentsServiceImpl;
import com.example.service.impl.UserServiceImpl;
import cz.mallat.uasparser.OnlineUpdater;
import cz.mallat.uasparser.UASparser;
import cz.mallat.uasparser.UserAgentInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ApiControl {

    @Autowired
    ArticleServiceImpl articleService;

    @Autowired
    RedisService redisService;

    @Autowired
    UserServiceImpl userService;
    @Autowired
    CommentsServiceImpl commentsService;

    @Autowired
    UserDao userDao;

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Result List(HttpServletRequest request, HttpServletResponse response, Article article) {
        String pageN= request.getParameter("page");
        if(StringUtils.isEmpty(pageN)){
            pageN = "1";
        }
        Page page = new Page();
        page.setCurrent(Integer.parseInt(pageN));
        page.setSize(10);
        List articleList = articleService.selectArticleList(page,article);
        page.setRecords(articleList);
        HashMap map = new HashMap();
        map.put("totalSize",page.getTotal());
        map.put("totalPage",page.getPages());
        map.put("current",page.getCurrent());
        map.put("articleList",page.getRecords());
        return  Result.success(map);
    }

    @RequestMapping(value = "/classify", method = RequestMethod.POST)
    public Result classify(HttpServletRequest request, HttpServletResponse response) {
        String param = request.getParameter("exculde");
        List classify = articleService.classify(param);
        return  Result.success(classify);
    }

    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public Result detail(HttpServletRequest request, HttpServletResponse response, Article article, UserAccount userAccount) {
        HashMap map = articleService.articleDetail(article,userAccount);
        return  Result.success(map);
    }

    @RequestMapping(value = "/visitInfo", method = RequestMethod.POST)
    public Result visitInfo(HttpServletRequest request, HttpServletResponse response,UserAccount userAccount) {


        JSONArray ipList = redisService.get(IpKey.ip,"StaticIp",JSONArray.class);
        if(ipList == null || ipList.size() == 0)
            ipList = new JSONArray();
        String IP = getIpAddr(request);
        if (!ipList.contains(IP)){
            ipList.add(IP);
        }
        redisService.set(IpKey.ip,"StaticIp",ipList);
        HashMap map = new HashMap();
        map.put("visterNum",ipList.size());
        map.put("ips",request.getRemoteAddr());
        map.put("ip",getIpAddr(request));
        String agent= request.getHeader("user-agent");
        try {
            UASparser uasParser = new UASparser(OnlineUpdater.getVendoredInputStream());
            UserAgentInfo userAgentInfo = uasParser.parse(agent);
            map.put("os",userAgentInfo.getOsName());
            map.put("browser",userAgentInfo.getUaFamily());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  Result.success(map);
    }

    @RequestMapping(value = "/userInfo", method = RequestMethod.POST)
    public Result userInfo(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader("token");
        UserAccount userAccount = redisService.get(UserKey.token, token, UserAccount.class);

        if (userAccount == null){
            return  Result.success(getIpAddr(request));
        }else {
            return  Result.success(userAccount.getUserPhone());
        }
    }


    private String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        System.out.println("x-forwarded-for ip: " + ip);
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if( ip.indexOf(",")!=-1 ){
                ip = ip.split(",")[0];
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
            System.out.println("Proxy-Client-IP ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
            System.out.println("WL-Proxy-Client-IP ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
            System.out.println("HTTP_CLIENT_IP ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            System.out.println("HTTP_X_FORWARDED_FOR ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
            System.out.println("X-Real-IP ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            System.out.println("getRemoteAddr ip: " + ip);
        }
        System.out.println("获取客户端ip: " + ip);
        return ip;
    }


    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public Result comment(HttpServletRequest request, HttpServletResponse response, Comments comments) {
        String token = request.getHeader("token");
        UserAccount userAccount = redisService.get(UserKey.token, token, UserAccount.class);
        UserAccount tempUser = new UserAccount();
        if(userAccount == null){
//            tempUser.setRoleId(1);
//            tempUser.setUserPhone("tempUser");
//            tempUser.setUserName(getIpAddr(request) + "用户");
//            tempUser.setCreateTime(new Date());
//            String salt = UUID.randomUUID().toString().substring(0,6);
//            tempUser.setSalt(salt);
//            tempUser.setStatus("1000");
//            tempUser.setUserPic("http://119.29.230.48/upload/image/tomcat.png");
//            tempUser.setPassword(MD5Util.md5(MD5Util.md5(salt)));
            tempUser.setUserUuid(1);
        }else {
            tempUser = userService.selectOne(new EntityWrapper<UserAccount>()
                    .eq("user_phone",userAccount.getUserPhone()));
        }
        comments.setUserId(tempUser.getUserUuid());
        comments.setStatus("1000");
        comments.setCreateTime(new Date());
        commentsService.insert(comments);
        return  Result.success(null);
    }


    @RequestMapping(value = "/commentList", method = RequestMethod.POST)
    public Result commentList(HttpServletRequest request, HttpServletResponse response, Comments comments) {

        List list = commentsService.commentsList(comments);

        return  Result.success(list);
    }
}
