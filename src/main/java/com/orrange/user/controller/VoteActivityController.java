package com.orrange.user.controller;

import com.orrange.common.response.Result;
import com.orrange.user.dto.VoteActivityQueryDTO;
import com.orrange.user.service.VoteActivityService;
import com.orrange.user.vo.PageResultVO;
import com.orrange.user.vo.VoteActivityVO;
import com.orrange.user.vo.VoteActivityDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class VoteActivityController {
    @Autowired
    private VoteActivityService voteActivityService;

    @GetMapping("/vote")
    public Result<PageResultVO<VoteActivityVO>> getVoteActivities(VoteActivityQueryDTO query) {
        PageResultVO<VoteActivityVO> page = voteActivityService.getVoteActivities(query);
        if (page.getData() == null || page.getData().isEmpty()) {
            return Result.error(400, "目前暂无投票");
        }
        return Result.success(page);
    }

    @GetMapping("/vote/{activityId}")
    public Result<VoteActivityDetailVO> getVoteActivityDetail(@PathVariable Integer activityId) {
        try {
            VoteActivityDetailVO detail = voteActivityService.getVoteActivityDetail(activityId);
            if (detail == null) {
                return Result.error(400, "目前无该投票信息");
            }
            return Result.success(detail);
        } catch (Exception e) {
            return Result.error(500, "查询失败：" + e.getMessage());
        }
    }
} 