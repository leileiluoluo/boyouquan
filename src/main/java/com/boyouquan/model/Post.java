package com.boyouquan.model;

import com.boyouquan.util.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date publishedAt;
    private Boolean draft = false;
    private Boolean recommended = false;
    private Boolean pinned = false;

}
