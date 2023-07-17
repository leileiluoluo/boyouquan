package com.boyouquan.dao;

import com.boyouquan.model.Post;

import java.util.Date;
import java.util.List;

public interface PostDaoMapper {

    Date getLatestPublishedAtByBlogDomainName(String blogDomainName);

    Long countByBlogDomainName(String blogDomainName);

    List<Post> listByBlogDomainName(String blogDomainName, int limit);

    boolean existsByLink(String link);

    void batchSave(List<Post> posts);

}
