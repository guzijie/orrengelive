package com.orrange.user.controller;

import com.orrange.common.response.Result;
import com.orrange.user.service.VoteActivityService;
import com.orrange.user.vo.VoteActivityVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class VoteActivityController {
    @Autowired
    private VoteActivityService voteActivityService;

    @GetMapping("/vote")
    public Result<List<VoteActivityVO>> getVoteActivities() {
        List<VoteActivityVO> voList = voteActivityService.getVoteActivities();
        if (voList == null || voList.isEmpty()) {
            return Result.error(400, "目前暂无投票");
        }
        return Result.success(voList);
    }
} 