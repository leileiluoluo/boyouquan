package com.boyouquan.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class BlogLatestPublishedAt {

    private String blogDetailPageUrl;
    private String latestPublishedAt;

}
