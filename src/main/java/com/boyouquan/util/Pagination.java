package com.boyouquan.util;

import java.util.ArrayList;
import java.util.List;

public class Pagination<T> {

    private int pageNo = 0;
    private int pageSize = 0;
    private int total = 0;

    private List<T> results = new ArrayList<>();

    public static <T> Pagination<T> builder() {
        return new Pagination<>();
    }

    public static <T> Pagination<T> buildEmptyResults() {
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

    public Pagination<T> total(int total) {
        this.total = total;
        return this;
    }

    public Pagination<T> results(List<T> results) {
        this.results = results;
        return this;
    }

    public int getPageNo() {
        return this.pageNo;
    }

    public boolean hasNextPage() {
        return total > (pageNo * pageSize);
    }

    public List<T> getResults() {
        return this.results;
    }

}
