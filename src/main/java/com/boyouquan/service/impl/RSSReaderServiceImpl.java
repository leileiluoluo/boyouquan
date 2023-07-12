package com.boyouquan.service.impl;

import com.boyouquan.model.BlogPost;
import com.boyouquan.service.RSSReaderService;
import com.boyouquan.util.CommonUtils;
import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndLink;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

@Service
public class RSSReaderServiceImpl implements RSSReaderService {

    @Override
    public List<BlogPost> read(String feedURL) {
        List<BlogPost> blogPosts = new ArrayList<>();

        URL feedSource = null;
        try {
            feedSource = new URL(feedURL);
            SyndFeed feed = new SyndFeedInput().build(new XmlReader(feedSource));
            String blogName = feed.getTitle();

            // address
            String blogAddress = feed.getLink();
            List<SyndLink> links = feed.getLinks();
            if (null != links) {
                Optional<SyndLink> address = links.stream().filter(item -> item.getHref().contains("http")).findFirst();
                if (address.isPresent()) {
                    blogAddress = address.get().getHref();
                }
            }

            // for
            for (Iterator iter = feed.getEntries().iterator(); iter.hasNext(); ) {
                SyndEntry entry = (SyndEntry) iter.next();
                String title = entry.getTitle();

                // content
                String description = "";
                List<SyndContent> contents = entry.getContents();
                if (null != contents && !contents.isEmpty()) {
                    description = contents.get(0).getValue();
                } else {
                    SyndContent descriptionContent = entry.getDescription();
                    description = descriptionContent.getValue();
                }

                description = parseAndTruncateHtml2Text(description, 200);

                // link
                String link = entry.getUri();
                if (StringUtils.isBlank(link) || !link.startsWith("http")) {
                    link = entry.getLink();
                }
                Date createdAt = entry.getPublishedDate();
                if (null == createdAt) {
                    createdAt = entry.getUpdatedDate();
                }

                // FIXME
                if (StringUtils.isBlank(blogName) || StringUtils.isBlank(blogAddress)
                        || !blogAddress.startsWith("http")
                        || StringUtils.isBlank(title) || StringUtils.isBlank(link)
                        || !link.startsWith("http")
                        || null == createdAt) {
                    System.out.printf("invalid entry, feedURL: %s", feedURL);
                    break;
                }

                // add entry
                blogAddress = CommonUtils.trimFeedURLSuffix(blogAddress);
                BlogPost blogPost = new BlogPost();
                blogPost.setBlogName(blogName);
                blogPost.setBlogAddress(blogAddress);
                blogPost.setTitle(title);
                blogPost.setDescription(description);
                blogPost.setLink(link);
                blogPost.setCreatedAt(createdAt);
                blogPosts.add(blogPost);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return blogPosts;
    }

    private static String parseAndTruncateHtml2Text(String html, int length) {
        if (StringUtils.isBlank(html)) {
            return "...";
        }

        String text = Jsoup.parse(html).text();
        if (text.length() <= length) {
            return text;
        } else {
            text = text.substring(0, length);
            text += "...";
        }
        return text;
    }

}
