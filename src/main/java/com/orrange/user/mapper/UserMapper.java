package com.orrange.user.mapper;

import com.orrange.user.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    
    /**
     * 根据手机号查询用户
     */
    User selectByPhone(@Param("phone") String phone);
    
    /**
     * 插入新用户
     */
    int insertUser(User user);
    
    /**
     * 根据手机号和密码查询用户
     */
    User selectByPhoneAndPassword(@Param("phone") String phone, @Param("password") String password);
    
    /**
     * 根据ID查询用户
     */
    User selectById(@Param("id") Integer id);
    
    /**
     * 更新用户资料
     */
    int updateUserProfile(@Param("id") Integer id,
                         @Param("phone") String phone,
                         @Param("name") String name,
                         @Param("gender") String gender,
                         @Param("idCard") String idCard,
                         @Param("communityName") String communityName,
                         @Param("buildingNumber") String buildingNumber,
                         @Param("unitNumber") String unitNumber,
                         @Param("roomNumber") String roomNumber,
                         @Param("areaSize") java.math.BigDecimal areaSize,
                         @Param("isVerified") Boolean isVerified);
    
    /**
     * 检查手机号是否已被其他用户使用
     */
    Integer selectIdByPhoneExcludeId(@Param("phone") String phone, @Param("excludeId") Integer excludeId);
    
    /**
     * 检查用户是否已对某个议题投票
     */
    Integer checkUserVoted(@Param("userId") Integer userId, @Param("questionId") Integer questionId);
    
    /**
     * 插入用户投票记录
     */
    int insertUserVote(@Param("userId") Integer userId,
                      @Param("activityId") Integer activityId,
                      @Param("questionId") Integer questionId,
                      @Param("optionId") Integer optionId,
                      @Param("voteMethod") String voteMethod,
                      @Param("voteTime") java.util.Date voteTime,
                      @Param("areaSize") java.math.BigDecimal areaSize);
    
    /**
     * 根据选项文本查找选项ID
     */
    Integer findOptionIdByText(@Param("questionId") Integer questionId, @Param("optionText") String optionText);
}


