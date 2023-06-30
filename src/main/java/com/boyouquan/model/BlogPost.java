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
public class BlogPost {

    private String blogName;
    private String blogAddress;
    private String title;
    private String description;
    private String link;
    private Date createdAt;

}
