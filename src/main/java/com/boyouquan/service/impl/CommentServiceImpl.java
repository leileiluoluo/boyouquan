package com.boyouquan.service.impl;

import com.boyouquan.dao.CommentDaoMapper;
import com.boyouquan.model.Comment;
import com.boyouquan.model.CommentList;
import com.boyouquan.service.CommentService;
import com.boyouquan.util.CommonUtils;
import com.boyouquan.util.StringIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentDaoMapper commentDaoMapper;

    @Override
    public String save(Comment comment) {
        String id = StringIDUtil.generateId();

        // save
        comment.setId(id);
        commentDaoMapper.save(comment);

        return id;
    }

    @Override
    public CommentList get(String url) {
        CommentList commentList = new CommentList();

        // data
        List<Comment> comments = commentDaoMapper.listByUrl(url);
        for (Comment comment : comments) {
            comment.setMailMd5(CommonUtils.md5(comment.getMail()));

            // replies
            List<Comment> replies = commentDaoMapper.listRepliesById(comment.getId());
            if (null != replies && !replies.isEmpty()) {
                comment.setReplies(replies);
            }
        }
        commentList.setData(comments);

        // count
        Long count = commentDaoMapper.countByUrl(url);
        commentList.setCount(count);
        commentList.setMore(false);

        return commentList;
    }

}
