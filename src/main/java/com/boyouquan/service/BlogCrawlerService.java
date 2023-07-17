package com.boyouquan.service;

import com.boyouquan.model.RSSInfo;

public interface BlogCrawlerService {

    RSSInfo getRSSInfoByRSSAddress(String rssAddress, int postsLimit);

}
