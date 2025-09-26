package com.orrange.user.entity;

import java.util.Date;

public class VoteQuestion {
    private Integer id;
    private Integer voteActivityId;
    private String title;
    private Integer sortOrder;
    private Integer optionSetId;
    private Date createdAt;

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVoteActivityId() {
        return voteActivityId;
    }

    public void setVoteActivityId(Integer voteActivityId) {
        this.voteActivityId = voteActivityId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Integer getOptionSetId() {
        return optionSetId;
    }

    public void setOptionSetId(Integer optionSetId) {
        this.optionSetId = optionSetId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
