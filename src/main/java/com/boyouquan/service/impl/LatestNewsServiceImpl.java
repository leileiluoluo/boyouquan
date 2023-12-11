package com.boyouquan.service.impl;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.model.Blog;
import com.boyouquan.model.BlogDomainNameAccess;
import com.boyouquan.model.BlogDomainNamePublish;
import com.boyouquan.model.LatestNews;
import com.boyouquan.service.AccessService;
import com.boyouquan.service.BlogService;
import com.boyouquan.service.LatestNewsService;
import com.boyouquan.service.PostService;
import com.boyouquan.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class LatestNewsServiceImpl implements LatestNewsService {

    @Autowired
    private BlogService blogService;
    @Autowired
    private PostService postService;
    @Autowired
    private AccessService accessService;

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
        final List<LatestNews> latestNews = new ArrayList<>();

        List<Blog> blogs = blogService.listRecentCollected(3);
        for (Blog blog : blogs) {
            LatestNews news = new LatestNews();
            String title = String.format(CommonConstants.BLOG_ADDED_WELCOME_PATTERN, blog.getName());
            news.setTitle(title);
            news.setLink(CommonConstants.BLOG_LIST_ADDRESS_SORT_BY_COLLECT_TIME);
            latestNews.add(news);
        }

        return latestNews;
    }

    private List<LatestNews> getMostAccessedBlogsNews() {
        BlogDomainNameAccess blogDomainNameAccess = accessService.getMostAccessedBlogDomainNameInLastMonth();
        if (null == blogDomainNameAccess) {
            return Collections.emptyList();
        }

        Blog blog = blogService.getByDomainName(blogDomainNameAccess.getBlogDomainName());
        if (null == blog) {
            return Collections.emptyList();
        }

        String title = String.format(CommonConstants.MOST_ACCESSED_BLOG_ANNOUNCE_PATTERN, blog.getName(), blogDomainNameAccess.getAccessCount());
        String domain = CommonUtils.getDomain(blog.getAddress());
        String link = String.format(CommonConstants.BLOG_ITEM_ADDRESS_PATTERN, domain);

        LatestNews news = new LatestNews();
        news.setTitle(title);
        news.setLink(link);

        return List.of(news);
    }

    private List<LatestNews> getMostUpdatedBlogsNews() {
        BlogDomainNamePublish blogDomainNamePublish = postService.getMostPublishedInLatestOneMonth();
        if (null == blogDomainNamePublish) {
            return Collections.emptyList();
        }
        Blog blog = blogService.getByDomainName(blogDomainNamePublish.getBlogDomainName());

        String title = String.format(CommonConstants.MOST_UPDATED_BLOG_ANNOUNCE_PATTERN, blog.getName(), blogDomainNamePublish.getPostCount());
        String domain = CommonUtils.getDomain(blog.getAddress());
        String link = String.format(CommonConstants.BLOG_ITEM_ADDRESS_PATTERN, domain);

        LatestNews news = new LatestNews();
        news.setTitle(title);
        news.setLink(link);

        return List.of(news);
    }

}
