package com.boyouquan.helper;

import com.boyouquan.model.Post;
import com.boyouquan.model.RSSInfo;
import com.boyouquan.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class PostHelper {

    private final Logger logger = LoggerFactory.getLogger(PostHelper.class);

    @Autowired
    private PostService postService;

    public boolean savePosts(String blogDomainName, RSSInfo rssInfo, boolean draft) {
        if (null != rssInfo) {
            // save posts
            List<Post> posts = new ArrayList<>();
            for (RSSInfo.Post rssPost : rssInfo.getBlogPosts()) {
                String link = rssPost.getLink();
                boolean exists = postService.existsByLink(link);

                Date publishedAt = rssPost.getPublishedAt();
                Date latestPublishedAt = postService.getLatestPublishedAtByBlogDomainName(blogDomainName);

                boolean isNewPost = (null == latestPublishedAt || publishedAt.after(latestPublishedAt));

                if (!exists && isNewPost) {
                    Post post = new Post();
                    post.setLink(rssPost.getLink());
                    post.setTitle(rssPost.getTitle());
                    post.setDescription(rssPost.getDescription());
                    post.setPublishedAt(rssPost.getPublishedAt());
                    post.setBlogDomainName(blogDomainName);
                    post.setDraft(draft);

                    posts.add(post);
                }
            }

            // batch save
            return postService.batchSave(posts);
        }

        return false;
    }

}
