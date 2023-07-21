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
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.*;

@Service
public class BlogCrawlerServiceImpl implements BlogCrawlerService {

    private final Logger logger = LoggerFactory.getLogger(BlogCrawlerServiceImpl.class);

    private static OkHttpClient client = new OkHttpClient();

    @Override
    public RSSInfo getRSSInfoByRSSAddress(String rssAddress, int postsLimit) {
        int postCount = 0;

        Request request = new Request.Builder()
                .addHeader(CommonConstants.HEADER_USER_AGENT, CommonConstants.DATA_SPIDER_USER_AGENT)
                .url(rssAddress)
                .build();

        Call call = client.newCall(request);

        try (Response response = call.execute(); InputStream stream = response.body().byteStream()) {
            SyndFeed feed = new SyndFeedInput().build(new XmlReader(stream));

            // blog address
            String blogAddress = parseBlogAddress(feed);

            // validation
            if (StringUtils.isBlank(blogAddress)
                    || !blogAddress.startsWith(CommonConstants.SCHEME_HTTP)) {
                logger.info("invalid rssAddress: {}", rssAddress);
                return null;
            }

            RSSInfo rssInfo = new RSSInfo();
            List<RSSInfo.Post> posts = new ArrayList<>();

            rssInfo.setBlogAddress(blogAddress);
            rssInfo.setBlogPosts(posts);

            // parse posts
            for (SyndEntry entry : feed.getEntries()) {
                // title
                String title = entry.getTitle();

                // description
                String description = parseDescription(entry);

                // link
                String link = parseLink(entry);

                // publishedAt
                Date publishedAt = parsePublishedAt(entry);

                // validation
                if (StringUtils.isBlank(title) || StringUtils.isBlank(link)
                        || !link.startsWith(CommonConstants.SCHEME_HTTP)
                        || StringUtils.isBlank(description)
                        || null == publishedAt) {
                    logger.info("invalid entry, title: {}, link: {}", title, link);
                    continue;
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
                    break;
                }
            }

            return rssInfo;
        } catch (Exception e) {
            logger.error("error in crawling blog", e);
            return null;
        }
    }

    private String parseBlogAddress(SyndFeed feed) {
        String blogAddress = feed.getLink();
        List<SyndLink> links = feed.getLinks();
        if (null != links) {
            Optional<SyndLink> address = links.stream().filter(item -> item.getHref().contains("http")).findFirst();
            if (address.isPresent()) {
                blogAddress = address.get().getHref();
            }
        }

        return blogAddress;
    }

    private String parseDescription(SyndEntry entry) {
        String description = "";
        List<SyndContent> contents = entry.getContents();
        if (null != contents && !contents.isEmpty()) {
            description = contents.get(0).getValue();
        } else {
            SyndContent descriptionContent = entry.getDescription();
            description = descriptionContent.getValue();
        }

        description = CommonUtils.parseAndTruncateHtml2Text(description, CommonConstants.RSS_BLOG_DESCRIPTION_LENGTH_LIMIT);
        return description;
    }

    private String parseLink(SyndEntry entry) {
        String link = entry.getLink();
        if (StringUtils.isBlank(link) || !link.startsWith(CommonConstants.SCHEME_HTTP)) {
            link = entry.getUri();
        }
        return link;
    }

    private Date parsePublishedAt(SyndEntry entry) {
        Date publishedAt = entry.getPublishedDate();
        if (null == publishedAt) {
            publishedAt = entry.getUpdatedDate();
        }
        return publishedAt;
    }

}