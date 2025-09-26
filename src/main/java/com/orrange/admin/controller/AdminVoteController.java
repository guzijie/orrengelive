package com.orrange.admin.controller;

import com.orrange.admin.dto.AdminVoteQuery;
import com.orrange.admin.dto.CreateVoteQuestionDTO;
import com.orrange.admin.dto.UpdateVoteQuestionDTO;
import com.orrange.admin.service.AdminVoteService;
import com.orrange.admin.service.AdminVoteDetailService;
import com.orrange.admin.service.VoteActivityCreateService;
import com.orrange.admin.service.VoteQuestionService;
import com.orrange.admin.vo.AdminVoteDetailVO;
import com.orrange.admin.vo.CreateVoteQuestionResultVO;
import com.orrange.common.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminVoteController {
    @Autowired
    private AdminVoteService adminVoteService;
    @Autowired
    private AdminVoteDetailService adminVoteDetailService;
    @Autowired
    private VoteActivityCreateService voteActivityCreateService;
    @Autowired
    private VoteQuestionService voteQuestionService;

    @GetMapping("/votes")
    public Result<Map<String, Object>> listVotes(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "communityName", required = false) String communityName,
            @RequestParam(value = "startFrom", required = false) String startFrom,
            @RequestParam(value = "endTo", required = false) String endTo,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "pageSize", required = false) Integer pageSize
    ) {
        AdminVoteQuery q = new AdminVoteQuery();
        q.setKeyword(keyword);
        q.setStatus(status);
        q.setCommunityName(communityName);
        q.setStartFrom(startFrom);
        q.setEndTo(endTo);
        q.setPage(page);
        q.setPageSize(pageSize);
        Map<String, Object> payload = adminVoteService.listVotes(q);
        return Result.success(payload);
    }

    @GetMapping("/votes/{id}")
    public Result<AdminVoteDetailVO> getVoteDetail(
            @PathVariable("id") Integer id,
            @RequestParam(value = "withStats", required = false, defaultValue = "true") Boolean withStats,
            @RequestParam(value = "withQuestions", required = false, defaultValue = "true") Boolean withQuestions
    ) {
        AdminVoteDetailVO detail = adminVoteDetailService.getDetail(id, withStats, withQuestions);
        if (detail == null) {
            return Result.error(404, "未找到该投票活动");
        }
        return Result.success(detail);
    }

    @PostMapping("/votes/{activityId}/questions")
    public Result<CreateVoteQuestionResultVO> createVoteQuestion(
            @PathVariable("activityId") Integer activityId,
            @RequestBody CreateVoteQuestionDTO dto
    ) {
        try {
            CreateVoteQuestionResultVO result = voteQuestionService.createVoteQuestion(activityId, dto);
            return Result.success(result);
        } catch (RuntimeException e) {
            String message = e.getMessage();
            if ("活动不存在或已删除".equals(message)) {
                return Result.error(404, message);
            } else if ("活动状态不允许新增议题（已结束/已取消）".equals(message)) {
                return Result.error(409, message);
            } else if (message.contains("options 非法")) {
                return Result.error(400, message);
            } else if (message.contains("议题时间需落在活动时间范围内")) {
                return Result.error(400, message);
            } else {
                return Result.error(400, message);
            }
        } catch (Exception e) {
            return Result.error(500, "系统错误：" + e.getMessage());
        }
    }

    @DeleteMapping("/votes/{activityId}/questions/{questionId}")
    public Result<?> deleteVoteQuestion(
            @PathVariable("activityId") Integer activityId,
            @PathVariable("questionId") Integer questionId
    ) {
        try {
            voteQuestionService.deleteVoteQuestion(activityId, questionId);
            return Result.success(null);
        } catch (RuntimeException e) {
            String message = e.getMessage();
            if ("活动不存在".equals(message)) {
                return Result.error(404, message);
            } else if ("议题已有投票记录，禁止删除（请改为取消）".equals(message)) {
                return Result.error(409, message);
            } else if ("议题不存在或不属于该活动".equals(message)) {
                return Result.error(404, message);
            } else {
                return Result.error(400, message);
            }
        } catch (Exception e) {
            return Result.error(500, "系统错误：" + e.getMessage());
        }
    }

    @PutMapping("/votes/questions/{questionId}")
    public Result<?> updateVoteQuestion(
            @PathVariable("questionId") Integer questionId,
            @RequestBody UpdateVoteQuestionDTO dto
    ) {
        try {
            // 设置questionId到DTO中
            dto.setQuestionId(questionId);
            voteQuestionService.updateVoteQuestion(dto);
            return Result.success(null);
        } catch (RuntimeException e) {
            String message = e.getMessage();
            if ("活动不存在".equals(message)) {
                return Result.error(404, message);
            } else if ("议题不存在或不属于该活动".equals(message)) {
                return Result.error(404, message);
            } else if ("活动已开始或该议题已有投票，禁止修改关键字段".equals(message)) {
                return Result.error(403, message);
            } else if (message.contains("参数不合法") || message.contains("议题时间需落在活动时间范围内")) {
                return Result.error(400, message);
            } else if (message.contains("options 非法")) {
                return Result.error(400, message);
            } else {
                return Result.error(400, message);
            }
        } catch (Exception e) {
            return Result.error(500, "系统错误：" + e.getMessage());
        }
    }

    @DeleteMapping("/votes/{id}")
    public Result<?> deleteVoteActivity(@PathVariable("id") Integer activityId) {
        try {
            voteActivityCreateService.deleteVoteActivity(activityId);
            return Result.success(null);
        } catch (RuntimeException e) {
            String message = e.getMessage();
            if ("活动不存在".equals(message)) {
                return Result.error(404, message);
            } else if ("活动已有投票记录，禁止删除（请改为取消）".equals(message)) {
                return Result.error(409, message);
            } else if ("删除活动失败".equals(message)) {
                return Result.error(500, "删除活动失败，请重试");
            } else {
                return Result.error(400, message);
            }
        } catch (Exception e) {
            return Result.error(500, "系统错误：" + e.getMessage());
        }
    }
}
