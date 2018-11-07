package com.example.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.example.config.exception.GlobalException;
import com.example.config.redis.RedisService;
import com.example.config.redis.UserKey;
import com.example.config.util.CodeMsg;
import com.example.config.util.Result;
import com.example.config.util.VerifiCode;
import com.example.entity.UserAccount;
import com.example.service.impl.UserServiceImpl;
import com.example.vo.UpdateUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserControl {

    @Autowired
    UserServiceImpl userService;

    @Autowired
    RedisService redisService;


    @RequestMapping(value = "/regist", method = RequestMethod.POST)
    public Result registUser(HttpServletRequest request, HttpServletResponse response, UserAccount userAccount) {
        String code = request.getParameter("imageCode");
        String imageCode = (String) request.getSession().getAttribute("imageCode");
        if(!imageCode.equalsIgnoreCase(code)){
            return  Result.error(new CodeMsg(500218,"验证码错误"));
        }
        userService.regist(userAccount);
        return  Result.success(null);
    }

    @RequestMapping(value = "/verifiCode", method = RequestMethod.GET)
    public void verifiCode(HttpServletRequest request, HttpServletResponse response, UserAccount userAccount) {
        VerifiCode verifiCode = new VerifiCode();
        BufferedImage  image = verifiCode.getImage();
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            String code = verifiCode.getText();
            request.getSession().setAttribute("imageCode",code);
            verifiCode.output(image,out);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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
        Page page = new Page();
        page.setCurrent(1);
        page.setSize(1);
        UserAccount user = redisService.get(UserKey.token, token, UserAccount.class);
        List list = userService.selectUserList(page, user);
        return  Result.success(list.get(0));
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Result save(HttpServletRequest request, HttpServletResponse response, @Valid UpdateUserVo userVo) {
        String token = request.getHeader("token");
        UserAccount userAccount = redisService.get(UserKey.token, token, UserAccount.class);

        UserAccount user = userService.selectOne(new EntityWrapper<UserAccount>().eq("user_phone",userAccount.getUserPhone()));
        user.setUserName(userVo.getUserName());
        user.setUserTag(userVo.getUserTag());
        user.setAddress(userVo.getAddress());
        user.setProvince(userVo.getProvince());
        user.setCity(userVo.getCity());
        user.setSex(userVo.getSex());
        user.setBirthday(userVo.getBirthday());
        if(user.getBirthday() != null){
            Calendar startDate = Calendar.getInstance();
            Calendar endDate = Calendar.getInstance();
            startDate.setTime(user.getBirthday());
            endDate.setTime(new Date());
            int age = (endDate.get(Calendar.YEAR) - startDate.get(Calendar.YEAR));
            if(age <= 0 || age > 120){
                throw new GlobalException(CodeMsg.UNLEAGEL_BIRTHDAY);
            }
            user.setAge(age);
        }

        userService.insertOrUpdate(user);

        return  Result.success(null);
    }
}


