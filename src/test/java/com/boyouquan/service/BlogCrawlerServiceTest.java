package com.boyouquan.service;

import com.boyouquan.constant.CommonConstants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BlogCrawlerServiceTest {

    @Autowired
    private BlogCrawlerService blogCrawlerService;

    @Test
    public void testGetRSSInfoByRSSAddress() {
        String rssAddress = "https://saltyleo.com/atom.xml";
        blogCrawlerService.getRSSInfoByRSSAddress(rssAddress, CommonConstants.RSS_POST_COUNT_READ_LIMIT_FIRST_TIME);
    }

}

