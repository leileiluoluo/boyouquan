package com.boyouquan.service;

import com.boyouquan.model.BlogPost;

import java.util.List;

public interface RSSReaderService {

    List<BlogPost> read(String feedURL);

}
