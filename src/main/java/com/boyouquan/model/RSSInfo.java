package com.boyouquan.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class RSSInfo {

    private String blogName;
    private String blogAddress;
    private List<Post> blogPosts;

    @NoArgsConstructor
    @Getter
    @Setter
    public static class Post {
        private String title;
        private String description;
        private Date publishedAt;
    }

}
