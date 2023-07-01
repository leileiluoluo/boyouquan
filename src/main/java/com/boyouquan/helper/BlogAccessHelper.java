package com.boyouquan.helper;

import com.boyouquan.service.BlogAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BlogAccessHelper {

    @Autowired
    private BlogAccessService blogAccessService;

    public Long countBlogAccessByLink(String link) {
        return blogAccessService.countBlogAccessByLink(link);
    }

}
