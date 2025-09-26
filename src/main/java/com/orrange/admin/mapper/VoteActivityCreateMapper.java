package com.orrange.admin.mapper;

import com.orrange.admin.entity.VoteActivityCreate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface VoteActivityCreateMapper {
    
    /**
     * 插入投票活动
     */
    int insertVoteActivity(VoteActivityCreate voteActivity);
    
    /**
     * 获取最后插入的ID
     */
    Integer getLastInsertId();
    
    /**
     * 插入投票活动范围项
     */
    int insertVoteActivityScope(@Param("activityId") Integer activityId,
                               @Param("buildingNumber") String buildingNumber,
                               @Param("unitNumber") String unitNumber);
    
    /**
     * 检查活动标题是否重复
     */
    int checkTitleExists(@Param("title") String title);
    
    /**
     * 检查时间区间是否冲突
     */
    int checkTimeConflict(@Param("startTime") java.util.Date startTime,
                         @Param("endTime") java.util.Date endTime,
                         @Param("communityName") String communityName);
    
    /**
     * 检查活动是否存在
     */
    int checkActivityExists(@Param("activityId") Integer activityId);
    
    /**
     * 检查活动是否有投票记录
     */
    int checkVoteRecordsExist(@Param("activityId") Integer activityId);
    
    /**
     * 删除投票活动范围项
     */
    int deleteVoteActivityScopes(@Param("activityId") Integer activityId);
    
    /**
     * 删除投票问题
     */
    int deleteVoteQuestions(@Param("activityId") Integer activityId);
    
    /**
     * 删除投票活动
     */
    int deleteVoteActivity(@Param("activityId") Integer activityId);
}
