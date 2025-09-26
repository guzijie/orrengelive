package com.orrange.user.entity;

import java.math.BigDecimal;
import java.util.Date;

public class UserVote {
    private Integer id;
    private Integer userId;
    private Integer voteActivityId;
    private Integer questionId;
    private Integer optionId;
    private String voteMethod;
    private Date voteTime;
    private BigDecimal areaSize;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public Integer getVoteActivityId() { return voteActivityId; }
    public void setVoteActivityId(Integer voteActivityId) { this.voteActivityId = voteActivityId; }
    public Integer getQuestionId() { return questionId; }
    public void setQuestionId(Integer questionId) { this.questionId = questionId; }
    public Integer getOptionId() { return optionId; }
    public void setOptionId(Integer optionId) { this.optionId = optionId; }
    public String getVoteMethod() { return voteMethod; }
    public void setVoteMethod(String voteMethod) { this.voteMethod = voteMethod; }
    public Date getVoteTime() { return voteTime; }
    public void setVoteTime(Date voteTime) { this.voteTime = voteTime; }
    public BigDecimal getAreaSize() { return areaSize; }
    public void setAreaSize(BigDecimal areaSize) { this.areaSize = areaSize; }
}
