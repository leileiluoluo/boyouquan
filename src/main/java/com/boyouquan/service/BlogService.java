package com.boyouquan.service;

import com.boyouquan.model.Blog;
import com.boyouquan.model.NewBlogInfo;
import com.boyouquan.util.NewPagination;

import java.util.List;

public interface BlogService {

    List<Blog> listAll();

    NewBlogInfo getBlogInfoByDomainName(String domainName);

    NewPagination<NewBlogInfo> listBlogInfosWithKeyWord(String keyword, int page, int size);

    NewPagination<Blog> listWithKeyWord(String keyword, int page, int size);

    boolean existsByDomainName(String domainName);

    Blog getByDomainName(String domainName);

    void save(Blog blog);

    void update(Blog blog);

    void deleteByDomainName(String domainName);

}
