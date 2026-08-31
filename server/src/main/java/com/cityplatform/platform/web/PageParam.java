package com.cityplatform.platform.web;

/**
 * 分页参数（平台 Web 基础能力）。
 */
public class PageParam {

    private int page = 1;
    private int size = 20;

    public PageParam() {
    }

    public PageParam(int page, int size) {
        this.page = page < 1 ? 1 : page;
        this.size = size < 1 ? 20 : Math.min(size, 100);
    }

    public int getPage() { return page; }
    public void setPage(int page) { this.page = page < 1 ? 1 : page; }
    public int getSize() { return size; }
    public void setSize(int size) { this.size = size < 1 ? 20 : Math.min(size, 100); }

    public int getOffset() {
        return (page - 1) * size;
    }
}
