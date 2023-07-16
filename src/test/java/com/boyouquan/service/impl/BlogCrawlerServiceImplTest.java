package com.boyouquan.service.impl;

import com.boyouquan.service.BlogCrawlerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BlogCrawlerServiceImplTest {

    @Autowired
    private BlogCrawlerService blogCrawlerService;

    @Test
    public void testGetRSSInfoByRSSAddress() {
        String rssAddress = "https://leileiluoluo.com/index.xml";
        blogCrawlerService.getRSSInfoByRSSAddress(rssAddress);
    }

}
