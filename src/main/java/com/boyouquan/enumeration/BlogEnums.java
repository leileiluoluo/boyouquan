package com.boyouquan.enumeration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public enum BlogEnums {

    LEILEILUOLUO("leileiluoluo@leileiluoluo.com", "https://leileiluoluo.com/index.xml"),
    XIAOWULEYI("0471666@gmail.com", "https://www.xiaowuleyi.com/feed"),
    WEIYEXING("weiyexing1@gmail.com", "https://weiyexing.ml/rss.xml"),
    TIANXIANZI("mimismeoww@gmail.com", "https://tianxianzi.me/atom.xml"),
    MACIN("leileiluoluo@leileiluoluo.com", "https://www.macin.org/atom.xml"),
    RUSHIHU("leileiluoluo@leileiluoluo.com", "http://rushihu.com/feed"),
    ATPX("leileiluoluo@leileiluoluo.com", "https://atpx.com/feed.xml"),
    TYPEMYLIFE("leileiluoluo@leileiluoluo.com", "https://www.typemylife.com/feed/"),
    OWLSWIMS("leileiluoluo@leileiluoluo.com", "https://owlswims.com/feed/");

    private final String email;
    private final String feedAddress;

    BlogEnums(String email, String feedAddress) {
        this.email = email;
        this.feedAddress = feedAddress;
    }

    public String getEmail() {
        return this.email;
    }

    public String getFeedAddress() {
        return this.feedAddress;
    }

    public static List<String> getAllFeedAddresses() {
        return Arrays.stream(BlogEnums.values()).map(BlogEnums::getFeedAddress).collect(Collectors.toList());
    }

    public static String getEmailByBlogAddress(String blogAddress) {
        for (BlogEnums e : BlogEnums.values()) {
            if (e.getFeedAddress().contains(blogAddress)) {
                return e.getEmail();
            }
        }
        return "notfound@notfound.com";
    }

}
