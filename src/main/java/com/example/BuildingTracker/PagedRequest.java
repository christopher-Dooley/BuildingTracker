package com.example.BuildingTracker;

public record PagedRequest(int page, int pageSize, boolean sort, String sortBy, boolean sortAscending) {

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
