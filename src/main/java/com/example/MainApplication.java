package com.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;

@MapperScan("com.example.dao")
@SpringBootApplication
//@Configuration
public class MainApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(MainApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
	}

	/**
	 * 文件上传配置
	 * @return
	 */
//	@Bean
//	public MultipartConfigElement multipartConfigElement() {
//		MultipartConfigFactory factory = new MultipartConfigFactory();
//		//单个文件最大
//		factory.setMaxFileSize("10240KB"); //KB,MB
//		/// 设置总上传数据总大小
//		factory.setMaxRequestSize("102400KB");
//		return factory.createMultipartConfig();
//	}
}
