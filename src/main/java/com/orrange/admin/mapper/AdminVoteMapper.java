package com.orrange.admin.mapper;

import com.orrange.admin.dto.AdminVoteQuery;
import com.orrange.admin.vo.AdminVoteItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminVoteMapper {
    int countVotes(@Param("q") AdminVoteQuery q);
    List<AdminVoteItemVO> listVotes(@Param("q") AdminVoteQuery q, @Param("offset") int offset, @Param("limit") int limit);
}
