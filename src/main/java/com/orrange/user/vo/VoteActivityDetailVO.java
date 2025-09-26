package com.orrange.user.vo;

import java.util.Date;
import java.util.List;

public class VoteActivityDetailVO {
    private Integer id;
    private String title;
    private String attachmentUrl;
    private Date startTime;
    private Date endTime;
    private Boolean isOfficial;
    private String communityName;
    private String buildingNumber;
    private String unitNumber;
    private String createdAt;
    private String voteScope;
    private List<QuestionDetailVO> questions;

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAttachmentUrl() {
        return attachmentUrl;
    }

    public void setAttachmentUrl(String attachmentUrl) {
        this.attachmentUrl = attachmentUrl;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Boolean getIsOfficial() {
        return isOfficial;
    }

    public void setIsOfficial(Boolean isOfficial) {
        this.isOfficial = isOfficial;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getBuildingNumber() {
        return buildingNumber;
    }

    public void setBuildingNumber(String buildingNumber) {
        this.buildingNumber = buildingNumber;
    }

    public String getUnitNumber() {
        return unitNumber;
    }

    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getVoteScope() {
        return voteScope;
    }

    public void setVoteScope(String voteScope) {
        this.voteScope = voteScope;
    }

    public List<QuestionDetailVO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionDetailVO> questions) {
        this.questions = questions;
    }
}
