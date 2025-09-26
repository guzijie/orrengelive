package com.orrange.admin.service;

import com.orrange.admin.dto.CreateVoteActivityDTO;
import com.orrange.admin.vo.CreateVoteActivityResultVO;

public interface VoteActivityCreateService {
    
    /**
     * 创建投票活动
     */
    CreateVoteActivityResultVO createVoteActivity(CreateVoteActivityDTO dto);
    
    /**
     * 删除投票活动
     */
    void deleteVoteActivity(Integer activityId);
}
