package com.boyouquan.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class BlogRequestInfo extends BlogRequest {

    private String statusInfo;
    private boolean approved;
    private boolean failed;
    private String domainName;
    private List<Post> posts = Collections.emptyList();

}
