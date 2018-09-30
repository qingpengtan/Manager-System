package com.example.service.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.config.exception.GlobalException;
import com.example.config.redis.RedisService;
import com.example.config.redis.UserKey;
import com.example.config.util.CodeMsg;
import com.example.config.util.MD5Util;
import com.example.config.util.UUIDUtil;
import com.example.dao.UserDao;
import com.example.entity.UserAccount;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Service
public class UserServiceImpl extends ServiceImpl<UserDao, UserAccount> implements UserService {
	
	
	public static final String COOKI_NAME_TOKEN = "token";
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	RedisService redisService;


	public UserAccount regist(UserAccount userAccount) {
	    List list = userDao.selectList(new EntityWrapper<UserAccount>().eq("user_phone", userAccount.getUserPhone()));
	    if(list.size() != 0){
			throw new GlobalException(CodeMsg.ACCOUNT_IS_EXIT);
		}
		userAccount.setUserName(userAccount.getUserPhone());
		userAccount.setRoleId(1);
		userAccount.setCreateTime(new Date());
        String salt = UUID.randomUUID().toString().substring(0,6);
        userAccount.setSalt(salt);
        String password = userAccount.getPassword()+salt;
        userAccount.setStatus("1000");
        userAccount.setPassword(MD5Util.md5(MD5Util.md5(password)));
	    this.insert(userAccount);
		return userAccount;
	}

	public Map login(HttpServletRequest request, HttpServletResponse response, UserAccount userAccountParm) {
		//判断手机号是否存在
		UserAccount userAccount = userDao.checkUser(userAccountParm);
		if(userAccount == null) {
			throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
		}
		//验证密码
		String dbPass = userAccount.getPassword();
		String saltDB = userAccount.getSalt();
		String password = userAccountParm.getPassword()+saltDB;
		String calcPass = MD5Util.md5(MD5Util.md5(password));
		if(!calcPass.equals(dbPass)) {
			throw new GlobalException(CodeMsg.PASSWORD_ERROR);
		}
		//生成cookie
		String token = UUIDUtil.uuid();
//		addCookie(response, token, userAccount);
//		request.getSession().setAttribute("token",token);
		redisService.set(UserKey.token, token, userAccount);
		HashMap map = new HashMap();
		map.put("userName", userAccount.getUserName());
		map.put("role", userAccount.getRoleId());
		map.put("userPhone", userAccount.getUserPhone());
		map.put("token",token);
		return map;
	}
	
	private void addCookie(HttpServletResponse response, String token, UserAccount userAccount) {
		redisService.set(UserKey.token, token, userAccount);
		Cookie cookie = new Cookie(COOKI_NAME_TOKEN, token);
		cookie.setMaxAge(UserKey.token.expireSeconds());
		cookie.setPath("/");
		response.addCookie(cookie);
	}



	public List selectUserList(UserAccount userAccount) {
		return userDao.selectUserList(userAccount);
	}
}
