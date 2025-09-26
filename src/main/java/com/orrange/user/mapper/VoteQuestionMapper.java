package com.orrange.user.mapper;

import com.orrange.user.entity.VoteQuestion;
import com.orrange.user.entity.VoteOptionSetItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface VoteQuestionMapper {
    
    /**
     * 根据活动ID查询议题列表
     */
    List<VoteQuestion> selectByActivityId(@Param("activityId") Integer activityId);

    /**
     * 根据问题ID查询
     */
    VoteQuestion selectById(@Param("id") Integer id);
    
    /**
     * 根据选项模板ID查询选项列表
     */
    List<VoteOptionSetItem> selectOptionsBySetId(@Param("setId") Integer setId);

    /**
     * 根据模板ID与匹配字符串查找选项（支持文本、代码或包含代码的组合文本）
     */
    VoteOptionSetItem selectOptionBySetIdAndMatch(@Param("setId") Integer setId,
                                                  @Param("match") String match);
}
