package com.orrange.user.controller;

import com.orrange.user.dto.UserVerificationDTO;
import com.orrange.user.dto.UserRegisterDTO;
import com.orrange.user.dto.UserLoginDTO;
import com.orrange.user.dto.UserProfileUpdateDTO;
import com.orrange.user.dto.UserVoteDTO;
import com.orrange.user.vo.UserVO;
import com.orrange.user.vo.UserProfileVO;
import com.orrange.user.vo.UserVoteResultVO;
import com.orrange.common.response.Result;
import com.orrange.common.utils.JwtUtils;
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

    /**
     * 用户资料编辑
     */
    @PostMapping("/profile")
    public Result<UserProfileVO> updateProfile(
            @RequestHeader("Authorization") String authorization,
            @RequestBody UserProfileUpdateDTO dto) {
        try {
            // 从JWT中获取用户ID
            String token = authorization.replace("Bearer ", "");
            Integer userId = JwtUtils.parseToken(token).get("userId", Integer.class);
            
            UserProfileVO result = userService.updateProfile(userId, dto);
            return Result.success(result);
        } catch (RuntimeException e) {
            String message = e.getMessage();
            if ("用户不存在".equals(message)) {
                return Result.error(404, "用户不存在");
            } else if ("该手机号已被其他用户使用".equals(message)) {
                return Result.error(400, "该手机号已被其他用户使用");
            } else if ("更换手机号必须提供验证码".equals(message)) {
                return Result.error(400, "更换手机号必须提供验证码");
            } else if ("验证码过期或者验证码不准确".equals(message)) {
                return Result.error(400, "验证码过期或者验证码不准确");
            } else if ("更新用户资料失败".equals(message)) {
                return Result.error(500, "更新用户资料失败");
            } else {
                return Result.error(400, message);
            }
        } catch (Exception e) {
            return Result.error(500, "系统错误：" + e.getMessage());
        }
    }

    /**
     * 用户投票
     */
    @PostMapping("/vote")
    public Result<UserVoteResultVO> vote(
            @RequestHeader("Authorization") String authorization,
            @RequestBody UserVoteDTO dto) {
        try {
            // 从JWT中获取用户ID
            String token = authorization.replace("Bearer ", "");
            Integer userId = JwtUtils.parseToken(token).get("userId", Integer.class);
            
            UserVoteResultVO result = userService.vote(userId, dto);
            return Result.success(result);
        } catch (RuntimeException e) {
            String message = e.getMessage();
            if ("用户不存在".equals(message)) {
                return Result.error(404, "用户不存在");
            } else if ("用户为禁用状态".equals(message)) {
                return Result.error(403, "用户为禁用状态");
            } else if ("活动不存在".equals(message)) {
                return Result.error(404, "活动不存在");
            } else if ("活动未开始或已结束".equals(message)) {
                return Result.error(400, "活动未开始或已结束");
            } else if ("您已完成该议题投票".equals(message)) {
                return Result.error(400, "您已完成该议题投票");
            } else if ("不在投票范围".equals(message)) {
                return Result.error(403, "不在投票范围");
            } else if ("无效的投票选项".equals(message)) {
                return Result.error(400, "无效的投票选项");
            } else if ("投票失败".equals(message)) {
                return Result.error(500, "投票失败");
            } else {
                return Result.error(400, message);
            }
        } catch (Exception e) {
            return Result.error(500, "系统错误：" + e.getMessage());
        }
    }
}
