package com.orrange.user.service.impl;

import com.orrange.user.entity.VoteActivity;
import com.orrange.user.mapper.VoteActivityMapper;
import com.orrange.user.service.VoteActivityService;
import com.orrange.user.vo.VoteActivityVO;
import com.orrange.common.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;

@Service
public class VoteActivityServiceImpl implements VoteActivityService {
    @Autowired
    private VoteActivityMapper voteActivityMapper;

    @Override
    public List<VoteActivityVO> getVoteActivities() {
        // 从数据库获取所有投票活动信息
        List<VoteActivity> activities = voteActivityMapper.selectAllVoteActivities();
        if (activities == null || activities.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 转换为VO列表
        List<VoteActivityVO> voList = new ArrayList<>();
        for (VoteActivity activity : activities) {
            VoteActivityVO vo = new VoteActivityVO();
            BeanCopyUtils.copy(activity, vo);
            voList.add(vo);
        }
        return voList;
    }
} 