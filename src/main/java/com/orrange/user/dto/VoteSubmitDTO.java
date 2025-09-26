package com.orrange.user.dto;

import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class VoteSubmitDTO {
    private Integer activityId;
    private Integer questionId;
    private String selectedOption; // 文本或代号，如 赞同 / A / 赞同A
    private String voteMethod;     // online/offline/sms 或 线上/短信/线下
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date voteTime;

    public Integer getActivityId() { return activityId; }
    public void setActivityId(Integer activityId) { this.activityId = activityId; }
    public Integer getQuestionId() { return questionId; }
    public void setQuestionId(Integer questionId) { this.questionId = questionId; }
    public String getSelectedOption() { return selectedOption; }
    public void setSelectedOption(String selectedOption) { this.selectedOption = selectedOption; }
    public String getVoteMethod() { return voteMethod; }
    public void setVoteMethod(String voteMethod) { this.voteMethod = voteMethod; }
    public Date getVoteTime() { return voteTime; }
    public void setVoteTime(Date voteTime) { this.voteTime = voteTime; }
}
