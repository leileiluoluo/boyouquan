package com.boyouquan.util;

import java.util.ArrayList;
import java.util.List;

public class Pagination<T> {

    private int pageNo;
    private int pageSize;

    private List<T> results = new ArrayList<>();

    private static class PaginationBuilder<T> {
        private int pageNo;
        private int pageSize;

        private List<T> results = new ArrayList<>();
    }

    public static <T> Pagination<T> newBuilder() {
        return new Pagination<>();
    }

    public Pagination<T> pageNo(int pageNo) {
        this.pageNo = pageNo;
        return this;
    }

    public Pagination<T> pageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public Pagination<T> results(List<T> results) {
        this.results = results;
        return this;
    }

}
