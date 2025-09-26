package com.orrange.admin.vo;

/**
 * 新增投票议题结果VO
 */
public class CreateVoteQuestionResultVO {
    private Integer activityId;
    private Integer questionId;

    public CreateVoteQuestionResultVO() {}

    public CreateVoteQuestionResultVO(Integer activityId, Integer questionId) {
        this.activityId = activityId;
        this.questionId = questionId;
    }

    // Getters and Setters
    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }
}
