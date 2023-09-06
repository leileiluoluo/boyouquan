package com.boyouquan.service.impl;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.model.Post;
import com.boyouquan.service.FeedService;
import com.boyouquan.service.PostService;
import com.boyouquan.util.Pagination;
import com.rometools.rome.feed.synd.*;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedServiceImpl implements FeedService {

    private static final Logger logger = LoggerFactory.getLogger(FeedServiceImpl.class);

    @Autowired
    private PostService postService;

    @Override
    public String generateFeedXML() {
        SyndFeed feed = new SyndFeedImpl();
        feed.setFeedType("rss_2.0");
        feed.setTitle("博友圈 - 最新文章聚合");
        feed.setDescription("聚合所收录博客的最新文章！");
        feed.setLink("https://www.boyouquan.com/home");

        try {
            Pagination<Post> posts = postService.listWithKeyWord("", CommonConstants.FEED_POST_QUERY_PAGE_NO, CommonConstants.FEED_POST_QUERY_PAGE_SIZE);

            List<SyndEntry> entries = posts.getResults().stream()
                    .map(post -> {
                        SyndEntry entry = new SyndEntryImpl();
                        entry.setTitle(post.getTitle());
                        entry.setLink(post.getLink());

                        SyndContent description = new SyndContentImpl();
                        description.setType("text/plain");
                        description.setValue(post.getDescription());
                        entry.setDescription(description);

                        entry.setPublishedDate(post.getPublishedAt());
                        return entry;
                    }).toList();

            feed.setEntries(entries);

            return new SyndFeedOutput().outputString(feed);
        } catch (FeedException e) {
            logger.error(e.getMessage(), e);
        }

        return "";
    }

}
