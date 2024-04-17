package com.boyouquan.dao;

import com.boyouquan.model.BlogRequest;

import java.util.List;

public interface BlogRequestDaoMapper {

    List<BlogRequest> listBySelfSubmittedAndStatuses(String keyword, boolean selfSubmitted, List<BlogRequest.Status> statuses, int offset, int rows);

    Long countBySelfSubmittedAndStatuses(String keyword, boolean selfSubmitted, List<BlogRequest.Status> statuses);

    List<BlogRequest> listByStatuses(String keyword, List<BlogRequest.Status> statuses, int offset, int rows);

    Long countByStatuses(String keyword, List<BlogRequest.Status> statuses);

    List<BlogRequest> listByStatus(BlogRequest.Status status);

    BlogRequest getById(Long id);

    BlogRequest getByRssAddress(String rssAddress);

    void update(BlogRequest blogRequest);

    void submit(BlogRequest blogRequest);

    void deleteByRssAddress(String rssAddress);

}
