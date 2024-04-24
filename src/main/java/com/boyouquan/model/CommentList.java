package com.boyouquan.model;

import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class CommentList {

    private List<Comment> data = Collections.emptyList();
    private boolean more;
    private Long count;

}
