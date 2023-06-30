package com.boyouquan.service;

import com.boyouquan.model.BlogPost;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class RSSReaderServiceImplTest {

    @Autowired
    private RSSReaderService rssReaderService;
    @Autowired
    private BlogPostService blogPostService;

    @Test
    public void testRead() {
        List<BlogPost> blogPosts = rssReaderService.read("https://owlswims.com/feed/");
//        save(blogPosts);
    }

    private void save(List<BlogPost> blogPosts) {
        if (null != blogPosts) {
            for (BlogPost blogPost: blogPosts) {
                blogPostService.saveBlogPost(blogPost);
            }
        }
    }

}
