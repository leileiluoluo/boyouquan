package com.boyouquan.service.impl;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.model.RSSInfo;
import com.boyouquan.service.BlogCrawlerService;
import com.boyouquan.util.CommonUtils;
import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndLink;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpUriRequest;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class BlogCrawlerServiceImpl implements BlogCrawlerService {

    private final Logger logger = LoggerFactory.getLogger(BlogCrawlerServiceImpl.class);

    private final CloseableHttpClient customClient = HttpClients.custom()
            .setConnectionManagerShared(true)
            .setUserAgent(CommonConstants.DATA_SPIDER_USER_AGENT)
            .build();

    @Override
    public RSSInfo getRSSInfoByRSSAddress(String rssAddress, int postsLimit) {
        int postCount = 0;

        try (CloseableHttpClient client = customClient) {
            HttpUriRequest request = new HttpGet(rssAddress);
            try (CloseableHttpResponse response = client.execute(request); InputStream stream = response.getEntity().getContent()) {
                SyndFeed feed = new SyndFeedInput().build(new XmlReader(stream));

                // blog name
                String blogName = feed.getTitle().trim();
                if (blogName.length() > CommonConstants.BLOG_NAME_MAX_LENGTH) {
                    int blankIndex = blogName.indexOf(" ");
                    if (blankIndex > 0) {
                        blogName = blogName.substring(0, blankIndex);
                    } else {
                        blogName = blogName.substring(0, CommonConstants.BLOG_NAME_MAX_LENGTH);
                    }
                }

                // blog address
                String blogAddress = feed.getLink();
                List<SyndLink> links = feed.getLinks();
                if (null != links) {
                    Optional<SyndLink> address = links.stream().filter(item -> item.getHref().contains("http")).findFirst();
                    if (address.isPresent()) {
                        blogAddress = address.get().getHref();
                    }
                }

                // FIXME
                if (StringUtils.isBlank(blogName) || StringUtils.isBlank(blogAddress)
                        || !blogAddress.startsWith("http")) {
                    logger.info("invalid rssAddress: {}", rssAddress);
                    return null;
                }

                RSSInfo rssInfo = new RSSInfo();
                List<RSSInfo.Post> posts = new ArrayList<>();

                rssInfo.setBlogName(blogName);
                rssInfo.setBlogAddress(blogAddress);
                rssInfo.setBlogPosts(posts);

                // for
                for (Iterator iter = feed.getEntries().iterator(); iter.hasNext(); ) {
                    SyndEntry entry = (SyndEntry) iter.next();
                    String title = entry.getTitle();

                    // description
                    String description = "";
                    List<SyndContent> contents = entry.getContents();
                    if (null != contents && !contents.isEmpty()) {
                        description = contents.get(0).getValue();
                    } else {
                        SyndContent descriptionContent = entry.getDescription();
                        description = descriptionContent.getValue();
                    }

                    description = CommonUtils.parseAndTruncateHtml2Text(description, 200);

                    // link
                    String link = entry.getUri();
                    if (StringUtils.isBlank(link) || !link.startsWith("http")) {
                        link = entry.getLink();
                    }
                    Date publishedAt = entry.getPublishedDate();
                    if (null == publishedAt) {
                        publishedAt = entry.getUpdatedDate();
                    }

                    // FIXME
                    if (StringUtils.isBlank(title) || StringUtils.isBlank(link)
                            || !link.startsWith("http")
                            || null == publishedAt) {
                        logger.info("invalid entry, rssAddress: {}", rssAddress);
                        break;
                    }

                    // add entry
                    blogAddress = CommonUtils.trimFeedURLSuffix(blogAddress);

                    RSSInfo.Post post = new RSSInfo.Post();
                    post.setLink(link);
                    post.setTitle(title);
                    post.setDescription(description);
                    post.setPublishedAt(publishedAt);
                    posts.add(post);

                    postCount++;
                    if (postCount >= postsLimit) {
                        return rssInfo;
                    }
                }

                return rssInfo;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

}
