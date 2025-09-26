package com.orrange.admin.service;

import com.orrange.admin.dto.CreateVoteQuestionDTO;
import com.orrange.admin.dto.UpdateVoteQuestionDTO;
import com.orrange.admin.vo.CreateVoteQuestionResultVO;

public interface VoteQuestionService {
    
    /**
     * 新增投票议题
     * @param activityId 活动ID
     * @param dto 议题信息
     * @return 创建结果
     */
    CreateVoteQuestionResultVO createVoteQuestion(Integer activityId, CreateVoteQuestionDTO dto);
    
    /**
     * 删除投票议题
     * @param activityId 活动ID
     * @param questionId 议题ID
     */
    void deleteVoteQuestion(Integer activityId, Integer questionId);
    
    /**
     * 修改投票议题
     * @param dto 修改议题信息
     */
    void updateVoteQuestion(UpdateVoteQuestionDTO dto);
}
