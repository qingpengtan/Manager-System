package com.example.service.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.config.exception.GlobalException;
import com.example.config.redis.RedisService;
import com.example.config.redis.UserKey;
import com.example.config.util.CodeMsg;
import com.example.config.util.MD5Util;
import com.example.config.util.UUIDUtil;
import com.example.dao.UserDao;
import com.example.entity.User;
import com.example.service.UserService;
import com.example.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Service
public class UserServiceImpl extends ServiceImpl<UserDao,User> implements UserService {
	
	
	public static final String COOKI_NAME_TOKEN = "token";
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	RedisService redisService;


	public User regist(User user) {
	    List list = userDao.selectList(new EntityWrapper<User>().eq("user_phone",user.getUserPhone()));
	    if(list.size() != 0){
			throw new GlobalException(CodeMsg.ACCOUNT_IS_EXIT);
		}
        user.setCreateTime(new Date());
        String salt = UUID.randomUUID().toString().substring(0,6);
        user.setSalt(salt);
        String password = user.getPassword()+salt;
        user.setStatus("1000");
        user.setPassword(MD5Util.md5(MD5Util.md5(password)));
	    this.insert(user);
		return user;
	}

	public Map login(HttpServletRequest request, HttpServletResponse response, User userParm) {
		//判断手机号是否存在
		User user = userDao.checkUser(userParm);
		if(user == null) {
			throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
		}
		//验证密码
		String dbPass = user.getPassword();
		String saltDB = user.getSalt();
		String password = userParm.getPassword()+saltDB;
		String calcPass = MD5Util.md5(MD5Util.md5(password));
		if(!calcPass.equals(dbPass)) {
			throw new GlobalException(CodeMsg.PASSWORD_ERROR);
		}
		//生成cookie
		String token = UUIDUtil.uuid();
//		addCookie(response, token, user);
//		request.getSession().setAttribute("token",token);
		redisService.set(UserKey.token, token, user);
		HashMap map = new HashMap();
		map.put("userName",user.getUserName());
		map.put("role",user.getRoleId());
		map.put("userPhone",user.getUserPhone());
		map.put("token",token);
		return map;
	}
	
	private void addCookie(HttpServletResponse response, String token, User user) {
		redisService.set(UserKey.token, token, user);
		Cookie cookie = new Cookie(COOKI_NAME_TOKEN, token);
		cookie.setMaxAge(UserKey.token.expireSeconds());
		cookie.setPath("/");
		response.addCookie(cookie);
	}

}
