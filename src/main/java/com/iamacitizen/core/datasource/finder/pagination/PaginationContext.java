package com.iamacitizen.core.datasource.finder.pagination;

public class PaginationContext {

    private int pageSize;
    private int pageIndex;
    private int recordsNumber;

    public PaginationContext(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int currentPage) {
        this.pageIndex = currentPage;
    }

    public int getRecordsNumber() {
        return recordsNumber;
    }

    public void setRecordsNumber(int recordsNumber) {
        this.recordsNumber = recordsNumber;
    }

    public int getTotalPages() {
        return recordsNumber / pageSize;
    }
}
