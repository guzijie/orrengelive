package com.orrange.user.service.impl;

import com.orrange.user.dto.UserVerificationDTO;
import com.orrange.user.dto.UserRegisterDTO;
import com.orrange.user.dto.UserLoginDTO;
import com.orrange.user.entity.User;
import com.orrange.user.mapper.UserMapper;
import com.orrange.user.service.UserService;
import com.orrange.user.vo.UserVO;
import com.orrange.common.utils.VerificationCodeUtils;
import com.orrange.common.utils.VerificationCodeCache;
import com.orrange.common.utils.PasswordUtils;
import com.orrange.common.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserMapper userMapper;

    @Override
    public void getVerificationCode(UserVerificationDTO dto) {
        String scene = dto.getScene();
        if (scene == null || (!"register".equals(scene) && !"login".equals(scene))) {
            throw new RuntimeException("不支持的场景");
        }
        if ("register".equals(scene)) {
            if (userMapper.selectByPhone(dto.getPhone()) != null) {
                throw new RuntimeException("手机号已注册");
            }
        } else {
            if (userMapper.selectByPhone(dto.getPhone()) == null) {
                throw new RuntimeException("用户不存在");
            }
        }
        String verificationCode = VerificationCodeUtils.generateVerificationCode();
        VerificationCodeCache.storeCode(scene, dto.getPhone(), verificationCode);
        System.out.println("[" + scene + "] 验证码: " + verificationCode + " 已发送到手机: " + dto.getPhone());
    }

    @Override
    public UserVO register(UserRegisterDTO dto) {
        if (userMapper.selectByPhone(dto.getPhone()) != null) {
            throw new RuntimeException("手机号已经注册");
        }
        if (!VerificationCodeCache.isValidCode("register", dto.getPhone(), dto.getVerification())) {
            throw new RuntimeException("验证码过期或者验证码不准确");
        }
        User user = new User();
        user.setPhone(dto.getPhone());
        user.setPassword(PasswordUtils.encrypt(dto.getPassword()));
        user.setCommunityName("阳光花园");
        user.setIsVerified(false);
        user.setCreatedAt(new Date());
        int result = userMapper.insertUser(user);
        if (result <= 0) {
            throw new RuntimeException("注册失败，请重试");
        }
        UserVO vo = new UserVO();
        vo.setId(user.getId());
        vo.setPhone(user.getPhone());
        vo.setCommunityName(user.getCommunityName());
        vo.setIsVerified(user.getIsVerified());
        return vo;
    }

    @Override
    public Object login(UserLoginDTO dto) {
        User user = null;
        if ("password".equals(dto.getLoginType())) {
            if (dto.getPassword() == null || dto.getPassword().trim().isEmpty()) {
                throw new RuntimeException("密码不能为空");
            }
            String encryptedPassword = PasswordUtils.encrypt(dto.getPassword());
            user = userMapper.selectByPhoneAndPassword(dto.getPhone(), encryptedPassword);
            if (user == null) {
                throw new RuntimeException("手机号或密码错误");
            }
        } else if ("sms".equals(dto.getLoginType())) {
            if (dto.getCode() == null || dto.getCode().trim().isEmpty()) {
                throw new RuntimeException("验证码不能为空");
            }
            if (!VerificationCodeCache.isValidCode("login", dto.getPhone(), dto.getCode())) {
                throw new RuntimeException("验证码错误或已过期");
            }
            user = userMapper.selectByPhone(dto.getPhone());
            if (user == null) {
                throw new RuntimeException("用户不存在");
            }
        } else {
            throw new RuntimeException("不支持的登录方式");
        }
        Map<String, Object> result = new HashMap<>();
        UserVO userVO = new UserVO();
        userVO.setId(user.getId());
        userVO.setPhone(user.getPhone());
        userVO.setName(user.getName());
        userVO.setCommunityName(user.getCommunityName());
        userVO.setBuildingNumber(user.getBuildingNumber());
        userVO.setUnitNumber(user.getUnitNumber());
        userVO.setRoomNumber(user.getRoomNumber());
        userVO.setIsVerified(user.getIsVerified());
        result.put("user", userVO);
        // 生成 JWT（100 天有效）
        String token = JwtUtils.generateToken(user.getId(), user.getPhone());
        result.put("token", token);
        return result;
    }
}
