package com.orrange.user.mapper;

import com.orrange.user.entity.UserVote;
import com.orrange.user.vo.VoteHistoryItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserVotesMapper {
    int insert(UserVote vote);

    int countByUserAndQuestion(@Param("userId") Integer userId,
                               @Param("activityId") Integer activityId,
                               @Param("questionId") Integer questionId);

    List<VoteHistoryItemVO> selectHistory(@Param("userId") Integer userId,
                                          @Param("activityId") Integer activityId,
                                          @Param("offset") Integer offset,
                                          @Param("limit") Integer limit);

    int countHistory(@Param("userId") Integer userId,
                     @Param("activityId") Integer activityId);
}
