package com.boyouquan.model;

import com.boyouquan.util.CommonUtils;
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
        return CommonUtils.getDomain(address);
    }

}
