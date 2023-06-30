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
            feedSource = new URL("https://leileiluoluo.com/index.xml");
            SyndFeed feed = new SyndFeedInput().build(new XmlReader(feedSource));
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
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return blogPosts;
    }

}
