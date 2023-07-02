package com.boyouquan.enumeration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public enum BlogEnums {

    LEILEILUOLUO("leileiluoluo@leileiluoluo.com", "https://leileiluoluo.com/index.xml"),
    XIAOWULEYI("0471666@gmail.com", "https://www.xiaowuleyi.com/feed"),
    WEIYEXING("weiyexing1@gmail.com", "https://aaronnick.github.io/rss.xml"),
    TIANXIANZI("mimismeoww@gmail.com", "https://tianxianzi.me/atom.xml"),
    MACIN("hi@macin.org", "https://www.macin.org/atom.xml"),
    RUSHIHU("notfound@rushihu.com", "http://rushihu.com/feed"),
    ATPX("hello@atpx.com", "https://atpx.com/feed.xml"),
    TYPEMYLIFE("zhang@typemylife.com", "https://www.typemylife.com/feed/"),
    OWLSWIMS("heyday.misses_0l@icloud.com", "https://owlswims.com/feed/"),
    SI_ON("me@si-on.top", "https://blog.si-on.top/atom.xml"),
    QNCD("376977443@qq.com", "http://www.qncd.com/?feed=rss2"),
    CHANGHAI("lu_changhai@yahoo.com", "https://www.changhai.org/feed.xml"),
    MMBKZ("notfound@mmbkz.cn", "https://www.mmbkz.cn/feed/"),
    MUXER("i@lms.im", "https://muxer.cn/feed"),
    STEPHENLENG("lengge86@gmail.com", "https://stephenleng.com/feed/"),
    FENG("mypen@163.com", "https://feng.pub/feed"),
    SUBNOOC("nooc@nooc.ink", "https://subnooc.com/rss.xml"),
    JINGFENGSHUO("i@jingfengshuo.com", "https://www.jingfengshuo.com/feed"),
    ORDCHAOS("orderchaos@ordchaos.com", "https://www.ordchaos.com/atom.xml");

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
