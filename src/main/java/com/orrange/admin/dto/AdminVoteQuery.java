package com.orrange.admin.dto;

public class AdminVoteQuery {
    private String keyword;
    private String status; // not_started | ongoing | finished
    private String communityName;
    private String startFrom; // YYYY-MM-DD HH:mm:ss
    private String endTo;     // YYYY-MM-DD HH:mm:ss
    private Integer page;     // default 1
    private Integer pageSize; // default 10, max 100

    public String getKeyword() { return keyword; }
    public void setKeyword(String keyword) { this.keyword = keyword; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getCommunityName() { return communityName; }
    public void setCommunityName(String communityName) { this.communityName = communityName; }
    public String getStartFrom() { return startFrom; }
    public void setStartFrom(String startFrom) { this.startFrom = startFrom; }
    public String getEndTo() { return endTo; }
    public void setEndTo(String endTo) { this.endTo = endTo; }
    public Integer getPage() { return page; }
    public void setPage(Integer page) { this.page = page; }
    public Integer getPageSize() { return pageSize; }
    public void setPageSize(Integer pageSize) { this.pageSize = pageSize; }
}



