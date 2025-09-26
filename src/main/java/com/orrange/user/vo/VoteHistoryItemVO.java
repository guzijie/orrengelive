package com.orrange.user.vo;

public class VoteHistoryItemVO {
    private Integer activityId;
    private String activityTitle;
    private String communityName;
    private Integer questionId;
    private String questionText;
    private String selectedOption;
    private String voteMethod;
    private String voteTime;

    public Integer getActivityId() { return activityId; }
    public void setActivityId(Integer activityId) { this.activityId = activityId; }
    public String getActivityTitle() { return activityTitle; }
    public void setActivityTitle(String activityTitle) { this.activityTitle = activityTitle; }
    public String getCommunityName() { return communityName; }
    public void setCommunityName(String communityName) { this.communityName = communityName; }
    public Integer getQuestionId() { return questionId; }
    public void setQuestionId(Integer questionId) { this.questionId = questionId; }
    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }
    public String getSelectedOption() { return selectedOption; }
    public void setSelectedOption(String selectedOption) { this.selectedOption = selectedOption; }
    public String getVoteMethod() { return voteMethod; }
    public void setVoteMethod(String voteMethod) { this.voteMethod = voteMethod; }
    public String getVoteTime() { return voteTime; }
    public void setVoteTime(String voteTime) { this.voteTime = voteTime; }
}
