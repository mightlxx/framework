package com.mightlin.common.model;

import lombok.Data;

@Data
public class PageInfo {
    /**
     * 当前页
     */
    private long current;
    /**
     * 每页结果数
     */
    private long size;
    /**
     * 总页数
     */
    private long pages;
    /**
     * 总数
     */
    private long total;

    public PageInfo(long current, long size, long total, long pages) {
        this.current = current;
        this.size = size;
        this.total = total;
        this.pages = pages;
    }
}
