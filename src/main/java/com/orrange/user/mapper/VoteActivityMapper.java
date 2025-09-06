package com.orrange.user.mapper;

import com.orrange.user.entity.VoteActivity;
import com.orrange.user.dto.VoteActivityQueryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface VoteActivityMapper {
    
    /**
     * 根据ID查询投票活动
     */
    VoteActivity selectById(@Param("id") Integer id);
    
    /**
     * 查询投票活动列表（带分页和筛选）
     */
    List<VoteActivity> selectVoteActivities(@Param("query") VoteActivityQueryDTO query);
    
    /**
     * 查询投票活动总数（带筛选条件）
     */
    int countVoteActivities(@Param("query") VoteActivityQueryDTO query);
}