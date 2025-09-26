package com.orrange.admin.service.impl;

import com.orrange.admin.dto.VerificationDTO;
import com.orrange.admin.dto.AdminRegisterDTO;
import com.orrange.admin.dto.AdminLoginDTO;
import com.orrange.admin.entity.Admin;
import com.orrange.admin.mapper.AdminMapper;
import com.orrange.admin.service.AdminService;
import com.orrange.admin.vo.AdminVO;
import com.orrange.common.utils.VerificationCodeUtils;
import com.orrange.common.utils.VerificationCodeCache;
import com.orrange.common.utils.PasswordUtils;
import com.orrange.common.utils.TokenUtils;
import com.orrange.common.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminMapper adminMapper;

    @Override
    public void getVerificationCode(VerificationDTO dto) {
        if ("register".equalsIgnoreCase(dto.getScene())) {
            if (adminMapper.selectByPhone(dto.getPhone()) != null) {
                throw new RuntimeException("手机号已注册");
            }
        } else if ("login".equalsIgnoreCase(dto.getScene())) {
            // 登录场景无需校验未注册
        } else {
            throw new RuntimeException("无效的scene");
        }
        String verificationCode = VerificationCodeUtils.generateVerificationCode();
        VerificationCodeCache.storeCode(dto.getScene(), dto.getPhone(), verificationCode);
        System.out.println("scene=" + dto.getScene() + ", phone=" + dto.getPhone() + ", code=" + verificationCode);
    }

    @Override
    public AdminVO register(AdminRegisterDTO dto) {
        if (adminMapper.selectByPhone(dto.getPhone()) != null) {
            throw new RuntimeException("手机号已注册");
        }
        if (!VerificationCodeCache.isValidCode("register", dto.getPhone(), dto.getVerification())) {
            throw new RuntimeException("验证码过期或者验证码不准确");
        }
        Admin admin = new Admin();
        admin.setPhone(dto.getPhone());
        admin.setPassword(PasswordUtils.encrypt(dto.getPassword()));
        int result = adminMapper.insertAdmin(admin);
        if (result <= 0) {
            throw new RuntimeException("注册失败，请重试");
        }
        AdminVO vo = new AdminVO();
        vo.setPhone(dto.getPhone());
        return vo;
    }

    @Override
    public String login(AdminLoginDTO dto) {
        Admin admin = adminMapper.selectByPhone(dto.getPhone());
        if (admin == null) {
            throw new RuntimeException("账号或密码不正确");
        }
        if ("password".equalsIgnoreCase(dto.getLoginType())) {
            String enc = PasswordUtils.encrypt(dto.getPassword());
            if (!enc.equals(admin.getPassword())) {
                throw new RuntimeException("账号或密码不正确");
            }
        } else if ("sms".equalsIgnoreCase(dto.getLoginType())) {
            if (!VerificationCodeCache.isValidCode("login", dto.getPhone(), dto.getCode())) {
                throw new RuntimeException("验证码不正确或已过期");
            }
        } else {
            throw new RuntimeException("无效的登录方式");
        }
        // generate JWT so that JwtAuthFilter can validate it
        Integer uid = admin.getId() == null ? null : admin.getId().intValue();
        return JwtUtils.generateToken(uid, admin.getPhone());
    }
} 