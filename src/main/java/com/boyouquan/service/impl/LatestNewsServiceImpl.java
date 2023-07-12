package com.boyouquan.service.impl;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.enumeration.BlogEnums;
import com.boyouquan.model.BlogAccessSummary;
import com.boyouquan.model.BlogAggregate;
import com.boyouquan.model.BlogPost;
import com.boyouquan.model.LatestNews;
import com.boyouquan.service.BlogAccessService;
import com.boyouquan.service.BlogPostService;
import com.boyouquan.service.LatestNewsService;
import com.boyouquan.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class LatestNewsServiceImpl implements LatestNewsService {

    @Autowired
    private BlogPostService blogPostService;
    @Autowired
    private BlogAccessService blogAccessService;

    @Override
    public List<LatestNews> getLatestNews() {
        List<LatestNews> news = new ArrayList<>();

        // recent added blogs
        List<LatestNews> recentAddedBlogs = getRecentAddedBlogsNews();
        if (!recentAddedBlogs.isEmpty()) {
            news.addAll(recentAddedBlogs);
        }

        // most accessed blogs
        List<LatestNews> mostAccessedBlogs = getMostAccessedBlogsNews();
        if (!mostAccessedBlogs.isEmpty()) {
            news.addAll(mostAccessedBlogs);
        }

        // most updated blogs
        List<LatestNews> mostUpdatedBlogs = getMostUpdatedBlogsNews();
        if (!mostUpdatedBlogs.isEmpty()) {
            news.addAll(mostUpdatedBlogs);
        }

        return news;
    }

    private List<LatestNews> getRecentAddedBlogsNews() {
        List<BlogEnums> blogs = Arrays.stream(BlogEnums.values())
                .sorted((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()))
                .limit(10)
                .toList();

        final List<LatestNews> latestNews = new ArrayList<>();
        blogs.forEach(blogEnums -> {
            String feedAddress = blogEnums.getFeedAddress();
            String blogAddress = CommonUtils.trimFeedURLSuffix(feedAddress);
            BlogPost blogPost = blogPostService.getBlogByAddress(blogAddress);
            if (null != blogPost) {
                LatestNews news = new LatestNews();
                String title = String.format(CommonConstants.BLOG_ADDED_WELCOME_PATTERN, blogPost.getBlogName());
                news.setTitle(title);
                String domain = CommonUtils.getDomain(blogPost.getBlogAddress());
                String link = String.format(CommonConstants.BLOG_ITEM_ADDRESS_PATTERN, domain);
                news.setLink(link);

                latestNews.add(news);
            }
        });

        return latestNews.stream().limit(3).toList();
    }

    private List<LatestNews> getMostAccessedBlogsNews() {
        BlogAccessSummary blogAccessSummary = blogAccessService.getMostAccessedBlogByLatestMonth();
        if (null != blogAccessSummary) {
            String title = String.format(CommonConstants.MOST_ACCESSED_BLOG_ANNOUNCE_PATTERN, blogAccessSummary.getBlogName(), blogAccessSummary.getAccessCount());
            String domain = CommonUtils.getDomain(blogAccessSummary.getBlogAddress());
            String link = String.format(CommonConstants.BLOG_ITEM_ADDRESS_PATTERN, domain);

            List<LatestNews> mostAccessedBlogs = new ArrayList<>();
            LatestNews news = new LatestNews();
            news.setTitle(title);
            news.setLink(link);
            mostAccessedBlogs.add(news);
            return mostAccessedBlogs;
        }
        return Collections.emptyList();
    }

    private List<LatestNews> getMostUpdatedBlogsNews() {
        BlogAggregate blogAggregate = blogPostService.getMostUpdatedBlogByLatestMonth();
        if (null != blogAggregate) {
            String title = String.format(CommonConstants.MOST_UPDATED_BLOG_ANNOUNCE_PATTERN, blogAggregate.getName(), blogAggregate.getPostCount());
            String domain = CommonUtils.getDomain(blogAggregate.getAddress());
            String link = String.format(CommonConstants.BLOG_ITEM_ADDRESS_PATTERN, domain);

            List<LatestNews> mostUpdatedBlogs = new ArrayList<>();
            LatestNews news = new LatestNews();
            news.setTitle(title);
            news.setLink(link);
            mostUpdatedBlogs.add(news);
            return mostUpdatedBlogs;
        }
        return Collections.emptyList();
    }

}
