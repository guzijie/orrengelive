package com.orrange.admin.dto;

import java.util.Date;

/**
 * 议题插入DTO
 */
public class VoteQuestionInsertDTO {
    private Integer activityId;
    private String questionText;
    private Integer sortOrder;
    private Integer optionSetId;
    private Date startTime;
    private Date endTime;
    private String attachmentUrl;
    private Integer questionId; // 用于接收生成的主键

    // 构造函数
    public VoteQuestionInsertDTO() {}

    public VoteQuestionInsertDTO(Integer activityId, String questionText, Integer sortOrder, 
                                Integer optionSetId, Date startTime, Date endTime, String attachmentUrl) {
        this.activityId = activityId;
        this.questionText = questionText;
        this.sortOrder = sortOrder;
        this.optionSetId = optionSetId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.attachmentUrl = attachmentUrl;
    }

    // Getters and Setters
    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
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

    public String getAttachmentUrl() {
        return attachmentUrl;
    }

    public void setAttachmentUrl(String attachmentUrl) {
        this.attachmentUrl = attachmentUrl;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }
}
