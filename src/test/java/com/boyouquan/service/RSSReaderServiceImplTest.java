package com.boyouquan.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RSSReaderServiceImplTest {

    @Autowired
    private RSSReaderService rssReaderService;

    @Test
    public void testRead() {
        rssReaderService.read("");
    }

}
