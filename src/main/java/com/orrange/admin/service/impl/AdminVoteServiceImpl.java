package com.orrange.admin.service.impl;

import com.orrange.admin.dto.AdminVoteQuery;
import com.orrange.admin.mapper.AdminVoteMapper;
import com.orrange.admin.service.AdminVoteService;
import com.orrange.admin.vo.AdminVoteItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminVoteServiceImpl implements AdminVoteService {
    @Autowired
    private AdminVoteMapper adminVoteMapper;

    @Override
    public Map<String, Object> listVotes(AdminVoteQuery query) {
        int page = (query.getPage() == null || query.getPage() <= 0) ? 1 : query.getPage();
        int pageSize = (query.getPageSize() == null || query.getPageSize() <= 0) ? 10 : query.getPageSize();
        if (pageSize > 100) pageSize = 100;
        int offset = (page - 1) * pageSize;

        int total = adminVoteMapper.countVotes(query);
        List<AdminVoteItemVO> data = total == 0 ? java.util.Collections.emptyList() : adminVoteMapper.listVotes(query, offset, pageSize);

        Map<String, Object> result = new HashMap<>();
        result.put("data", data);
        Map<String, Object> pageMeta = new HashMap<>();
        pageMeta.put("page", page);
        pageMeta.put("pageSize", pageSize);
        pageMeta.put("total", total);
        result.put("page_meta", pageMeta);
        return result;
    }
}
