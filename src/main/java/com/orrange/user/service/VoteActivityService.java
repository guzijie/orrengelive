package com.orrange.user.service;

import com.orrange.user.dto.VoteActivityQueryDTO;
import com.orrange.user.dto.VoteSubmitDTO;
import com.orrange.user.dto.VoteHistoryQueryDTO;
import com.orrange.user.vo.PageResultVO;
import com.orrange.user.vo.VoteActivityVO;
import com.orrange.user.vo.VoteActivityDetailVO;
import com.orrange.user.vo.VoteHistoryItemVO;

public interface VoteActivityService {
    
    /**
     * 获取投票活动列表（带分页和筛选）
     */
    PageResultVO<VoteActivityVO> getVoteActivities(VoteActivityQueryDTO query);
    
    /**
     * 获取投票活动详情（包含议题和选项）
     */
    VoteActivityDetailVO getVoteActivityDetail(Integer activityId);

    /**
     * 提交投票
     */
    void submitVote(Integer currentUserId, VoteSubmitDTO dto);

    /**
     * 获取投票历史
     */
    PageResultVO<VoteHistoryItemVO> getVoteHistory(Integer currentUserId, VoteHistoryQueryDTO query);
}