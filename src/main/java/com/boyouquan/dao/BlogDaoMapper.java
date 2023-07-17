package com.boyouquan.dao;

import com.boyouquan.model.Blog;

import java.util.List;

public interface BlogDaoMapper {

    List<Blog> listAll();

    Long countWithKeyword(String keyword);

    List<Blog> listWithKeyWord(String keyword, int offset, int rows);

    boolean existsByDomainName(String domainName);

    Blog getByDomainName(String domainName);

    void save(Blog blog);

    void update(Blog blog);

    void deleteByDomainName(String domainName);

}
