package com.boyouquan.service.impl;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.model.Blog;
import com.boyouquan.model.Post;
import com.boyouquan.model.PostSortType;
import com.boyouquan.service.BlogService;
import com.boyouquan.service.FeedService;
import com.boyouquan.service.PostService;
import com.boyouquan.util.CommonUtils;
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
    @Autowired
    private BlogService blogService;

    @Override
    public String generateFeedXML(PostSortType sortType) {
        SyndFeed feed = new SyndFeedImpl();
        feed.setFeedType("rss_2.0");
        feed.setTitle("博友圈");
        feed.setDescription("博客人的朋友圈");
        feed.setLink(CommonConstants.HOME_PAGE_ADDRESS);
        if (PostSortType.recommended.equals(sortType)) {
            feed.setTitle("博友圈 - 推荐文章聚合");
            feed.setDescription("聚合展示博友圈推荐文章");
            feed.setLink(CommonConstants.HOME_PAGE_SORT_RECOMMENDED_ADDRESS);
        } else if (PostSortType.latest.equals(sortType)) {
            feed.setTitle("博友圈 - 最新文章聚合");
            feed.setDescription("聚合展示博友圈最新文章");
            feed.setLink(CommonConstants.HOME_PAGE_SORT_LATEST_ADDRESS);
        }

        try {
            Pagination<Post> posts = postService.listWithKeyWord(sortType, "", CommonConstants.FEED_POST_QUERY_PAGE_NO, CommonConstants.FEED_POST_QUERY_PAGE_SIZE);

            List<SyndEntry> entries = posts.getResults().stream()
                    .map(post -> {
                        SyndEntry entry = new SyndEntryImpl();
                        entry.setTitle(post.getTitle());

                        String postLink = CommonUtils.urlEncode(post.getLink());
                        String link = String.format("%s?from=feed&link=%s", CommonConstants.GO_PAGE_ADDRESS, postLink);
                        entry.setLink(link);

                        Blog blog = blogService.getByDomainName(post.getBlogDomainName());
                        if (null != blog) {
                            entry.setAuthor(blog.getName());
                        }

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
