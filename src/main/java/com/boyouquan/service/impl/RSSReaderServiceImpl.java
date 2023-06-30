package com.boyouquan.service.impl;

import com.boyouquan.model.BlogPost;
import com.boyouquan.service.RSSReaderService;
import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

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
            String blogAddress = feed.getLink();

            for (Iterator iter = feed.getEntries().iterator(); iter.hasNext(); ) {
                SyndEntry entry = (SyndEntry) iter.next();
                String title = entry.getTitle();
                SyndContent descriptionContent = entry.getDescription();
                String description = descriptionContent.getValue();
                description = splitAndFilterString(description, 100);
                String link = entry.getUri();
                Date createdAt = entry.getPublishedDate();
                if (null == createdAt) {
                    createdAt = entry.getUpdatedDate();
                }

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

    public static String splitAndFilterString(String input, int length) {
        if (input == null || input.trim().equals("")) {
            return "";
        }
        // 去掉所有html元素,
        String str = input.replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll(
                "<[^>]*>", "");
        str = str.replaceAll("[(/>)<]", "");
        int len = str.length();
        if (len <= length) {
            return str;
        } else {
            str = str.substring(0, length);
            str += "...";
        }
        return str;
    }

}
