package com.orrange.admin.service.impl;

import com.orrange.admin.dto.AdminRegisterDTO;
import com.orrange.admin.entity.Admin;
import com.orrange.admin.mapper.AdminMapper;
import com.orrange.admin.service.AdminService;
import com.orrange.admin.vo.AdminVO;
import com.orrange.common.utils.BeanCopyUtils;
import com.orrange.common.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminMapper adminMapper;

    @Override
    public AdminVO register(AdminRegisterDTO dto) {
        // 检查手机号是否已注册
        if (adminMapper.selectByPhone(dto.getPhone()) != null) {
            throw new RuntimeException("手机号已注册");
        }
        Admin admin = new Admin();
        BeanCopyUtils.copy(dto, admin);
        // 密码加密
        admin.setPassword(PasswordUtils.encrypt(dto.getPassword()));
        adminMapper.insertAdmin(admin);
        AdminVO vo = new AdminVO();
        BeanCopyUtils.copy(admin, vo);
        return vo;
    }
} 