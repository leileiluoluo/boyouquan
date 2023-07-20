package com.boyouquan.dao;

import com.boyouquan.model.BlogDomainNamePublish;
import com.boyouquan.model.Post;

import java.util.Date;
import java.util.List;

public interface PostDaoMapper {

    BlogDomainNamePublish getMostPublishedInLatestOneMonth();

    Long countWithKeyWord(String keyword);

    List<Post> listWithKeyWord(String keyword, int offset, int rows);

    Long countAll();

    Date getLatestPublishedAtByBlogDomainName(String blogDomainName);

    Long countByBlogDomainName(String blogDomainName);

    List<Post> listByBlogDomainName(String blogDomainName, int limit);

    List<Post> listByDraftAndBlogDomainName(boolean draft, String blogDomainName, int limit);

    Post getByLink(String link);

    boolean existsByLink(String link);

    void batchSave(List<Post> posts);

    void batchUpdateDraftByBlogDomainName(String blogDomainName, boolean draft);

    void deleteByBlogDomainName(String blogDomainName);

}
