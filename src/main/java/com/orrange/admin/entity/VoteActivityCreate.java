package com.orrange.admin.entity;

import java.util.Date;

public class VoteActivityCreate {
    private Integer id;
    private String title;
    private String attachmentUrl;
    private Date startTime;
    private Date endTime;
    private Integer isOfficial;
    private String voteScope;
    private String communityName;

    // 构造函数
    public VoteActivityCreate() {}

    public VoteActivityCreate(String title, String attachmentUrl, Date startTime, Date endTime, 
                             Integer isOfficial, String voteScope, String communityName) {
        this.title = title;
        this.attachmentUrl = attachmentUrl;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isOfficial = isOfficial;
        this.voteScope = voteScope;
        this.communityName = communityName;
    }

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

    public Integer getIsOfficial() {
        return isOfficial;
    }

    public void setIsOfficial(Integer isOfficial) {
        this.isOfficial = isOfficial;
    }

    public String getVoteScope() {
        return voteScope;
    }

    public void setVoteScope(String voteScope) {
        this.voteScope = voteScope;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }
}
