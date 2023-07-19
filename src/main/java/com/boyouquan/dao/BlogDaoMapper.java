package com.boyouquan.dao;

import com.boyouquan.model.Blog;

import java.util.List;

public interface BlogDaoMapper {

    Blog getByRandom();

    Long countAll();

    List<Blog> listAll();

    List<Blog> listRecentCollected(int limit);

    Long countWithKeyword(String keyword);

    List<Blog> listWithKeyWord(String keyword, int offset, int rows);

    boolean existsByDomainName(String domainName);

    boolean existsByRssAddress(String rssAddress);

    Blog getByDomainName(String domainName);

    Blog getByAddress(String address);

    Blog getByRSSAddress(String rssAddress);

    void save(Blog blog);

    void update(Blog blog);

    void deleteByDomainName(String domainName);

}
