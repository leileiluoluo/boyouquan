package com.boyouquan.util;

import java.util.ArrayList;
import java.util.List;

public class PaginationBuilder<T> {

    private Integer pageNo = 0;
    private Integer pageSize = 0;
    private Long total = 0L;
    private List<T> results = new ArrayList<>();

    public static <T> PaginationBuilder<T> newBuilder() {
        return new PaginationBuilder<>();
    }

    public static <T> Pagination<T> buildEmptyResults() {
        return new Pagination<>();
    }

    public PaginationBuilder<T> pageNo(int pageNo) {
        this.pageNo = pageNo;
        return this;
    }

    public PaginationBuilder<T> pageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public PaginationBuilder<T> total(Long total) {
        this.total = total;
        return this;
    }

    public PaginationBuilder<T> results(List<T> results) {
        this.results = results;
        return this;
    }

    public Pagination<T> build() {
        Pagination<T> pagination = new Pagination<>();
        pagination.setPageNo(this.pageNo);
        pagination.setPageSize(this.pageSize);
        pagination.setTotal(this.total);
        pagination.setResults(this.results);

        return pagination;
    }

}
