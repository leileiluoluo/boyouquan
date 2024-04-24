package com.boyouquan.model;

import lombok.Data;

@Data
public class CommentOperation {

    private Event event;
    private String url;
    private String nick;
    private String mail;
    private String link;
    private String href;
    private String comment;
    private String pid;
    private String rid;
    private String ua;

    public enum Event {
        COMMENT_SUBMIT,
        GET_CONFIG,
        COMMENT_GET
    }

}
