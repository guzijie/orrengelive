package com.orrange.user.mapper;

import com.orrange.user.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    
    /**
     * 根据手机号查询用户
     */
    User selectByPhone(@Param("phone") String phone);
    
    /**
     * 插入新用户
     */
    int insertUser(User user);
    
    /**
     * 根据手机号和密码查询用户
     */
    User selectByPhoneAndPassword(@Param("phone") String phone, @Param("password") String password);
    
    /**
     * 根据ID查询用户
     */
    User selectById(@Param("id") Integer id);
}


