package com.boyouquan.constant;

public class CommonConstants {

    public static final int DEFAULT_PAGE_SIZE = 10;

    public static final String DATA_SPIDER_USER_AGENT = "Mozilla/5.0 (compatible; Boyouquanspider/1.0; +https://www.boyouquan.com/about#data-spider)";
    public static final String HEADER_USER_AGENT = "User-Agent";
    public static final String SCHEME_HTTP = "http";

    public static final String FAKE_BLOG_ADMIN_EMAIL = "notfound@notfound.com";

    public static final String BLOG_ADDED_WELCOME_PATTERN = "恭喜「%s」被本站收录！";
    public static final String MOST_ACCESSED_BLOG_ANNOUNCE_PATTERN = "最近一个月浏览最多的博客是「%s」，浏览 %d 次！";
    public static final String MOST_UPDATED_BLOG_ANNOUNCE_PATTERN = "最近一个月更博最频繁的博友是「%s」，更博 %d 篇！";
    public static final String BLOG_LIST_ADDRESS = "/blogs";
    public static final String BLOG_ITEM_ADDRESS_PATTERN = "/blogs?keyword=%s";

    public static final String GRAVATAR_SOURCE_ADDRESS = "https://seccdn.libravatar.org/gravatarproxy/%s?s=%d";
    public static final String GRAVATAR_STORE_FOLDER = "gravatar/%s/";
    public static final String GRAVATAR_DEFAULT_IMAGE_MD5_EMAIL = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
    public static final int GRAVATAR_IMAGE_SMALL_SIZE = 20;
    public static final int GRAVATAR_IMAGE_LARGE_SIZE = 80;
    public static final String GRAVATAR_ADDRESS_SMALL_SIZE = "/gravatar/%s?size=" + GRAVATAR_IMAGE_SMALL_SIZE;
    public static final String GRAVATAR_ADDRESS_LARGE_SIZE = "/gravatar/%s?size=" + GRAVATAR_IMAGE_LARGE_SIZE;

    public static final int RSS_POST_COUNT_READ_LIMIT_FIRST_TIME = 1000;
    public static final int RSS_POST_COUNT_READ_LIMIT = 10;
    public static final int RSS_BLOG_DESCRIPTION_LENGTH_LIMIT = 200;
    public static final int LATEST_POST_COUNT_LIMIT = 3;
    public static final int ALL_POST_COUNT_LIMIT = 100;

    public static final String FULL_BLOG_LIST_ADDRESS = "https://www.boyouquan.com/blogs";

    public static final int FEED_POST_QUERY_PAGE_NO = 1;
    public static final int FEED_POST_QUERY_PAGE_SIZE = 20;

}
