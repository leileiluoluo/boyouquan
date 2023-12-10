package com.boyouquan.model;

public enum BlogSortType {
    access_count,
    collect_time;

    public static BlogSortType of(String sort) {
        for (BlogSortType sortType : BlogSortType.values()) {
            if (sortType.name().equals(sort)) {
                return sortType;
            }
        }
        return access_count;
    }
}
