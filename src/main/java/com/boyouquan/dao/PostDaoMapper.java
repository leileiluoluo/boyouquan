package com.boyouquan.dao;

import com.boyouquan.model.BlogDomainNamePublish;
import com.boyouquan.model.MonthPublish;
import com.boyouquan.model.Post;

import java.util.Date;
import java.util.List;

public interface PostDaoMapper {

    BlogDomainNamePublish getMostPublishedInLatestOneMonth();

    List<MonthPublish> getBlogPostPublishSeriesInLatestOneYear(String blogDomainName);

    Long countWithKeyWord(String keyword);

    List<Post> listWithKeyWord(String keyword, int offset, int rows);

    Long countAll();

    Date getLatestPublishedAtByBlogDomainName(String blogDomainName);

    Long countByBlogDomainName(String blogDomainName);

    Long countByDraftAndBlogDomainName(boolean draft, String blogDomainName);

    List<Post> listByDraftAndBlogDomainName(boolean draft, String blogDomainName, int offset, int rows);

    Post getByLink(String link);

    boolean existsByLink(String link);

    boolean existsByTitle(String title);

    void batchSave(List<Post> posts);

    void batchUpdateDraftByBlogDomainName(String blogDomainName, boolean draft);

    void deleteByBlogDomainName(String blogDomainName);

    void deleteByLink(String link);

}
