package com.boyouquan.service;

import com.boyouquan.model.Comment;
import com.boyouquan.model.CommentList;

public interface CommentService {

    String save(Comment comment);

    CommentList get(String url);

}
