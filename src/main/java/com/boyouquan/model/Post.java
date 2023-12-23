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
public class Post {

    private String link;
    private String blogDomainName;
    private String title;
    private String description;
    private Date publishedAt;
    private Boolean draft = false;
    private Boolean pinned = false;

}
