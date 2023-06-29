package com.boyouquan.service.impl;

import com.boyouquan.model.BlogPost;
import com.boyouquan.service.RSSReaderService;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
            feedSource = new URL("https://leileiluoluo.com/index.xml");
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(feedSource));
            String blogName = feed.getTitle();
            String blogAddress = feed.getLink();

            for (Iterator iter = feed.getEntries().iterator(); iter.hasNext(); ) {
                SyndEntry entry = (SyndEntry) iter.next();
                String title = entry.getTitle();
                SyndContent descriptionContent = entry.getDescription();
                String description = descriptionContent.getValue();
                String link = entry.getUri();
                Date createdAt = entry.getPublishedDate();

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
            throw new RuntimeException(e);
        } catch (FeedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return blogPosts;
    }

}
