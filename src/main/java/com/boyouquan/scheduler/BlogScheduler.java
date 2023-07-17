package com.boyouquan.scheduler;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.enumeration.BlogEnums;
import com.boyouquan.model.Blog;
import com.boyouquan.model.RSSInfo;
import com.boyouquan.service.BlogCrawlerService;
import com.boyouquan.service.BlogService;
import com.boyouquan.service.PostService;
import com.boyouquan.util.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class BlogScheduler implements ApplicationRunner {

    private final Logger logger = LoggerFactory.getLogger(BlogScheduler.class);

    @Autowired
    private BlogCrawlerService blogCrawlerService;
    @Autowired
    private BlogService blogService;
    @Autowired
    private PostService postService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("blog scheduler started!");

        try {
            for (BlogEnums e : BlogEnums.values()) {
                String rssAddress = e.getFeedAddress();

                boolean exists = blogService.existsByRssAddress(rssAddress);
                if (!exists) {
                    RSSInfo rssInfo = blogCrawlerService.getRSSInfoByRSSAddress(rssAddress, CommonConstants.RSS_POST_COUNT_READ_LIMIT);
                    if (null == rssInfo) {
                        logger.error("rss info read failed, rssAddress: {}", rssAddress);
                        continue;
                    }

                    // save blog
                    saveBlog(e, rssInfo);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void saveBlog(BlogEnums blogEnum, RSSInfo rssInfo) {
        try {
            String blogDomainName = CommonUtils.getDomain(rssInfo.getBlogAddress());
            boolean exists = blogService.existsByDomainName(blogDomainName);
            if (!exists && !rssInfo.getBlogPosts().isEmpty()) {
                Blog blog = new Blog();
                Date collectedAt = new SimpleDateFormat("yyyy/MM/dd").parse(blogEnum.getCreatedAt());
                blog.setDomainName(blogDomainName);
                blog.setAdminEmail(blogEnum.getEmail());
                blog.setName(rssInfo.getBlogName());
                blog.setAddress(rssInfo.getBlogAddress());
                blog.setRssAddress(blogEnum.getFeedAddress());
                blog.setDescription(blogEnum.getDescription());
                blog.setSelfSubmitted(blogEnum.getSelfSubmitted());
                blog.setCollectedAt(collectedAt);
                blog.setUpdatedAt(collectedAt);

                // save
                blogService.save(blog);

                logger.info("blog saved, blogDomainName: {}", blog.getDomainName());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

}
