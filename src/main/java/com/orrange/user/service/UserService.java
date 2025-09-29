package com.orrange.user.service;

import com.orrange.user.dto.UserVerificationDTO;
import com.orrange.user.dto.UserRegisterDTO;
import com.orrange.user.dto.UserLoginDTO;
import com.orrange.user.dto.UserProfileUpdateDTO;
import com.orrange.user.dto.UserVoteDTO;
import com.orrange.user.vo.UserVO;
import com.orrange.user.vo.UserProfileVO;
import com.orrange.user.vo.UserVoteResultVO;

public interface UserService {
    
    /**
     * 获取验证码（通过 scene 区分 register/login）
     */
    void getVerificationCode(UserVerificationDTO dto);
    
    /**
     * 用户注册
     */
    UserVO register(UserRegisterDTO dto);
    
    /**
     * 用户登录
     */
    Object login(UserLoginDTO dto);
    
    /**
     * 更新用户资料
     */
    UserProfileVO updateProfile(Integer userId, UserProfileUpdateDTO dto);
    
    /**
     * 用户投票
     */
    UserVoteResultVO vote(Integer userId, UserVoteDTO dto);
}
