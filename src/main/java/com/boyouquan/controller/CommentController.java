package com.boyouquan.controller;

import com.boyouquan.model.Comment;
import com.boyouquan.model.CommentCreated;
import com.boyouquan.model.CommentList;
import com.boyouquan.model.CommentOperation;
import com.boyouquan.service.CommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    public ResponseEntity<?> operation(@RequestBody CommentOperation commentOperation) {
        if (CommentOperation.Event.COMMENT_SUBMIT.equals(commentOperation.getEvent())) {
            Comment comment = new Comment();
            BeanUtils.copyProperties(commentOperation, comment);
            String id = commentService.save(comment);
            return ResponseEntity.ok(new CommentCreated(id));
        } else if (CommentOperation.Event.COMMENT_GET.equals(commentOperation.getEvent())) {
            CommentList commentList = commentService.get(commentOperation.getUrl());
            return ResponseEntity.ok(commentList);
        }
        return null;
    }

}
