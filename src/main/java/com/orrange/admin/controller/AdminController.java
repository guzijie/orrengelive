package com.orrange.admin.controller;

import com.orrange.admin.dto.AdminRegisterDTO;
import com.orrange.common.response.Result;
import com.orrange.admin.service.AdminService;
import com.orrange.admin.vo.AdminVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @PostMapping("/register")
    public Result<AdminVO> register(@RequestBody AdminRegisterDTO dto) {
        AdminVO vo = adminService.register(dto);
        return Result.success(vo);
    }
} 