package com.orrange.admin.service;

import com.orrange.admin.dto.VerificationDTO;
import com.orrange.admin.dto.AdminRegisterDTO;
import com.orrange.admin.vo.AdminVO;

public interface AdminService {
    void getVerificationCode(VerificationDTO dto);
    AdminVO register(AdminRegisterDTO dto);
} 