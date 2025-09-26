package com.orrange.admin.vo;

import lombok.Data;

@Data
public class CreateVoteActivityResultVO {
    private Integer id;
    
    public CreateVoteActivityResultVO(Integer id) {
        this.id = id;
    }
}
