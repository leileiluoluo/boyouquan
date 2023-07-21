package com.boyouquan.service.impl;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.model.RSSInfo;
import com.boyouquan.service.BlogCrawlerService;
import com.boyouquan.util.CommonUtils;
import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndLink;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpUriRequest;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.util.Timeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.*;

@Service
public class BlogCrawlerServiceImpl implements BlogCrawlerService {

    private final Logger logger = LoggerFactory.getLogger(BlogCrawlerServiceImpl.class);

    @Override
    public RSSInfo getRSSInfoByRSSAddress(String rssAddress, int postsLimit) {
        int postCount = 0;

        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(Timeout.ofMinutes(1))
                .setResponseTimeout(Timeout.ofMinutes(1))
                .build();

        final CloseableHttpClient customClient = HttpClients.custom()
                .setDefaultRequestConfig(defaultRequestConfig)
                .setUserAgent(CommonConstants.DATA_SPIDER_USER_AGENT)
                .build();

        try (CloseableHttpClient client = customClient) {
            HttpUriRequest request = new HttpGet(rssAddress);
            try (CloseableHttpResponse response = client.execute(request); InputStream stream = response.getEntity().getContent()) {
                SyndFeed feed = new SyndFeedInput().build(new XmlReader(stream));

                // blog name
                String blogName = feed.getTitle().trim();
                if (blogName.length() > CommonConstants.BLOG_NAME_MAX_LENGTH) {
                    int dashIndex = blogName.indexOf("-");
                    if (dashIndex > 0) {
                        blogName = blogName.substring(0, dashIndex);
                    } else {
                        int blankIndex = blogName.indexOf(" ");
                        if (blankIndex > 0) {
                            blogName = blogName.substring(0, blankIndex);
                        } else {
                            blogName = blogName.substring(0, CommonConstants.BLOG_NAME_MAX_LENGTH);
                        }
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

                rssInfo.setBlogName(blogName.trim());
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
                    String link = entry.getLink();
                    if (StringUtils.isBlank(link) || !link.startsWith("http")) {
                        link = entry.getUri();
                    }
                    Date publishedAt = entry.getPublishedDate();
                    if (null == publishedAt) {
                        publishedAt = entry.getUpdatedDate();
                    }

                    // FIXME
                    if (StringUtils.isBlank(title) || StringUtils.isBlank(link)
                            || !link.startsWith("http")
                            || StringUtils.isBlank(description)
                            || null == publishedAt) {
                        logger.info("invalid entry, rssAddress: {}", rssAddress);
                        break;
                    }

                    // add entry
                    blogAddress = CommonUtils.trimFeedURLSuffix(blogAddress);

                    RSSInfo.Post post = new RSSInfo.Post();
                    post.setLink(link.trim());
                    post.setTitle(title.trim());
                    post.setDescription(description.trim());
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
