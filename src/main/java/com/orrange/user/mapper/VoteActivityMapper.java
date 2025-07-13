package com.orrange.user.mapper;

import com.orrange.user.entity.VoteActivity;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface VoteActivityMapper {
    List<VoteActivity> selectAllVoteActivities();
} 