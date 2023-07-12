package com.boyouquan.service.impl;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.enumeration.BlogEnums;
import com.boyouquan.model.BlogPost;
import com.boyouquan.model.LatestNews;
import com.boyouquan.service.BlogPostService;
import com.boyouquan.service.LatestNewsService;
import com.boyouquan.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class LatestNewsServiceImpl implements LatestNewsService {

    @Autowired
    private BlogPostService blogPostService;

    @Override
    public List<LatestNews> getLatestNews() {
        List<BlogEnums> blogs = Arrays.stream(BlogEnums.values())
                .sorted((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()))
                .limit(10)
                .toList();

        final List<LatestNews> latestNews = new ArrayList<>();
        blogs.forEach(blogEnums -> {
            LatestNews news = new LatestNews();

            String feedAddress = blogEnums.getFeedAddress();
            String blogAddress = CommonUtils.trimFeedURLSuffix(feedAddress);
            BlogPost blogPost = blogPostService.getBlogByAddress(blogAddress);
            if (null != blogPost) {
                String title = String.format(CommonConstants.BLOG_ADDED_WELCOME_PATTERN, blogPost.getBlogName());
                news.setTitle(title);
                String domain = CommonUtils.getDomain(blogPost.getBlogAddress());
                String link = String.format(CommonConstants.BLOG_ITEM_ADDRESS_PATTERN, domain);
                news.setLink(link);
                latestNews.add(news);
            }
        });

        return latestNews.stream().limit(5).toList();
    }

}
