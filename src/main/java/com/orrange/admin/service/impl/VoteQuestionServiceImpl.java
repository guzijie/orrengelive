package com.orrange.admin.service.impl;

import com.orrange.admin.dto.CreateVoteQuestionDTO;
import com.orrange.admin.dto.UpdateVoteQuestionDTO;
import com.orrange.admin.dto.VoteQuestionInsertDTO;
import com.orrange.admin.mapper.AdminVoteQuestionMapper;
import com.orrange.admin.service.VoteQuestionService;
import com.orrange.admin.vo.CreateVoteQuestionResultVO;
import com.orrange.user.entity.VoteActivity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class VoteQuestionServiceImpl implements VoteQuestionService {
    
    @Autowired
    private AdminVoteQuestionMapper adminVoteQuestionMapper;
    
    @Override
    @Transactional
    public CreateVoteQuestionResultVO createVoteQuestion(Integer activityId, CreateVoteQuestionDTO dto) {
        // 1. 验证活动是否存在
        VoteActivity activity = adminVoteQuestionMapper.selectVoteActivityById(activityId);
        if (activity == null) {
            throw new RuntimeException("活动不存在或已删除");
        }
        
        // 2. 验证活动状态（不能是已结束或已取消）
        Date now = new Date();
        if (now.after(activity.getEndTime())) {
            throw new RuntimeException("活动状态不允许新增议题（已结束/已取消）");
        }
        
        // 3. 验证议题时间（如果提供了时间）
        if (dto.getStartTime() != null && dto.getEndTime() != null) {
            // 议题时间需落在活动时间范围内
            if (dto.getStartTime().before(activity.getStartTime()) || 
                dto.getStartTime().after(activity.getEndTime()) ||
                dto.getEndTime().before(activity.getStartTime()) || 
                dto.getEndTime().after(activity.getEndTime())) {
                throw new RuntimeException("议题时间需落在活动时间范围内，且结束晚于开始");
            }
            
            // 议题结束时间需晚于开始时间
            if (dto.getEndTime().before(dto.getStartTime()) || 
                dto.getEndTime().equals(dto.getStartTime())) {
                throw new RuntimeException("议题时间需落在活动时间范围内，且结束晚于开始");
            }
        }
        
        // 4. 处理排序号
        Integer sortOrder = dto.getSortOrder();
        if (sortOrder == null) {
            sortOrder = adminVoteQuestionMapper.getNextSortOrder(activityId);
        }
        
        // 5. 处理选项
        Integer optionSetId;
        if (dto.getOptions() != null && !dto.getOptions().isEmpty()) {
            // 验证选项
            List<String> options = dto.getOptions();
            if (options.size() < 2) {
                throw new RuntimeException("options 非法：至少 2 个不重复的非空选项");
            }
            
            // 检查选项是否重复或为空
            for (String option : options) {
                if (option == null || option.trim().isEmpty()) {
                    throw new RuntimeException("options 非法：至少 2 个不重复的非空选项");
                }
            }
            
            // 检查重复
            long distinctCount = options.stream().distinct().count();
            if (distinctCount != options.size()) {
                throw new RuntimeException("options 非法：至少 2 个不重复的非空选项");
            }
            
            // 创建新的选项模板
            optionSetId = adminVoteQuestionMapper.getNextOptionSetId();
            adminVoteQuestionMapper.batchInsertOptionSetItems(optionSetId, options);
        } else {
            // 使用默认的选项模板（假设ID为1是默认的赞同/反对/弃权模板）
            optionSetId = 1;
        }
        
        // 6. 插入议题
        VoteQuestionInsertDTO insertDTO = new VoteQuestionInsertDTO(
            activityId,
            dto.getQuestionText(),
            sortOrder,
            optionSetId,
            dto.getStartTime(),
            dto.getEndTime(),
            dto.getAttachmentUrl()
        );
        
        int result = adminVoteQuestionMapper.insertVoteQuestionWithDTO(insertDTO);
        
        if (result <= 0) {
            throw new RuntimeException("创建议题失败");
        }
        
        // 7. 返回结果
        // 生成的ID会自动设置到insertDTO.questionId中
        return new CreateVoteQuestionResultVO(activityId, insertDTO.getQuestionId());
    }
    
    @Override
    @Transactional
    public void deleteVoteQuestion(Integer activityId, Integer questionId) {
        // 1. 验证活动是否存在
        VoteActivity activity = adminVoteQuestionMapper.selectVoteActivityById(activityId);
        if (activity == null) {
            throw new RuntimeException("活动不存在");
        }
        
        // 2. 验证议题是否存在且属于该活动
        Integer questionExists = adminVoteQuestionMapper.selectQuestionByIdAndActivity(activityId, questionId);
        if (questionExists == null) {
            throw new RuntimeException("议题不存在或不属于该活动");
        }
        
        // 3. 检查议题是否有投票记录
        Integer voteCount = adminVoteQuestionMapper.countVotesByQuestionId(questionId);
        if (voteCount != null && voteCount > 0) {
            throw new RuntimeException("议题已有投票记录，禁止删除（请改为取消）");
        }
        
        // 4. 删除议题
        int result = adminVoteQuestionMapper.deleteQuestionById(questionId);
        if (result <= 0) {
            throw new RuntimeException("删除议题失败");
        }
    }
    
    @Override
    @Transactional
    public void updateVoteQuestion(UpdateVoteQuestionDTO dto) {
        // 1. 验证活动是否存在
        VoteActivity activity = adminVoteQuestionMapper.selectVoteActivityById(dto.getActivityId());
        if (activity == null) {
            throw new RuntimeException("活动不存在");
        }
        
        // 2. 验证议题是否存在且属于该活动
        VoteQuestionInsertDTO existingQuestion = adminVoteQuestionMapper.selectQuestionById(dto.getQuestionId());
        if (existingQuestion == null || !existingQuestion.getActivityId().equals(dto.getActivityId())) {
            throw new RuntimeException("议题不存在或不属于该活动");
        }
        
        // 3. 检查活动状态和该议题的投票记录
        Date now = new Date();
        boolean isActivityStarted = now.after(activity.getStartTime());
        boolean hasVotesForThisQuestion = adminVoteQuestionMapper.countVotesByQuestionId(dto.getQuestionId()) > 0;
        boolean canModifyKeyFields = !isActivityStarted && !hasVotesForThisQuestion;
        
        // 4. 验证时间字段（如果提供了）
        if (dto.getStartTime() != null && dto.getEndTime() != null) {
            // 议题时间需落在活动时间范围内
            if (dto.getStartTime().before(activity.getStartTime()) || 
                dto.getStartTime().after(activity.getEndTime()) ||
                dto.getEndTime().before(activity.getStartTime()) || 
                dto.getEndTime().after(activity.getEndTime())) {
                throw new RuntimeException("议题时间需落在活动时间范围内");
            }
            
            // 议题结束时间需晚于开始时间
            if (dto.getEndTime().before(dto.getStartTime()) || 
                dto.getEndTime().equals(dto.getStartTime())) {
                throw new RuntimeException("参数不合法（结束时间需大于开始时间）");
            }
        }
        
        // 5. 检查修改限制
        if (!canModifyKeyFields) {
            // 如果活动已开始或该议题已有投票，只允许修改attachmentUrl
            if (dto.getQuestionText() != null || dto.getSortOrder() != null || 
                dto.getOptions() != null || dto.getStartTime() != null || dto.getEndTime() != null) {
                throw new RuntimeException("活动已开始或该议题已有投票，禁止修改关键字段");
            }
        }
        
        // 6. 处理选项更新（如果需要）
        Integer optionSetId = existingQuestion.getOptionSetId();
        if (dto.getOptions() != null && !dto.getOptions().isEmpty()) {
            // 验证选项
            List<String> options = dto.getOptions();
            if (options.size() < 2) {
                throw new RuntimeException("options 非法：至少 2 个不重复的非空选项");
            }
            
            // 检查选项是否重复或为空
            for (String option : options) {
                if (option == null || option.trim().isEmpty()) {
                    throw new RuntimeException("options 非法：至少 2 个不重复的非空选项");
                }
            }
            
            // 检查重复
            long distinctCount = options.stream().distinct().count();
            if (distinctCount != options.size()) {
                throw new RuntimeException("options 非法：至少 2 个不重复的非空选项");
            }
            
            // 删除原有选项并创建新选项
            adminVoteQuestionMapper.deleteQuestionOptions(dto.getQuestionId());
            optionSetId = adminVoteQuestionMapper.getNextOptionSetId();
            adminVoteQuestionMapper.batchInsertOptionSetItems(optionSetId, options);
        }
        
        // 7. 更新议题信息
        int result = adminVoteQuestionMapper.updateQuestionById(
            dto.getQuestionId(),
            dto.getQuestionText() != null ? dto.getQuestionText() : existingQuestion.getQuestionText(),
            dto.getSortOrder() != null ? dto.getSortOrder() : existingQuestion.getSortOrder(),
            optionSetId,
            dto.getStartTime() != null ? dto.getStartTime() : existingQuestion.getStartTime(),
            dto.getEndTime() != null ? dto.getEndTime() : existingQuestion.getEndTime(),
            dto.getAttachmentUrl() != null ? dto.getAttachmentUrl() : existingQuestion.getAttachmentUrl()
        );
        
        if (result <= 0) {
            throw new RuntimeException("修改议题失败");
        }
    }
}
