package com.orrange.user.service.impl;

import com.orrange.user.dto.UserVerificationDTO;
import com.orrange.user.dto.UserRegisterDTO;
import com.orrange.user.dto.UserLoginDTO;
import com.orrange.user.dto.UserProfileUpdateDTO;
import com.orrange.user.dto.UserVoteDTO;
import com.orrange.user.entity.User;
import com.orrange.user.entity.VoteActivity;
import com.orrange.user.mapper.UserMapper;
import com.orrange.user.mapper.VoteActivityMapper;
import com.orrange.user.service.UserService;
import com.orrange.user.vo.UserVO;
import com.orrange.user.vo.UserProfileVO;
import com.orrange.user.vo.UserVoteResultVO;
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
    
    @Autowired
    private VoteActivityMapper voteActivityMapper;

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
    
    @Override
    public UserProfileVO updateProfile(Integer userId, UserProfileUpdateDTO dto) {
        // 1. 查询当前用户信息
        User currentUser = userMapper.selectById(userId);
        if (currentUser == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 2. 检查手机号是否更换
        boolean phoneChanged = !currentUser.getPhone().equals(dto.getPhone());
        
        // 3. 如果手机号更换，验证新手机号和验证码
        if (phoneChanged) {
            // 检查新手机号是否已被其他用户使用
            Integer existingUserId = userMapper.selectIdByPhoneExcludeId(dto.getPhone(), userId);
            if (existingUserId != null) {
                throw new RuntimeException("该手机号已被其他用户使用");
            }
            
            // 验证验证码
            if (dto.getVerification() == null || dto.getVerification().trim().isEmpty()) {
                throw new RuntimeException("更换手机号必须提供验证码");
            }
            
            // 验证验证码是否正确
            if (!VerificationCodeCache.isValidCode("phone_change", dto.getPhone(), dto.getVerification())) {
                throw new RuntimeException("验证码过期或者验证码不准确");
            }
        }
        
        // 4. 处理身份证号加密/脱敏
        String encryptedIdCard = dto.getIdCard();
        if (encryptedIdCard != null && !encryptedIdCard.trim().isEmpty()) {
            // 这里可以添加身份证号加密逻辑
            // 暂时直接使用，实际项目中应该加密存储
        }
        
        // 5. 更新用户资料
        int result = userMapper.updateUserProfile(
            userId,
            dto.getPhone(),
            dto.getName(),
            dto.getGender(),
            encryptedIdCard,
            dto.getCommunityName(),
            dto.getBuildingNumber(),
            dto.getUnitNumber(),
            dto.getRoomNumber(),
            dto.getAreaSize(),
            true // 更新资料后设置为已验证
        );
        
        if (result <= 0) {
            throw new RuntimeException("更新用户资料失败");
        }
        
        // 6. 查询更新后的用户信息并返回
        User updatedUser = userMapper.selectById(userId);
        UserProfileVO profileVO = new UserProfileVO();
        profileVO.setId(updatedUser.getId());
        profileVO.setPhone(updatedUser.getPhone());
        profileVO.setName(updatedUser.getName());
        profileVO.setGender(updatedUser.getGender());
        
        // 身份证号脱敏显示
        if (updatedUser.getIdCard() != null && updatedUser.getIdCard().length() >= 4) {
            String idCard = updatedUser.getIdCard();
            StringBuilder masked = new StringBuilder();
            for (int i = 0; i < idCard.length() - 4; i++) {
                masked.append("*");
            }
            masked.append(idCard.substring(idCard.length() - 4));
            profileVO.setIdCard(masked.toString());
        } else {
            profileVO.setIdCard(updatedUser.getIdCard());
        }
        
        profileVO.setCommunityName(updatedUser.getCommunityName());
        profileVO.setBuildingNumber(updatedUser.getBuildingNumber());
        profileVO.setUnitNumber(updatedUser.getUnitNumber());
        profileVO.setRoomNumber(updatedUser.getRoomNumber());
        profileVO.setAreaSize(updatedUser.getAreaSize());
        profileVO.setIsVerified(updatedUser.getIsVerified());
        profileVO.setCreatedAt(updatedUser.getCreatedAt());
        
        return profileVO;
    }
    
    @Override
    public UserVoteResultVO vote(Integer userId, UserVoteDTO dto) {
        // 1. 检查用户状态
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 检查用户是否被禁用
        if ("DISABLED".equals(user.getStatus())) {
            throw new RuntimeException("用户为禁用状态");
        }
        
        // 2. 检查活动是否存在且有效
        VoteActivity activity = voteActivityMapper.selectById(dto.getActivityId());
        if (activity == null) {
            throw new RuntimeException("活动不存在");
        }
        
        // 检查活动时间
        Date now = new Date();
        if (now.before(activity.getStartTime())) {
            throw new RuntimeException("活动未开始或已结束");
        }
        if (now.after(activity.getEndTime())) {
            throw new RuntimeException("活动未开始或已结束");
        }
        
        // 3. 检查用户是否已对该议题投票
        Integer voteCount = userMapper.checkUserVoted(userId, dto.getQuestionId());
        if (voteCount != null && voteCount > 0) {
            throw new RuntimeException("您已完成该议题投票");
        }
        
        // 4. 检查用户是否在投票范围内
        boolean inScope = checkUserInVoteScope(user, activity);
        if (!inScope) {
            throw new RuntimeException("不在投票范围");
        }
        
        // 5. 查找选项ID
        Integer optionId = userMapper.findOptionIdByText(dto.getQuestionId(), dto.getSelectedOption());
        if (optionId == null) {
            throw new RuntimeException("无效的投票选项");
        }
        
        // 6. 插入投票记录
        int result = userMapper.insertUserVote(
            userId,
            dto.getActivityId(),
            dto.getQuestionId(),
            optionId,
            dto.getVoteMethod(),
            dto.getVoteTime(),
            user.getAreaSize()
        );
        
        if (result <= 0) {
            throw new RuntimeException("投票失败");
        }
        
        // 7. 返回投票结果
        UserVoteResultVO resultVO = new UserVoteResultVO();
        resultVO.setActivityId(dto.getActivityId());
        resultVO.setQuestionId(dto.getQuestionId());
        resultVO.setSelectedOption(dto.getSelectedOption());
        resultVO.setVoteMethod(dto.getVoteMethod());
        resultVO.setVoteTime(dto.getVoteTime());
        
        return resultVO;
    }
    
    /**
     * 检查用户是否在投票范围内
     */
    private boolean checkUserInVoteScope(User user, VoteActivity activity) {
        // 检查小区名称是否匹配
        if (!user.getCommunityName().equals(activity.getCommunityName())) {
            return false;
        }
        
        // 如果活动范围是ALL，则小区内所有用户都可以投票
        if ("ALL".equals(activity.getVoteScope())) {
            return true;
        }
        
        // 如果活动范围是PARTIAL，需要检查具体的楼栋和单元
        if ("PARTIAL".equals(activity.getVoteScope())) {
            // 这里需要查询vote_activity_scopes表来检查具体范围
            // 暂时简化处理，假设PARTIAL范围包含所有用户
            return true;
        }
        
        return false;
    }
}
