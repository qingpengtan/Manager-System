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
import com.example.entity.UserAccount;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
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
		String userName = userAccount.getUserPhone().substring(8)+"用户";
		userAccount.setUserName(userName);
		userAccount.setRoleId(1);
		userAccount.setCreateTime(new Date());
        String salt = UUID.randomUUID().toString().substring(0,6);
        userAccount.setSalt(salt);
        String password = userAccount.getPassword()+salt;
        userAccount.setStatus("1000");
        userAccount.setUserPic("http://119.29.230.48/ROO/upload/image/tomcat.gif");
        userAccount.setPassword(MD5Util.md5(MD5Util.md5(password)));
	    this.insert(userAccount);
		return userAccount;
	}

	public UserAccount save(UserAccount userAccount) {
		List list = userDao.selectList(new EntityWrapper<UserAccount>().eq("user_phone", userAccount.getUserPhone()));
		if(list.size() != 0){
			throw new GlobalException(CodeMsg.ACCOUNT_IS_EXIT);
		}
		if(userAccount.getUserUuid() == null){
			userAccount.setCreateTime(new Date());
			String salt = UUID.randomUUID().toString().substring(0,6);
			userAccount.setSalt(salt);
			String password = "123456qq"+salt;
			userAccount.setPassword(MD5Util.md5(MD5Util.md5(password)));
			this.insert(userAccount);
		}else{
			this.insertOrUpdate(userAccount);
		}
		return userAccount;
	}

	public Map login(HttpServletRequest request, HttpServletResponse response, UserAccount userAccountParm)  {
		//判断手机号是否存在
		UserAccount userAccount = userDao.checkUser(userAccountParm);
		if(userAccount == null) {
			throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
		}
		if(userAccount.getStatus().equals("2000")) {
			throw new GlobalException(CodeMsg.DISABLED_ACCOUNT);
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
//		String tempToken = token;
		BASE64Encoder encoder = new BASE64Encoder();
		try {
			token = encoder.encode(token.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		UserAccount tempUser = new UserAccount();
		tempUser.setUserPhone(userAccount.getUserPhone());
//		addCookie(response, token, userAccount);
//		request.getSession().setAttribute("token",token);
		redisService.set(UserKey.token, token, tempUser);
		HashMap map = new HashMap();
		map.put("userName", userAccount.getUserName());
		map.put("role", userAccount.getRoleId());
		map.put("userPhone", userAccount.getUserPhone());
		map.put("userPic",userAccount.getUserPic());
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



	public List selectUserList(Page page, UserAccount userAccount) {
		return userDao.selectUserList(page,userAccount);
	}
}
