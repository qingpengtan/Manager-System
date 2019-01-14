package com.example.config.interceptor;

import com.example.config.exception.GlobalException;
import com.example.config.redis.RedisService;
import com.example.config.redis.UserKey;
import com.example.config.util.CodeMsg;
import com.example.entity.UserAccount;
import com.example.service.impl.UserServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MiniInterceptor implements HandlerInterceptor {

	@Autowired
	public RedisService redis;

	@Autowired
	UserServiceImpl userService;

	/**
	 * 拦截请求，在controller调用之前
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object arg2) throws Exception {

//		String token = (String) request.getSession().getAttribute(UserServiceImpl.COOKI_NAME_TOKEN);
//		String token = request.getParameter("token");
		String token = request.getHeader("token");
		if(StringUtils.isEmpty(token))
			throw new GlobalException(CodeMsg.OUT_LINE);
		UserAccount userAccount = redis.get(UserKey.token, token, UserAccount.class);

		if (userAccount == null)
			throw new GlobalException(CodeMsg.ACCOUNT_QUIT);
		else{
//			redis.delK("UserKey:"+UserKey.token+token);
			redis.set(UserKey.token, token, userAccount);
			return true;
		}
	}
	
//	public void returnErrorResponse(HttpServletResponse response, Result result)
//			throws IOException, UnsupportedEncodingException {
//		OutputStream out=null;
//		try{
//		    response.setCharacterEncoding("utf-8");
//		    response.setContentType("text/json");
//		    out = response.getOutputStream();
//		    out.write(JsonUtils.objectToJson(result).getBytes("utf-8"));
//		    out.flush();
//		} finally{
//		    if(out!=null){
//		        out.close();
//		    }
//		}
//	}
//
	/**
	 * 请求controller之后，渲染视图之前
	 */
	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
	}
	
	/**
	 * 请求controller之后，视图渲染之后
	 */
	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
	}

}
