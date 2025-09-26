package com.orrange.admin.vo;

import java.util.List;

public class AdminVoteQuestionVO {
    private Integer questionId;
    private String questionText;
    private Integer templateId;
    private String createdAt;
    private List<AdminVoteQuestionOptionVO> options;
    private AdminVoteQuestionSeriesVO series;

    public Integer getQuestionId() { return questionId; }
    public void setQuestionId(Integer questionId) { this.questionId = questionId; }
    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }
    public Integer getTemplateId() { return templateId; }
    public void setTemplateId(Integer templateId) { this.templateId = templateId; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public List<AdminVoteQuestionOptionVO> getOptions() { return options; }
    public void setOptions(List<AdminVoteQuestionOptionVO> options) { this.options = options; }
    public AdminVoteQuestionSeriesVO getSeries() { return series; }
    public void setSeries(AdminVoteQuestionSeriesVO series) { this.series = series; }
}
