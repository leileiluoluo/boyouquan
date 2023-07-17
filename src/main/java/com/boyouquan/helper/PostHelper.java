package com.boyouquan.helper;

import com.boyouquan.model.Post;
import com.boyouquan.model.RSSInfo;
import com.boyouquan.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PostHelper {

    private final Logger logger = LoggerFactory.getLogger(PostHelper.class);

    @Autowired
    private PostService postService;

    public void savePosts(String blogDomainName, RSSInfo rssInfo) {
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
                    post.setBlogDomainName(blogDomainName);

                    posts.add(post);
                    count++;
                }
            }

            // batch save
            postService.batchSave(posts);
            logger.info("{} posts saved, blogDomainName: {}", count, blogDomainName);
        }
    }

}
