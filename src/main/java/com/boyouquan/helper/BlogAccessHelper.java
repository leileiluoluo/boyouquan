package com.boyouquan.helper;

import com.boyouquan.enumeration.BlogEnums;
import com.boyouquan.service.BlogAccessService;
import com.boyouquan.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.boyouquan.util.CommonUtils.md5;

@Component
public class BlogAccessHelper {

    @Autowired
    private BlogAccessService blogAccessService;

    public Long countBlogAccessByLink(String link) {
        return blogAccessService.countBlogAccessByLink(link);
    }

    public String getBlogAdminImageURLByAddress(String blogAddress) {
        String email = BlogEnums.getEmailByBlogAddress(blogAddress);
        md5(email);
        return "https://seccdn.libravatar.org/gravatarproxy/" + md5(email) + "?s=20";
    }

    public String dateFriendlyDisplay(Date date) {
        return CommonUtils.dateFriendlyDisplay(date);
    }

}
