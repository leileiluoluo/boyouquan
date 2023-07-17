package com.boyouquan.service;

import com.boyouquan.model.BlogPost;
import com.boyouquan.model.Post;
import com.boyouquan.util.Pagination;

import java.util.Date;
import java.util.List;

public interface PostService {

    Date getLatestPublishedAtByBlogDomainName(String blogDomainName);

    Long countByBlogDomainName(String blogDomainName);

    List<Post> listByBlogDomainName(String blogDomainName, int limit);

    boolean existsByLink(String link);

    void batchSave(List<Post> posts);

}
