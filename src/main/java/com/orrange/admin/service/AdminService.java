package com.orrange.admin.service;

import com.orrange.admin.dto.AdminRegisterDTO;
import com.orrange.admin.vo.AdminVO;

public interface AdminService {
    AdminVO register(AdminRegisterDTO dto);
} 