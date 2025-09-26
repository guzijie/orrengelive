package com.orrange.admin.service;

import com.orrange.admin.vo.AdminVoteDetailVO;

public interface AdminVoteDetailService {
    AdminVoteDetailVO getDetail(Integer id, Boolean withStats, Boolean withQuestions);
}
