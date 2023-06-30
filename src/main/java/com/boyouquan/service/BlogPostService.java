package com.boyouquan.service;

import com.boyouquan.model.BlogPost;
import com.boyouquan.util.Pagination;

public interface BlogPostService {

    Pagination<BlogPost> listAllPosts();

}
