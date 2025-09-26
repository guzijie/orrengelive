package com.orrange.admin.mapper;

import com.orrange.admin.dto.VoteQuestionInsertDTO;
import com.orrange.user.entity.VoteActivity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminVoteQuestionMapper {
    
    /**
     * 根据活动ID查询活动信息
     */
    VoteActivity selectVoteActivityById(@Param("id") Integer id);
    
    /**
     * 插入议题
     */
    int insertVoteQuestion(@Param("activityId") Integer activityId, 
                          @Param("questionText") String questionText,
                          @Param("sortOrder") Integer sortOrder,
                          @Param("optionSetId") Integer optionSetId,
                          @Param("startTime") java.util.Date startTime,
                          @Param("endTime") java.util.Date endTime,
                          @Param("attachmentUrl") String attachmentUrl);
    
    /**
     * 插入议题并返回ID
     */
    int insertVoteQuestionAndReturnId(@Param("activityId") Integer activityId, 
                                     @Param("questionText") String questionText,
                                     @Param("sortOrder") Integer sortOrder,
                                     @Param("optionSetId") Integer optionSetId,
                                     @Param("startTime") java.util.Date startTime,
                                     @Param("endTime") java.util.Date endTime,
                                     @Param("attachmentUrl") String attachmentUrl);
    
    /**
     * 插入议题并返回ID（使用DTO）
     */
    int insertVoteQuestionWithDTO(VoteQuestionInsertDTO dto);
    
    /**
     * 获取下一个排序号
     */
    Integer getNextSortOrder(@Param("activityId") Integer activityId);
    
    /**
     * 插入选项模板项
     */
    int insertOptionSetItem(@Param("setId") Integer setId,
                           @Param("optionText") String optionText,
                           @Param("optionCode") String optionCode,
                           @Param("sortOrder") Integer sortOrder);
    
    /**
     * 获取下一个选项模板ID
     */
    Integer getNextOptionSetId();
    
    /**
     * 批量插入选项模板项
     */
    int batchInsertOptionSetItems(@Param("setId") Integer setId, 
                                 @Param("options") List<String> options);
    
    /**
     * 获取指定活动的最新议题ID
     */
    Integer getLatestQuestionId(@Param("activityId") Integer activityId);
    
    /**
     * 根据活动ID和议题ID查询议题是否存在
     */
    Integer selectQuestionByIdAndActivity(@Param("activityId") Integer activityId, @Param("questionId") Integer questionId);
    
    /**
     * 检查议题是否有投票记录
     */
    Integer countVotesByQuestionId(@Param("questionId") Integer questionId);
    
    /**
     * 删除议题
     */
    int deleteQuestionById(@Param("questionId") Integer questionId);
    
    /**
     * 根据议题ID查询议题信息
     */
    VoteQuestionInsertDTO selectQuestionById(@Param("questionId") Integer questionId);
    
    /**
     * 更新议题信息
     */
    int updateQuestionById(@Param("questionId") Integer questionId,
                          @Param("questionText") String questionText,
                          @Param("sortOrder") Integer sortOrder,
                          @Param("optionSetId") Integer optionSetId,
                          @Param("startTime") java.util.Date startTime,
                          @Param("endTime") java.util.Date endTime,
                          @Param("attachmentUrl") String attachmentUrl);
    
    /**
     * 删除议题的所有选项
     */
    int deleteQuestionOptions(@Param("questionId") Integer questionId);
    
    /**
     * 获取议题的选项模板ID
     */
    Integer getQuestionOptionSetId(@Param("questionId") Integer questionId);
}
