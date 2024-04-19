package com.boyouquan.helper;

import com.boyouquan.model.Post;
import com.boyouquan.model.RSSInfo;
import com.boyouquan.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class PostHelper {

    @Autowired
    private PostService postService;

    public int savePosts(String blogDomainName, RSSInfo rssInfo, boolean draft) {
        if (null != rssInfo) {
            // save posts
            List<Post> posts = new ArrayList<>();
            for (RSSInfo.Post rssPost : rssInfo.getBlogPosts()) {
                String link = rssPost.getLink();
                boolean existsByLink = postService.existsByLink(link);
                boolean existsByTitle = postService.existsByTitle(rssPost.getTitle());

                Date publishedAt = rssPost.getPublishedAt();
                boolean isValidNewPost = isValidNewPost(publishedAt);

                if (!existsByLink && !existsByTitle && isValidNewPost) {
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

        return 0;
    }

    private boolean isValidNewPost(Date publishedAt) {
        Date now = new Date();
        return publishedAt.before(now);
    }

}
