package com.boyouquan.scheduler;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.helper.PostHelper;
import com.boyouquan.model.Blog;
import com.boyouquan.model.RSSInfo;
import com.boyouquan.service.BlogCrawlerService;
import com.boyouquan.service.BlogService;
import com.boyouquan.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableScheduling
@EnableAsync
public class PostScheduler {

    private final Logger logger = LoggerFactory.getLogger(PostScheduler.class);

    @Autowired
    private BlogService blogService;
    @Autowired
    private PostService postService;
    @Autowired
    private BlogCrawlerService blogCrawlerService;
    @Autowired
    private PostHelper postHelper;

    @Async
    @Scheduled(cron = "0 0 0/1 * * ?")
    public void crawlingBlogPosts() {
        logger.info("post scheduler start!");

        savePosts();

        logger.info("post scheduler end!");
    }

    public void savePosts() {
        List<Blog> blogs = blogService.listAll();
        for (Blog blog : blogs) {
            try {
                logger.info("start crawling posts, blogDomainName: {}", blog.getDomainName());

                RSSInfo rssInfo = blogCrawlerService.getRSSInfoByRSSAddress(blog.getRssAddress(), CommonConstants.RSS_POST_COUNT_READ_LIMIT);

                // save posts
                boolean success = postHelper.savePosts(blog.getDomainName(), rssInfo, false);
                if (!success) {
                    logger.error("posts save failed, blogDomainName: {}", blog.getDomainName());
                    continue;
                }

                logger.info("posts save success, blogDomainName: {}", blog.getDomainName());
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

}
