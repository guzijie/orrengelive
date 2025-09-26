package com.orrange.admin.mapper;

import com.orrange.admin.vo.AdminVoteDetailVO;
import com.orrange.admin.vo.AdminVoteQuestionVO;
import com.orrange.admin.vo.AdminVoteQuestionOptionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface AdminVoteDetailMapper {
    AdminVoteDetailVO selectBase(@Param("id") Integer id);
    Integer countQuestion(@Param("id") Integer id);
    Integer countTotalVotes(@Param("id") Integer id);
    Integer countEligible(@Param("id") Integer id);
    Integer countParticipants(@Param("id") Integer id);

    List<AdminVoteQuestionVO> listQuestionsBase(@Param("id") Integer id);
    List<AdminVoteQuestionOptionVO> listQuestionOptions(@Param("id") Integer id);
    List<Map<String, Object>> listQuestionOptionsAsMap(@Param("id") Integer id);
}
