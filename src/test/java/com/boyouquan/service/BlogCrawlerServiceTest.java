package com.boyouquan.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BlogCrawlerServiceTest {

    @Autowired
    private BlogCrawlerService blogCrawlerService;

    @Test
    public void testGetRSSInfoByRSSAddress() {
        String rssAddress = "https://www.iclws.com/feed";
        blogCrawlerService.getRSSInfoByRSSAddress(rssAddress, 10);
    }

}
