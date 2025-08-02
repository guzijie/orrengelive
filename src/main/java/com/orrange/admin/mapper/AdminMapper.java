package com.orrange.admin.mapper;

import com.orrange.admin.entity.Admin;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminMapper {
    Admin selectByPhone(String phone);
    int insertAdmin(Admin admin);
} 