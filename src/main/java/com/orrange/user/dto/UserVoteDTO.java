package com.orrange.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 用户投票DTO
 */
public class UserVoteDTO {
    
    @NotNull(message = "活动ID不能为空")
    private Integer activityId;
    
    @NotNull(message = "议题ID不能为空")
    private Integer questionId;
    
    @NotNull(message = "选择的选项不能为空")
    private String selectedOption;
    
    @NotNull(message = "投票方式不能为空")
    private String voteMethod;
    
    @NotNull(message = "投票时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date voteTime;

    // Getters and Setters
    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public String getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(String selectedOption) {
        this.selectedOption = selectedOption;
    }

    public String getVoteMethod() {
        return voteMethod;
    }

    public void setVoteMethod(String voteMethod) {
        this.voteMethod = voteMethod;
    }

    public Date getVoteTime() {
        return voteTime;
    }

    public void setVoteTime(Date voteTime) {
        this.voteTime = voteTime;
    }
}
