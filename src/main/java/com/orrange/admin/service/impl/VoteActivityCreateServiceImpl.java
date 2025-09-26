package com.orrange.admin.service.impl;

import com.orrange.admin.dto.CreateVoteActivityDTO;
import com.orrange.admin.entity.VoteActivityCreate;
import com.orrange.admin.mapper.VoteActivityCreateMapper;
import com.orrange.admin.service.VoteActivityCreateService;
import com.orrange.admin.vo.CreateVoteActivityResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class VoteActivityCreateServiceImpl implements VoteActivityCreateService {

    @Autowired
    private VoteActivityCreateMapper voteActivityCreateMapper;

    @Override
    @Transactional
    public CreateVoteActivityResultVO createVoteActivity(CreateVoteActivityDTO dto) {
        // 1. 参数验证
        validateCreateVoteActivity(dto);
        
        // 2. 检查标题重复
        if (voteActivityCreateMapper.checkTitleExists(dto.getTitle()) > 0) {
            throw new RuntimeException("同名活动已存在");
        }
        
        // 3. 检查时间区间冲突
        if (voteActivityCreateMapper.checkTimeConflict(dto.getStartTime(), dto.getEndTime(), dto.getCommunityName()) > 0) {
            throw new RuntimeException("时间区间冲突");
        }
        
        // 4. 插入投票活动
        VoteActivityCreate voteActivity = new VoteActivityCreate(
            dto.getTitle(),
            dto.getAttachmentUrl(),
            dto.getStartTime(),
            dto.getEndTime(),
            dto.getIsOfficial() != null ? dto.getIsOfficial() : 0,
            dto.getVoteScope(),
            dto.getCommunityName()
        );
        
        int result = voteActivityCreateMapper.insertVoteActivity(voteActivity);
        
        if (result <= 0) {
            throw new RuntimeException("创建活动失败");
        }
        
        // 获取插入后的ID
        Integer activityId = voteActivity.getId();
        
        // 5. 如果是PARTIAL范围，插入范围项
        if ("PARTIAL".equals(dto.getVoteScope()) || "partial".equalsIgnoreCase(dto.getVoteScope())) {
            if (dto.getScopeItems() == null || dto.getScopeItems().isEmpty()) {
                throw new RuntimeException("PARTIAL范围必须提供scopeItems");
            }
            
            for (CreateVoteActivityDTO.ScopeItemDTO scopeItem : dto.getScopeItems()) {
                if (scopeItem.getBuildingNumber() == null || scopeItem.getBuildingNumber().trim().isEmpty()) {
                    throw new RuntimeException("PARTIAL范围的scopeItems必须包含buildingNumber");
                }
                
                // 插入栋号
                voteActivityCreateMapper.insertVoteActivityScope(
                    activityId, 
                    scopeItem.getBuildingNumber(), 
                    null
                );
                
                // 如果有单元号，插入单元号
                if (scopeItem.getUnitNumbers() != null && !scopeItem.getUnitNumbers().isEmpty()) {
                    for (String unitNumber : scopeItem.getUnitNumbers()) {
                        voteActivityCreateMapper.insertVoteActivityScope(
                            activityId, 
                            scopeItem.getBuildingNumber(), 
                            unitNumber
                        );
                    }
                }
            }
        }
        
        return new CreateVoteActivityResultVO(activityId);
    }
    
    private void validateCreateVoteActivity(CreateVoteActivityDTO dto) {
        // 验证时间
        if (dto.getStartTime() == null || dto.getEndTime() == null) {
            throw new RuntimeException("开始时间和结束时间不能为空");
        }
        
        if (dto.getEndTime().before(dto.getStartTime()) || dto.getEndTime().equals(dto.getStartTime())) {
            throw new RuntimeException("结束时间必须大于开始时间");
        }
        
        // 验证投票范围
        if (!"ALL".equals(dto.getVoteScope()) && !"PARTIAL".equals(dto.getVoteScope()) 
            && !"all".equalsIgnoreCase(dto.getVoteScope()) && !"partial".equalsIgnoreCase(dto.getVoteScope())) {
            throw new RuntimeException("voteScope必须是ALL或PARTIAL");
        }
        
        // 验证小区名
        if (dto.getCommunityName() == null || dto.getCommunityName().trim().isEmpty()) {
            throw new RuntimeException("小区名不能为空");
        }
        
        // 验证范围逻辑
        if ("ALL".equals(dto.getVoteScope()) || "all".equalsIgnoreCase(dto.getVoteScope())) {
            if (dto.getScopeItems() != null && !dto.getScopeItems().isEmpty()) {
                throw new RuntimeException("ALL范围不得携带scopeItems");
            }
        } else if ("PARTIAL".equals(dto.getVoteScope()) || "partial".equalsIgnoreCase(dto.getVoteScope())) {
            if (dto.getScopeItems() == null || dto.getScopeItems().isEmpty()) {
                throw new RuntimeException("PARTIAL范围必须提供scopeItems");
            }
        }
    }
    
    @Override
    @Transactional
    public void deleteVoteActivity(Integer activityId) {
        // 1. 检查活动是否存在
        if (voteActivityCreateMapper.checkActivityExists(activityId) == 0) {
            throw new RuntimeException("活动不存在");
        }
        
        // 2. 检查是否有投票记录
        if (voteActivityCreateMapper.checkVoteRecordsExist(activityId) > 0) {
            throw new RuntimeException("活动已有投票记录，禁止删除（请改为取消）");
        }
        
        // 3. 物理删除相关数据（按依赖关系顺序删除）
        // 3.1 删除投票活动范围项
        voteActivityCreateMapper.deleteVoteActivityScopes(activityId);
        
        // 3.2 删除投票问题
        voteActivityCreateMapper.deleteVoteQuestions(activityId);
        
        // 3.3 删除投票活动
        int result = voteActivityCreateMapper.deleteVoteActivity(activityId);
        
        if (result <= 0) {
            throw new RuntimeException("删除活动失败");
        }
    }
    
}
