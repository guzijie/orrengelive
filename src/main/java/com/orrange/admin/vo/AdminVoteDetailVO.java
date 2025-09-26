package com.orrange.admin.vo;

import java.util.List;

public class AdminVoteDetailVO {
    private Integer id;
    private String title;
    private String attachmentUrl;
    private String startTime;
    private String endTime;
    private Integer isOfficial;
    private String voteScope;
    private String communityName;
    private String createdAt;
    private String status;
    private Integer questionCount;
    private Integer totalVotes;

    private AdminVoteSummaryVO summary; // optional
    private List<AdminVoteQuestionVO> questions; // optional

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
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
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getQuestionCount() { return questionCount; }
    public void setQuestionCount(Integer questionCount) { this.questionCount = questionCount; }
    public Integer getTotalVotes() { return totalVotes; }
    public void setTotalVotes(Integer totalVotes) { this.totalVotes = totalVotes; }
    public AdminVoteSummaryVO getSummary() { return summary; }
    public void setSummary(AdminVoteSummaryVO summary) { this.summary = summary; }
    public List<AdminVoteQuestionVO> getQuestions() { return questions; }
    public void setQuestions(List<AdminVoteQuestionVO> questions) { this.questions = questions; }
}
