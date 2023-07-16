package com.boyouquan.util;

import java.util.ArrayList;
import java.util.List;

public class NewPaginationBuilder<T> {

    private Integer pageNo = 0;
    private Integer pageSize = 0;
    private Long total = 0L;
    private List<T> results = new ArrayList<>();

    public static <T> NewPaginationBuilder<T> newBuilder() {
        return new NewPaginationBuilder<>();
    }

    public static <T> NewPagination<T> buildEmptyResults() {
        return new NewPagination<>();
    }

    public NewPaginationBuilder<T> pageNo(int pageNo) {
        this.pageNo = pageNo;
        return this;
    }

    public NewPaginationBuilder<T> pageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public NewPaginationBuilder<T> total(Long total) {
        this.total = total;
        return this;
    }

    public NewPaginationBuilder<T> results(List<T> results) {
        this.results = results;
        return this;
    }

    public NewPagination<T> build() {
        NewPagination<T> newPagination = new NewPagination<>();
        newPagination.setPageNo(this.pageNo);
        newPagination.setPageSize(this.pageSize);
        newPagination.setTotal(this.total);
        newPagination.setResults(this.results);

        return newPagination;
    }

}
