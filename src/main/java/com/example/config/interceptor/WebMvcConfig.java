package com.example.config.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Autowired
    UserArgumentResolver userArgumentResolver;
	
	@Bean
	public MiniInterceptor miniInterceptor() {
		return new MiniInterceptor();
	}

	@Bean
	public SystemInterceptor systemInterceptor() {
		return new SystemInterceptor();
	}

//	登陆拦截器
	@Override
	public void addInterceptors(InterceptorRegistry registry) {

//		拦截是否登录
		registry.addInterceptor(miniInterceptor()).addPathPatterns("/sys/**","/user/**","/index/**")
												  .excludePathPatterns("/sys/user/login","/sys/user/test")
												  .excludePathPatterns("/user/login","/user/regist","/user/verifiCode");

		//拦截是否管理员
		registry.addInterceptor(systemInterceptor()).addPathPatterns("/sys/**")
				.excludePathPatterns("/sys/user/login","/sys/user/test");

		super.addInterceptors(registry);
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/api/**");
	}


//	参数传递
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
//        argumentResolvers.add(userArgumentResolver);
    }

}
