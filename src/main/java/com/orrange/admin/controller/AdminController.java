package com.orrange.admin.controller;

import com.orrange.admin.dto.VerificationDTO;
import com.orrange.admin.dto.AdminRegisterDTO;
import com.orrange.admin.dto.AdminLoginDTO;
import com.orrange.common.response.Result;
import com.orrange.admin.service.AdminService;
import com.orrange.admin.vo.AdminVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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
            String msg = e.getMessage();
            if ("手机号已注册".equals(msg)) return Result.error(400, msg);
            return Result.error(404, "程序更新中请等待");
        }
    }

    @PostMapping("/register")
    public Result<AdminVO> register(@RequestBody AdminRegisterDTO dto) {
        try {
            AdminVO vo = adminService.register(dto);
            return Result.success(vo);
        } catch (RuntimeException e) {
            String msg = e.getMessage();
            if ("验证码过期或者验证码不准确".equals(msg)) return Result.error(400, msg);
            if ("手机号已注册".equals(msg)) return Result.error(400, "手机号已经注册");
            return Result.error(404, "程序更新中请等待");
        }
    }

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody AdminLoginDTO dto) {
        try {
            String token = adminService.login(dto);
            Map<String, Object> data = new HashMap<>();
            Map<String, Object> admin = new HashMap<>();
            admin.put("id", 10001); // 如需真实id，可在Service返回后补充
            admin.put("phone", dto.getPhone());
            data.put("admin", admin);
            data.put("adminToken", token);
            return Result.success(data);
        } catch (RuntimeException e) {
            String msg = e.getMessage();
            if ("账号或密码不正确".equals(msg)) return Result.error(400, msg);
            if ("验证码不正确或已过期".equals(msg)) return Result.error(400, msg);
            return Result.error(404, "程序更新中请等待");
        }
    }
} 