package com.boyouquan.service;

import com.boyouquan.model.BlogRequest;

public interface EmailService {

    void sendBlogRequestApprovedNotice(BlogRequest blogRequest);

    void sendBlogRequestRejectNotice(BlogRequest blogRequest);

    void send(String to, String subject, String content, boolean html);

}
