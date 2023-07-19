package com.boyouquan.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class BlogRequestInfo extends BlogRequest {

    private String statusInfo;
    private boolean approved;
    private boolean failed;
    private String domainName;

}
