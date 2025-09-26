package com.orrange.admin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
public class CreateVoteActivityDTO {
    @NotBlank(message = "活动标题不能为空")
    private String title;
    
    private String attachmentUrl;
    
    @NotNull(message = "开始时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;
    
    @NotNull(message = "结束时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;
    
    private Integer isOfficial; // 0 or 1
    
    @NotBlank(message = "投票范围不能为空")
    private String voteScope; // "ALL" or "PARTIAL"
    
    private String communityName;
    
    private List<ScopeItemDTO> scopeItems;
    
    @Data
    public static class ScopeItemDTO {
        private String buildingNumber;
        private List<String> unitNumbers;
    }
}
