package com.orrange.user.entity;

import java.util.Date;

public class VoteActivity {
    private Long id;
    private String activityName;
    private String topics1;
    private String topics2;
    private String topics3;
    private String beginTime;
    private String endTime;
    private boolean official;
    private String communityName;
    private int voteNum;
    private String voteScope;
    private Date createTime;
    private Date updateTime;
    
    // getter/setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getActivityName() { return activityName; }
    public void setActivityName(String activityName) { this.activityName = activityName; }
    public String getTopics1() { return topics1; }
    public void setTopics1(String topics1) { this.topics1 = topics1; }
    public String getTopics2() { return topics2; }
    public void setTopics2(String topics2) { this.topics2 = topics2; }
    public String getTopics3() { return topics3; }
    public void setTopics3(String topics3) { this.topics3 = topics3; }
    public String getBeginTime() { return beginTime; }
    public void setBeginTime(String beginTime) { this.beginTime = beginTime; }
    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
    public boolean isOfficial() { return official; }
    public void setOfficial(boolean official) { this.official = official; }
    public String getCommunityName() { return communityName; }
    public void setCommunityName(String communityName) { this.communityName = communityName; }
    public int getVoteNum() { return voteNum; }
    public void setVoteNum(int voteNum) { this.voteNum = voteNum; }
    public String getVoteScope() { return voteScope; }
    public void setVoteScope(String voteScope) { this.voteScope = voteScope; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
} 