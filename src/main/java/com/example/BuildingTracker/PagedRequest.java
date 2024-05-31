package com.example.BuildingTracker;

import java.util.Objects;

public class PagedRequest {

    private int page;
    private int pageSize;
    private boolean sort;
    private String sortBy;
    private boolean sortAscending;

    public PagedRequest(int page, int pageSize, boolean sort, String sortBy, boolean sortAscending) {
        this.page = page;
        this.pageSize = pageSize;
        this.sort = sort;
        this.sortBy = sortBy;
        this.sortAscending = sortAscending;
    }

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public boolean isSort() {
        return sort;
    }

    public String getSortBy() {
        return sortBy;
    }

    public boolean isSortAscending() {
        return sortAscending;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PagedRequest that = (PagedRequest) o;
        return page == that.page && pageSize == that.pageSize && sort == that.sort && sortAscending == that.sortAscending && Objects.equals(sortBy, that.sortBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(page, pageSize, sort, sortBy, sortAscending);
    }

    @Override
    public String toString() {
        return "PagedRequest{" +
                "page=" + page +
                ", pageSize=" + pageSize +
                ", sort=" + sort +
                ", sortBy='" + sortBy + '\'' +
                ", sortAscending=" + sortAscending +
                '}';
    }
}
