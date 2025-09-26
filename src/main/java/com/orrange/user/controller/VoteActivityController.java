package com.orrange.user.controller;

import com.orrange.common.response.Result;
import com.orrange.user.dto.VoteActivityQueryDTO;
import com.orrange.user.dto.VoteSubmitDTO;
import com.orrange.user.dto.VoteHistoryQueryDTO;
import com.orrange.user.service.VoteActivityService;
import com.orrange.user.vo.PageResultVO;
import com.orrange.user.vo.VoteActivityVO;
import com.orrange.user.vo.VoteActivityDetailVO;
import com.orrange.user.vo.VoteHistoryItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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

    // 历史列表
    @GetMapping("/vote/history")
    public Result<PageResultVO<VoteHistoryItemVO>> getHistory(VoteHistoryQueryDTO query, HttpServletRequest request) {
        Object uid = request.getAttribute("CURRENT_USER_ID");
        if (uid == null) {
            return Result.error(401, "未登录或登录已过期");
        }
        PageResultVO<VoteHistoryItemVO> page = voteActivityService.getVoteHistory(Integer.parseInt(uid.toString()), query);
        if (page.getData() == null || page.getData().isEmpty()) {
            return Result.error(400, "暂无投票历史");
        }
        return Result.success(page);
    }

    // Query 参数提交
    @PostMapping(value = "/vote", consumes = {"application/x-www-form-urlencoded", "*/*"})
    public Result<?> submitVote(VoteSubmitDTO dto, HttpServletRequest request) {
        try {
            Object uid = request.getAttribute("CURRENT_USER_ID");
            if (uid == null) {
                return Result.error(401, "未登录或登录已过期");
            }
            voteActivityService.submitVote(Integer.parseInt(uid.toString()), dto);
            return Result.success(dto);
        } catch (RuntimeException ex) {
            String m = ex.getMessage();
            if ("活动未开始或已结束".equals(m)) return Result.error(400, m);
            if ("您已完成该议题投票".equals(m)) return Result.error(400, m);
            if ("未登录或登录已过期".equals(m)) return Result.error(401, m);
            if ("不在投票范围".equals(m)) return Result.error(403, m);
            if ("目前无该投票信息".equals(m)) return Result.error(400, m);
            return Result.error(400, m);
        } catch (Exception e) {
            return Result.error(500, "提交失败：" + e.getMessage());
        }
    }

    // JSON 提交
    @PostMapping(value = "/vote", consumes = "application/json")
    public Result<?> submitVoteJson(@org.springframework.web.bind.annotation.RequestBody VoteSubmitDTO dto,
                                    HttpServletRequest request) {
        return submitVote(dto, request);
    }
} 