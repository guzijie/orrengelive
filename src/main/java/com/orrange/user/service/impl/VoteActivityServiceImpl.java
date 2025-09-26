package com.orrange.user.service.impl;

import com.orrange.user.dto.VoteActivityQueryDTO;
import com.orrange.user.dto.VoteSubmitDTO;
import com.orrange.user.dto.VoteHistoryQueryDTO;
import com.orrange.user.entity.VoteActivity;
import com.orrange.user.entity.VoteQuestion;
import com.orrange.user.entity.VoteOptionSetItem;
import com.orrange.user.entity.User;
import com.orrange.user.entity.UserVote;
import com.orrange.user.mapper.VoteActivityMapper;
import com.orrange.user.mapper.VoteQuestionMapper;
import com.orrange.user.mapper.UserMapper;
import com.orrange.user.mapper.UserVotesMapper;
import com.orrange.user.service.VoteActivityService;
import com.orrange.user.vo.PageResultVO;
import com.orrange.user.vo.PageMetaVO;
import com.orrange.user.vo.VoteActivityVO;
import com.orrange.user.vo.VoteActivityDetailVO;
import com.orrange.user.vo.QuestionDetailVO;
import com.orrange.user.vo.VoteHistoryItemVO;
import com.orrange.common.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class VoteActivityServiceImpl implements VoteActivityService {
    
    @Autowired
    private VoteActivityMapper voteActivityMapper;
    
    @Autowired
    private VoteQuestionMapper voteQuestionMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserVotesMapper userVotesMapper;

    @Override
    public PageResultVO<VoteActivityVO> getVoteActivities(VoteActivityQueryDTO query) {
        if (query.getPage() == null || query.getPage() <= 0) {
            query.setPage(1);
        }
        if (query.getPageSize() == null || query.getPageSize() <= 0) {
            query.setPageSize(10);
        }
        List<VoteActivity> activities = voteActivityMapper.selectVoteActivities(query);
        int total = voteActivityMapper.countVoteActivities(query);
        List<VoteActivityVO> voList = BeanCopyUtils.copyBeanList(activities, VoteActivityVO.class);
        for (int i = 0; i < voList.size(); i++) {
            VoteActivityVO vo = voList.get(i);
            VoteActivity activity = activities.get(i);
            // 根据新的 vote_scope 字段设置投票范围
            if ("ALL".equals(activity.getVoteScope())) {
                vo.setVoteScope(activity.getCommunityName());
            } else if ("PARTIAL".equals(activity.getVoteScope())) {
                // 对于 PARTIAL 范围，需要从 vote_activity_scopes 表获取具体范围
                // 这里简化处理，显示小区名 + "部分楼栋"
                vo.setVoteScope(activity.getCommunityName() + " 部分楼栋");
            } else {
                vo.setVoteScope(activity.getCommunityName());
            }
            if (activity.getCreatedAt() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                vo.setCreatedAt(sdf.format(activity.getCreatedAt()));
            }
        }
        PageMetaVO pageMeta = new PageMetaVO(query.getPage(), query.getPageSize(), total);
        return new PageResultVO<>(voList, pageMeta);
    }

    @Override
    public VoteActivityDetailVO getVoteActivityDetail(Integer activityId) {
        VoteActivity activity = voteActivityMapper.selectById(activityId);
        if (activity == null) {
            return null;
        }
        VoteActivityDetailVO detailVO = new VoteActivityDetailVO();
        BeanCopyUtils.copy(activity, detailVO);
        // 根据新的 vote_scope 字段设置投票范围
        if ("ALL".equals(activity.getVoteScope())) {
            detailVO.setVoteScope(activity.getCommunityName());
        } else if ("PARTIAL".equals(activity.getVoteScope())) {
            // 对于 PARTIAL 范围，需要从 vote_activity_scopes 表获取具体范围
            // 这里简化处理，显示小区名 + "部分楼栋"
            detailVO.setVoteScope(activity.getCommunityName() + " 部分楼栋");
        } else {
            detailVO.setVoteScope(activity.getCommunityName());
        }
        if (activity.getCreatedAt() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            detailVO.setCreatedAt(sdf.format(activity.getCreatedAt()));
        }
        List<VoteQuestion> questions = voteQuestionMapper.selectByActivityId(activityId);
        List<QuestionDetailVO> questionVOList = new ArrayList<>();
        for (VoteQuestion question : questions) {
            QuestionDetailVO questionVO = new QuestionDetailVO();
            questionVO.setQuestionsId(question.getId());
            questionVO.setActivityId(question.getVoteActivityId());
            questionVO.setQuestionText(question.getTitle());
            if (question.getCreatedAt() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                questionVO.setCreatedAt(sdf.format(question.getCreatedAt()));
            }
            List<VoteOptionSetItem> options = voteQuestionMapper.selectOptionsBySetId(question.getOptionSetId());
            List<String> optionTexts = new ArrayList<>();
            for (VoteOptionSetItem option : options) {
                optionTexts.add(option.getOptionText());
            }
            questionVO.setOptions(optionTexts);
            questionVO.setMyVote(null);
            questionVOList.add(questionVO);
        }
        detailVO.setQuestions(questionVOList);
        return detailVO;
    }

    @Override
    public void submitVote(Integer currentUserId, VoteSubmitDTO dto) {
        // 基本存在性与归属校验
        VoteActivity activity = voteActivityMapper.selectById(dto.getActivityId());
        if (activity == null) {
            throw new RuntimeException("目前无该投票信息");
        }
        VoteQuestion question = voteQuestionMapper.selectById(dto.getQuestionId());
        if (question == null || !question.getVoteActivityId().equals(dto.getActivityId())) {
            throw new RuntimeException("目前无该投票信息");
        }
        // 时间窗口校验
        Date now = dto.getVoteTime() != null ? dto.getVoteTime() : new Date();
        if (now.before(activity.getStartTime()) || now.after(activity.getEndTime())) {
            throw new RuntimeException("活动未开始或已结束");
        }
        // 范围校验：根据 vote_scope 字段进行验证
        User user = userMapper.selectById(currentUserId);
        if (user == null) {
            throw new RuntimeException("未登录或登录已过期");
        }
        
        // 小区必须相等
        if (activity.getCommunityName() != null && !activity.getCommunityName().equals(user.getCommunityName())) {
            throw new RuntimeException("不在投票范围");
        }
        
        // 根据 vote_scope 进行范围验证
        if ("PARTIAL".equals(activity.getVoteScope())) {
            // 对于 PARTIAL 范围，需要检查用户是否在 vote_activity_scopes 表中指定的范围内
            // 这里简化处理，暂时允许所有该小区的用户投票
            // TODO: 实现完整的 PARTIAL 范围验证逻辑，查询 vote_activity_scopes 表
        }
        // 对于 "ALL" 范围，只要小区匹配就允许投票
        // 重复投票校验
        int voted = userVotesMapper.countByUserAndQuestion(currentUserId, dto.getActivityId(), dto.getQuestionId());
        if (voted > 0) {
            throw new RuntimeException("您已完成该议题投票");
        }
        // 选项解析
        VoteOptionSetItem option = voteQuestionMapper.selectOptionBySetIdAndMatch(question.getOptionSetId(), dto.getSelectedOption());
        if (option == null) {
            throw new RuntimeException("选项不存在");
        }
        // 写入投票记录
        UserVote vote = new UserVote();
        vote.setUserId(currentUserId);
        vote.setVoteActivityId(dto.getActivityId());
        vote.setQuestionId(dto.getQuestionId());
        vote.setOptionId(option.getId());
        // 归一化投票方式
        String method = dto.getVoteMethod();
        if ("线上".equals(method)) method = "online";
        if ("短信".equals(method)) method = "sms";
        if ("线下".equals(method)) method = "offline";
        vote.setVoteMethod(method);
        vote.setVoteTime(now);
        vote.setAreaSize(user.getAreaSize());
        int rows = userVotesMapper.insert(vote);
        if (rows <= 0) {
            throw new RuntimeException("提交失败，请重试");
        }
    }

    @Override
    public PageResultVO<VoteHistoryItemVO> getVoteHistory(Integer currentUserId, VoteHistoryQueryDTO query) {
        if (query.getPage() == null || query.getPage() <= 0) query.setPage(1);
        if (query.getPageSize() == null || query.getPageSize() <= 0) query.setPageSize(10);
        int offset = (query.getPage() - 1) * query.getPageSize();
        List<VoteHistoryItemVO> list = userVotesMapper.selectHistory(currentUserId, query.getActivityId(), offset, query.getPageSize());
        int total = userVotesMapper.countHistory(currentUserId, query.getActivityId());
        PageMetaVO meta = new PageMetaVO(query.getPage(), query.getPageSize(), total);
        return new PageResultVO<>(list, meta);
    }
}