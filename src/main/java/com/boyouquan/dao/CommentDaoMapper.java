package com.boyouquan.dao;

import com.boyouquan.model.Comment;

import java.util.List;

public interface CommentDaoMapper {

    Long countByUrl(String url);

    List<Comment> listByUrl(String url);

    List<Comment> listRepliesById(String id);

    void save(Comment comment);

}
