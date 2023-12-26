package com.boyouquan.service;

import com.boyouquan.model.Blog;
import com.boyouquan.model.BlogRequest;
import com.boyouquan.model.Post;

public interface EmailService {

    void sendBlogRequestSubmittedNotice(BlogRequest blogRequest);

    void sendBlogRequestApprovedNotice(BlogRequest blogRequest);

    void sendBlogSystemCollectedNotice(BlogRequest blogRequest);

    void sendBlogRequestRejectNotice(BlogRequest blogRequest);

    void sendPostRecommendedNotice(Blog blog, Post post);

    void sendPostPinnedNotice(Blog blog, Post post);

    void send(String to, String subject, String content, boolean html);

}
