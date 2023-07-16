package com.boyouquan.service;

import com.boyouquan.model.Blog;
import com.boyouquan.model.NewBlogInfo;
import com.boyouquan.util.NewPagination;

public interface BlogService {

    NewBlogInfo getBlogInfoByDomainName(String domainName);

    NewPagination<Blog> listWithKeyWord(String keyword, int page, int size);

    Blog getByDomainName(String domainName);

    void save(Blog blog);

    void update(Blog blog);

    void deleteByDomainName(String domainName);

}
