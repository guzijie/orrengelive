package com.orrange.admin.controller;

import com.orrange.admin.dto.VerificationDTO;
import com.orrange.admin.dto.AdminRegisterDTO;
import com.orrange.common.response.Result;
import com.orrange.admin.service.AdminService;
import com.orrange.admin.vo.AdminVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @PostMapping("/verification")
    public Result<?> getVerificationCode(@RequestBody VerificationDTO dto) {
        try {
            adminService.getVerificationCode(dto);
            return Result.success(null);
        } catch (RuntimeException e) {
            if ("手机号已注册".equals(e.getMessage())) {
                return Result.error(400, "手机号已注册");
            } else {
                return Result.error(404, "程序更新中请等待");
            }
        }
    }

    @PostMapping("/register")
    public Result<AdminVO> register(@RequestBody AdminRegisterDTO dto) {
        try {
            AdminVO vo = adminService.register(dto);
            return Result.success(vo);
        } catch (RuntimeException e) {
            if ("手机号已注册".equals(e.getMessage())) {
                return Result.error(400, "手机号已注册");
            } else if ("验证码错误或已过期".equals(e.getMessage())) {
                return Result.error(400, "验证码错误或已过期");
            } else {
                return Result.error(404, "程序更新中请等待");
            }
        }
    }
} 