package com.orrange.user.dto;

public class VoteHistoryQueryDTO {
    private Integer activityId;
    private Integer page = 1;
    private Integer pageSize = 10;

    public Integer getActivityId() { return activityId; }
    public void setActivityId(Integer activityId) { this.activityId = activityId; }
    public Integer getPage() { return page; }
    public void setPage(Integer page) { this.page = page; }
    public Integer getPageSize() { return pageSize; }
    public void setPageSize(Integer pageSize) { this.pageSize = pageSize; }
}
