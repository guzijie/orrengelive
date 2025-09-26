package com.orrange.user.vo;

import java.util.List;

public class PageResultVO<T> {
    private List<T> data;
    private PageMetaVO pageMeta;

    public PageResultVO() {}

    public PageResultVO(List<T> data, PageMetaVO pageMeta) {
        this.data = data;
        this.pageMeta = pageMeta;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public PageMetaVO getPageMeta() {
        return pageMeta;
    }

    public void setPageMeta(PageMetaVO pageMeta) {
        this.pageMeta = pageMeta;
    }
}
