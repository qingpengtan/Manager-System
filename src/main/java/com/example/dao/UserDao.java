package com.example.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.example.entity.UserAccount;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface UserDao extends BaseMapper<UserAccount> {

	@Select("select salt,password,role_id as roleId, user_phone as userPhone,user_name as userNme" +
			" from user_account where (user_phone = #{userAccount.userName}" +
			" or user_name= #{userAccount.userName}) and role_id = #{userAccount.roleId} ")
	public UserAccount checkUser(@Param("userAccount") UserAccount userAccount);

	@Insert("INSERT INTO province (province, city,area,value,parent_value,short_name) VALUES(" +
			"#{map.province},#{map.city},#{map.area},#{map.value},#{map.parent},#{map.short});")
    void insertCity(@Param("map") HashMap map);

	@Select("select u.user_uuid as userUuid,u.user_name as userName, u.user_phone as userPhone, u.role_id as roleId," +
			" u.age,DATE_FORMAT(u.birthday,'%Y-%m-%d') as birthday ,u.province as provinceV,u.city as cityV,"+
			" u.sex,p.province,c.city,u.address,u.user_tag as userTag,u.status, DATE_FORMAT(u.create_time,'%Y-%m-%d %H:%i') AS createTime" +
			" from user_account u left join province p on p.svalue = u.province left join province c on c.svalue = u.city where u.user_phone" +
			"=#{userAccount.userPhone} order by u.create_time desc")
	List<Map<String,Object>> selectUserList(@Param("userAccount") UserAccount userAccount);
}
