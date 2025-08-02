package com.orrange.admin.service.impl;

import com.orrange.admin.dto.VerificationDTO;
import com.orrange.admin.dto.AdminRegisterDTO;
import com.orrange.admin.entity.Admin;
import com.orrange.admin.mapper.AdminMapper;
import com.orrange.admin.service.AdminService;
import com.orrange.admin.vo.AdminVO;
import com.orrange.common.utils.VerificationCodeUtils;
import com.orrange.common.utils.VerificationCodeCache;
import com.orrange.common.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminMapper adminMapper;

    @Override
    public void getVerificationCode(VerificationDTO dto) {
        // 检查手机号是否已注册
        if (adminMapper.selectByPhone(dto.getPhone()) != null) {
            throw new RuntimeException("手机号已注册");
        }
        
        // 生成6位验证码
        String verificationCode = VerificationCodeUtils.generateVerificationCode();
        
        // 存储验证码到缓存，60秒过期
        VerificationCodeCache.storeCode(dto.getPhone(), verificationCode);
        
        // 这里可以集成短信服务发送验证码
        // 目前只是生成并存储，实际项目中需要调用短信API
        System.out.println("验证码: " + verificationCode + " 已发送到手机: " + dto.getPhone());
    }

    @Override
    public AdminVO register(AdminRegisterDTO dto) {
        // 检查手机号是否已注册
        if (adminMapper.selectByPhone(dto.getPhone()) != null) {
            throw new RuntimeException("手机号已注册");
        }
        
        // 验证验证码
        if (!VerificationCodeCache.isValidCode(dto.getPhone(), dto.getVerification())) {
            throw new RuntimeException("验证码错误或已过期");
        }
        
        // 创建Admin实体并设置数据
        Admin admin = new Admin();
        admin.setPhone(dto.getPhone());
        // 密码加密存储
        admin.setPassword(PasswordUtils.encrypt(dto.getPassword()));
        
        // 插入数据库
        int result = adminMapper.insertAdmin(admin);
        if (result <= 0) {
            throw new RuntimeException("注册失败，请重试");
        }
        
        // 返回注册成功的用户信息
        AdminVO vo = new AdminVO();
        vo.setPhone(dto.getPhone());
        return vo;
    }
} 