package com.orrange.admin.service;

import com.orrange.admin.dto.AdminVoteQuery;
import com.orrange.admin.vo.AdminVoteItemVO;

import java.util.List;
import java.util.Map;

public interface AdminVoteService {
    Map<String, Object> listVotes(AdminVoteQuery query);
}
