package com.example.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.example.entity.Article;
import com.example.entity.UserAccount;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserDao extends BaseMapper<UserAccount> {

	UserAccount checkUser(@Param("userAccount") UserAccount userAccount);


	List<Map<String,Object>> selectUserList(@Param("userAccount") UserAccount userAccount);


}
