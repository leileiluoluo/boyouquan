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
public class BlogInfo {

    private String name;
    private String address;
    private String description;
    private String createdAt;
    private Long postsCount;
    private Long accessCount;
    private Date latestUpdatedAt;
    private List<BlogPost> latestPosts;

    public String getDomain() {
        // scheme
        if (address.startsWith("https://")) {
            address = address.substring("https://".length());
        } else if (address.startsWith("http://")) {
            address = address.substring("http://".length());
        }

        // tail
        if (address.endsWith("/")) {
            address = address.substring(0, address.length() - 1);
        }
        return address;
    }

}
