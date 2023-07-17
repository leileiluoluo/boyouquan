package com.boyouquan.scheduler;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.model.Blog;
import com.boyouquan.model.Post;
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

import java.util.ArrayList;
import java.util.List;

@Component
@EnableScheduling
@EnableAsync
public class PostsScheduler {

    private final Logger logger = LoggerFactory.getLogger(PostsScheduler.class);

    @Autowired
    private BlogService blogService;
    @Autowired
    private PostService postService;
    @Autowired
    private BlogCrawlerService blogCrawlerService;

    @Async
    @Scheduled(cron = "0 0 0/1 * * ?")
    public void crawlingBlogPosts() {
        logger.info("posts scheduler started!");

        savePosts();
    }

    private void savePosts() {
        List<Blog> blogs = blogService.listAll();
        for (Blog blog : blogs) {
            try {
                logger.info("start crawling posts, blogDomainName: {}", blog.getDomainName());

                RSSInfo rssInfo = blogCrawlerService.getRSSInfoByRSSAddress(blog.getRssAddress(), CommonConstants.RSS_POST_COUNT_READ_LIMIT);
                if (null != rssInfo) {
                    // save posts
                    List<Post> posts = new ArrayList<>();
                    int count = 0;
                    for (RSSInfo.Post rssPost : rssInfo.getBlogPosts()) {
                        String link = rssPost.getLink();
                        boolean exists = postService.existsByLink(link);
                        if (!exists) {
                            Post post = new Post();
                            post.setLink(rssPost.getLink());
                            post.setTitle(rssPost.getTitle());
                            post.setDescription(rssPost.getDescription());
                            post.setPublishedAt(rssPost.getPublishedAt());
                            post.setBlogDomainName(blog.getDomainName());

                            posts.add(post);
                            count++;
                        }
                    }

                    // batch save
                    postService.batchSave(posts);
                    logger.info("{} posts saved, blogDomainName: {}", count, blog.getDomainName());
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

}
