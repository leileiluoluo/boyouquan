package com.boyouquan.model;

import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class Comment {

    private String id;
    private String url;
    private String nick;
    private String mail;
    private String mailMd5;
    private String link;
    private String href;
    private String comment;
    private String ua;
    private String browser;
    private String opRegion;
    private boolean master;
    private int like;
    private boolean liked;
    private List<Comment> replies = Collections.emptyList();
    private String rid;
    private String pid;
    private String ruser;
    private Long created;
    private Long updated;

}
