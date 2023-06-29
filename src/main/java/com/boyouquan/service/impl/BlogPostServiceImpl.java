package com.boyouquan.service.impl;

import com.boyouquan.model.BlogPost;
import com.boyouquan.service.BlogPostService;
import com.boyouquan.util.Pagination;
import org.springframework.stereotype.Service;

@Service
public class BlogPostServiceImpl implements BlogPostService {

    @Override
    public Pagination<BlogPost> listAllPosts() {
        return null;
    }

}
