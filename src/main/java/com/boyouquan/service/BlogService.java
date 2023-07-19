package com.boyouquan.service;

import com.boyouquan.model.Blog;
import com.boyouquan.model.BlogInfo;
import com.boyouquan.util.Pagination;

import java.util.List;

public interface BlogService {

    String getBlogAdminSmallImageURLByDomainName(String blogDomainName);

    String getBlogAdminLargeImageURLByDomainName(String blogDomainName);

    Blog getByRandom();

    Long countAll();

    List<Blog> listAll();

    List<Blog> listRecentCollected(int limit);

    BlogInfo getBlogInfoByDomainName(String domainName);

    Pagination<BlogInfo> listBlogInfosWithKeyWord(String keyword, int page, int size);

    Pagination<Blog> listWithKeyWord(String keyword, int page, int size);

    boolean existsByRssAddress(String rssAddress);

    boolean existsByDomainName(String domainName);

    Blog getByDomainName(String domainName);

    Blog getByAddress(String address);

    Blog getByRSSAddress(String rssAddress);

    void save(Blog blog);

    void update(Blog blog);

    void deleteByDomainName(String domainName);

}
