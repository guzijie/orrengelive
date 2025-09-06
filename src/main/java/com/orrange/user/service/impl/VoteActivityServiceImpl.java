package com.orrange.user.service.impl;

import com.orrange.user.dto.VoteActivityQueryDTO;
import com.orrange.user.entity.VoteActivity;
import com.orrange.user.entity.VoteQuestion;
import com.orrange.user.entity.VoteOptionSetItem;
import com.orrange.user.mapper.VoteActivityMapper;
import com.orrange.user.mapper.VoteQuestionMapper;
import com.orrange.user.service.VoteActivityService;
import com.orrange.user.vo.PageResultVO;
import com.orrange.user.vo.PageMetaVO;
import com.orrange.user.vo.VoteActivityVO;
import com.orrange.user.vo.VoteActivityDetailVO;
import com.orrange.user.vo.QuestionDetailVO;
import com.orrange.common.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class VoteActivityServiceImpl implements VoteActivityService {
    
    @Autowired
    private VoteActivityMapper voteActivityMapper;
    
    @Autowired
    private VoteQuestionMapper voteQuestionMapper;

    @Override
    public PageResultVO<VoteActivityVO> getVoteActivities(VoteActivityQueryDTO query) {
        // 设置默认分页参数
        if (query.getPage() == null || query.getPage() <= 0) {
            query.setPage(1);
        }
        if (query.getPageSize() == null || query.getPageSize() <= 0) {
            query.setPageSize(10);
        }
        
        // 查询数据
        List<VoteActivity> activities = voteActivityMapper.selectVoteActivities(query);
        int total = voteActivityMapper.countVoteActivities(query);
        
        // 转换为VO
        List<VoteActivityVO> voList = BeanCopyUtils.copyBeanList(activities, VoteActivityVO.class);
        
        // 设置voteScope字段
        for (int i = 0; i < voList.size(); i++) {
            VoteActivityVO vo = voList.get(i);
            VoteActivity activity = activities.get(i);
            
            // 构建投票范围字符串
            StringBuilder voteScope = new StringBuilder();
            voteScope.append(activity.getCommunityName());
            if (activity.getBuildingNumber() != null && !activity.getBuildingNumber().trim().isEmpty()) {
                voteScope.append(" ").append(activity.getBuildingNumber()).append("栋");
            }
            if (activity.getUnitNumber() != null && !activity.getUnitNumber().trim().isEmpty()) {
                voteScope.append(" ").append(activity.getUnitNumber()).append("单元");
            }
            vo.setVoteScope(voteScope.toString());
            
            // 格式化创建时间
            if (activity.getCreatedAt() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                vo.setCreatedAt(sdf.format(activity.getCreatedAt()));
            }
        }
        
        // 构建分页信息
        PageMetaVO pageMeta = new PageMetaVO(query.getPage(), query.getPageSize(), total);
        
        return new PageResultVO<>(voList, pageMeta);
    }

    @Override
    public VoteActivityDetailVO getVoteActivityDetail(Integer activityId) {
        // 查询活动基本信息
        VoteActivity activity = voteActivityMapper.selectById(activityId);
        if (activity == null) {
            return null;
        }
        
        // 转换为详情VO
        VoteActivityDetailVO detailVO = new VoteActivityDetailVO();
        BeanCopyUtils.copy(activity, detailVO);
        
        // 构建投票范围
        StringBuilder voteScope = new StringBuilder();
        voteScope.append(activity.getCommunityName());
        if (activity.getBuildingNumber() != null && !activity.getBuildingNumber().trim().isEmpty()) {
            voteScope.append(" ").append(activity.getBuildingNumber()).append("栋");
        }
        if (activity.getUnitNumber() != null && !activity.getUnitNumber().trim().isEmpty()) {
            voteScope.append(" ").append(activity.getUnitNumber()).append("单元");
        }
        detailVO.setVoteScope(voteScope.toString());
        
        // 格式化创建时间
        if (activity.getCreatedAt() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            detailVO.setCreatedAt(sdf.format(activity.getCreatedAt()));
        }
        
        // 查询议题列表
        List<VoteQuestion> questions = voteQuestionMapper.selectByActivityId(activityId);
        List<QuestionDetailVO> questionVOList = new ArrayList<>();
        
        for (VoteQuestion question : questions) {
            QuestionDetailVO questionVO = new QuestionDetailVO();
            questionVO.setQuestionsId(question.getId());
            questionVO.setActivityId(question.getVoteActivityId());
            questionVO.setQuestionText(question.getTitle());
            
            // 格式化议题创建时间
            if (question.getCreatedAt() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                questionVO.setCreatedAt(sdf.format(question.getCreatedAt()));
            }
            
            // 查询选项列表
            List<VoteOptionSetItem> options = voteQuestionMapper.selectOptionsBySetId(question.getOptionSetId());
            List<String> optionTexts = new ArrayList<>();
            for (VoteOptionSetItem option : options) {
                optionTexts.add(option.getOptionText());
            }
            questionVO.setOptions(optionTexts);
            
            // TODO: 查询用户投票记录，设置myVote
            questionVO.setMyVote(null);
            
            questionVOList.add(questionVO);
        }
        
        detailVO.setQuestions(questionVOList);
        return detailVO;
    }
}