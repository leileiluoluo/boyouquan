package com.boyouquan.scheduler;

import com.boyouquan.config.BoYouQuanConfig;
import com.boyouquan.enumeration.BlogEnums;
import com.boyouquan.model.BlogPost;
import com.boyouquan.service.BlogPostService;
import com.boyouquan.service.RSSReaderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Component
@EnableScheduling
@EnableAsync
public class BlogPostsScheduler {

    private final Logger logger = LoggerFactory.getLogger(BlogPostsScheduler.class);

    @Autowired
    private BoYouQuanConfig boYouQuanConfig;
    @Autowired
    private RSSReaderService rssReaderService;
    @Autowired
    private BlogPostService blogPostService;

    @Async
    @Scheduled(cron = "0 0 0/1 * * ?")
    public void crawlingBlogPosts() {
        logger.info("scheduler started!");

        readAndSaveAllBlogs();
    }

    private void readAndSaveAllBlogs() {
        List<String> blogs = BlogEnums.getAllFeedAddresses();
        for (String blog : blogs) {
            try {
                logger.info("blog address: {}", blog);

                // only fetch the latest 10 posts
                List<BlogPost> blogPosts = rssReaderService.read(blog).stream().limit(10L).toList();
                if (!blogPosts.isEmpty()) {
                    Date minCreatedAt = blogPosts.stream().min(Comparator.comparing(BlogPost::getCreatedAt)).get().getCreatedAt();
                    String blogAddress = blogPosts.get(0).getBlogAddress();
                    blogPostService.deleteLaterBlogPostsByAddressAndDate(blogAddress, minCreatedAt);

                    logger.info("{} old blogs deleted for address: {}", blogPosts.size(), blogAddress);
                    save(blogPosts);
                    logger.info("{} new blogs saved for address: {}", blogPosts.size(), blogAddress);
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    private void save(List<BlogPost> blogPosts) {
        if (null != blogPosts) {
            for (BlogPost blogPost : blogPosts) {
                blogPostService.saveBlogPost(blogPost);
            }
        }
    }

}
