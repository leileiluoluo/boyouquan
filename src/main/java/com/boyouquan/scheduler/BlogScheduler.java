package com.boyouquan.scheduler;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.enumeration.BlogEnums;
import com.boyouquan.helper.PostHelper;
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

import java.text.ParseException;
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
    @Autowired
    private PostHelper postHelper;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("blog scheduler started!");

        try {
            for (BlogEnums e : BlogEnums.values()) {
                String rssAddress = e.getRssAddress();

                boolean exists = blogService.existsByRssAddress(rssAddress);
                if (!exists) {
                    RSSInfo rssInfo = blogCrawlerService.getRSSInfoByRSSAddress(rssAddress, CommonConstants.RSS_POST_COUNT_READ_LIMIT);
                    if (null == rssInfo) {
                        logger.error("rss info read failed, rssAddress: {}", rssAddress);
                        continue;
                    }

                    // save blog and posts
                    saveBlogAndPosts(e, rssInfo);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void saveBlogAndPosts(BlogEnums blogEnum, RSSInfo rssInfo) throws ParseException {
        String blogDomainName = CommonUtils.getDomain(rssInfo.getBlogAddress());

        boolean exists = blogService.existsByDomainName(blogDomainName);
        if (!exists && !rssInfo.getBlogPosts().isEmpty()) {
            Blog blog = new Blog();
            Date collectedAt = new SimpleDateFormat("yyyy/MM/dd").parse(blogEnum.getCollectedAt());
            blog.setDomainName(blogDomainName);
            blog.setAdminEmail(blogEnum.getAdminEmail());
            blog.setName(rssInfo.getBlogName());
            blog.setAddress(rssInfo.getBlogAddress());
            blog.setRssAddress(blogEnum.getRssAddress());
            blog.setDescription(blogEnum.getDescription());
            blog.setSelfSubmitted(blogEnum.getSelfSubmitted());
            blog.setCollectedAt(collectedAt);
            blog.setUpdatedAt(collectedAt);

            // save blog
            blogService.save(blog);

            // save posts
            postHelper.savePosts(blogDomainName, rssInfo);

            logger.info("blog and posts saved, blogDomainName: {}, postCount: {}", blog.getDomainName(), rssInfo.getBlogPosts().size());
        }
    }

}
