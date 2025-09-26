package com.orrange.user.controller;

import com.orrange.user.dto.UserVerificationDTO;
import com.orrange.user.dto.UserRegisterDTO;
import com.orrange.user.dto.UserLoginDTO;
import com.orrange.user.vo.UserVO;
import com.orrange.common.response.Result;
import com.orrange.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    
    @Autowired
    private UserService userService;

    /**
     * 获取验证码（通过 scene 区分 register/login）
     */
    @PostMapping("/verification")
    public Result<?> getVerificationCode(@RequestBody UserVerificationDTO dto) {
        try {
            userService.getVerificationCode(dto);
            return Result.success(null);
        } catch (RuntimeException e) {
            String m = e.getMessage();
            if ("手机号已注册".equals(m)) {
                return Result.error(400, "手机号已注册");
            } else if ("用户不存在".equals(m)) {
                return Result.error(400, "用户不存在");
            } else if ("不支持的场景".equals(m)) {
                return Result.error(400, "不支持的场景");
            } else {
                return Result.error(404, "程序更新中请等待");
            }
        }
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<UserVO> register(@RequestBody UserRegisterDTO dto) {
        try {
            UserVO vo = userService.register(dto);
            return Result.success(vo);
        } catch (RuntimeException e) {
            if ("手机号已经注册".equals(e.getMessage())) {
                return Result.error(400, "手机号已经注册");
            } else if ("验证码过期或者验证码不准确".equals(e.getMessage())) {
                return Result.error(400, "验证码过期或者验证码不准确");
            } else {
                return Result.error(404, "程序更新中请等待");
            }
        }
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<?> login(@RequestBody UserLoginDTO dto) {
        try {
            Object result = userService.login(dto);
            return Result.success(result);
        } catch (RuntimeException e) {
            String message = e.getMessage();
            if ("手机号或密码错误".equals(message)) {
                return Result.error(400, "手机号或密码错误");
            } else if ("验证码错误或已过期".equals(message)) {
                return Result.error(400, "验证码错误或已过期");
            } else if ("用户不存在".equals(message)) {
                return Result.error(400, "用户不存在");
            } else if ("不支持的登录方式".equals(message)) {
                return Result.error(400, "不支持的登录方式");
            } else {
                return Result.error(400, message);
            }
        }
    }
}
