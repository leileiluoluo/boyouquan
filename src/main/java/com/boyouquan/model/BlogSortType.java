package com.boyouquan.model;

public enum BlogSortType {
    collect_time,
    access_count;

    public static BlogSortType of(String sort) {
        for (BlogSortType sortType : BlogSortType.values()) {
            if (sortType.name().equals(sort)) {
                return sortType;
            }
        }
        return access_count;
    }
}
