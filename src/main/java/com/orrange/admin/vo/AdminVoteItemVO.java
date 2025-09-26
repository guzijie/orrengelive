package com.orrange.admin.vo;

public class AdminVoteItemVO {
    private Integer activityId;
    private String title;
    private String attachmentUrl;
    private String startTime;
    private String endTime;
    private Integer isOfficial; // 0/1
    private String voteScope;
    private String communityName;
    private String createdAt;
    private Integer questionCount;
    private Integer totalVotes;
    private String status; // not_started | ongoing | finished

    public Integer getActivityId() { return activityId; }
    public void setActivityId(Integer activityId) { this.activityId = activityId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAttachmentUrl() { return attachmentUrl; }
    public void setAttachmentUrl(String attachmentUrl) { this.attachmentUrl = attachmentUrl; }
    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
    public Integer getIsOfficial() { return isOfficial; }
    public void setIsOfficial(Integer isOfficial) { this.isOfficial = isOfficial; }
    public String getVoteScope() { return voteScope; }
    public void setVoteScope(String voteScope) { this.voteScope = voteScope; }
    public String getCommunityName() { return communityName; }
    public void setCommunityName(String communityName) { this.communityName = communityName; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public Integer getQuestionCount() { return questionCount; }
    public void setQuestionCount(Integer questionCount) { this.questionCount = questionCount; }
    public Integer getTotalVotes() { return totalVotes; }
    public void setTotalVotes(Integer totalVotes) { this.totalVotes = totalVotes; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}



