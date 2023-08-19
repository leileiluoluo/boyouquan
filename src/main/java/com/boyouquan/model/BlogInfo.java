package com.boyouquan.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class BlogInfo extends Blog {

    private Long postCount;
    private Long accessCount;
    private Date latestPublishedAt;
    private List<Post> posts = Collections.emptyList();
    private String blogAdminSmallImageURL;
    private String blogAdminLargeImageURL;
    private String submittedInfo;
    private boolean statusOk;

}
