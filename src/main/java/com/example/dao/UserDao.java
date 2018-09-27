package com.example.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.example.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserDao extends BaseMapper<User> {

	@Select("select salt,password,role_id as roleId, user_phone as userPhone,user_name as userNme" +
			" from user where (user_phone = #{user.userName}" +
			" or user_name= #{user.userName}) and role_id = #{user.roleId} ")
	public User checkUser(@Param("user") User user);
}
