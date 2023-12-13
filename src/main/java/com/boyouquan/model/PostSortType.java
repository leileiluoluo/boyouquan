package com.boyouquan.model;

public enum PostSortType {
    recommended,
    latest;

    public static PostSortType of(String sort) {
        for (PostSortType sortType : PostSortType.values()) {
            if (sortType.name().equals(sort)) {
                return sortType;
            }
        }
        return recommended;
    }
}
