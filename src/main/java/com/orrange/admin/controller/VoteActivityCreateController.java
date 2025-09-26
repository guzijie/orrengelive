package com.orrange.admin.controller;

import com.orrange.admin.dto.CreateVoteActivityDTO;
import com.orrange.admin.service.VoteActivityCreateService;
import com.orrange.admin.vo.CreateVoteActivityResultVO;
import com.orrange.common.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/admin")
public class VoteActivityCreateController {

    @Autowired
    private VoteActivityCreateService voteActivityCreateService;

    @PostMapping("/votes")
    public Result<CreateVoteActivityResultVO> createVoteActivity(@Valid @RequestBody CreateVoteActivityDTO dto) {
        try {
            CreateVoteActivityResultVO result = voteActivityCreateService.createVoteActivity(dto);
            return Result.success(result);
        } catch (RuntimeException e) {
            String message = e.getMessage();
            if ("参数不合法".equals(message) || "结束时间必须大于开始时间".equals(message) || 
                "voteScope必须是ALL或PARTIAL".equals(message) || "小区名不能为空".equals(message) ||
                "ALL范围不得携带scopeItems".equals(message) || "PARTIAL范围必须提供scopeItems".equals(message) ||
                "PARTIAL范围的scopeItems必须包含buildingNumber".equals(message)) {
                return Result.error(400, message);
            } else if ("同名活动已存在".equals(message) || "时间区间冲突".equals(message)) {
                return Result.error(409, message);
            } else if ("创建活动失败".equals(message)) {
                return Result.error(500, "创建活动失败，请重试");
            } else {
                return Result.error(400, message);
            }
        } catch (Exception e) {
            return Result.error(500, "系统错误：" + e.getMessage());
        }
    }

}
