package com.boyouquan.service.impl;

import com.boyouquan.enumeration.BlogEnums;
import com.boyouquan.model.BlogAggregate;
import com.boyouquan.model.BlogInfo;
import com.boyouquan.model.BlogPost;
import com.boyouquan.service.BlogAccessService;
import com.boyouquan.service.BlogInfoService;
import com.boyouquan.service.BlogPostService;
import com.boyouquan.util.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BlogInfoServiceImpl implements BlogInfoService {

    @Autowired
    private BlogPostService blogPostService;
    @Autowired
    private BlogAccessService blogAccessService;

    @Override
    public Pagination<BlogInfo> listBlogInfos(String keyword, int page, int size) {
        List<BlogInfo> blogInfos = new ArrayList<>();

        int total = blogPostService.countBlogs(keyword).intValue();

        Pagination<BlogAggregate> pagination = blogPostService.listBlogsOrderByPostDate(keyword, page, size);
        pagination.getResults().forEach(blogAggregate -> {
            BlogInfo blogInfo = new BlogInfo();
            blogInfo.setName(blogAggregate.getName());
            blogInfo.setAddress(blogAggregate.getAddress());
            blogInfo.setDescription(BlogEnums.getDescriptionByBlogAddress(blogAggregate.getAddress()));
            blogInfo.setCreatedAt(BlogEnums.getCreatedAtByBlogAddress(blogAggregate.getAddress()));
            blogInfo.setLatestUpdatedAt(blogAggregate.getLatestUpdatedAt());
            blogInfo.setPostsCount(blogAggregate.getPostCount());

            Long accessCount = blogAccessService.countBlogAccessByLinkPrefix(blogInfo.getAddress());
            blogInfo.setAccessCount(accessCount);

            Pagination<BlogPost> blogPosts = blogPostService.listLatestBlogPostsByAddress(blogAggregate.getAddress(), 1, 3);
            blogInfo.setLatestPosts(blogPosts.getResults());
            blogInfos.add(blogInfo);
        });

        return Pagination.<BlogInfo>builder().pageNo(page).pageSize(size).total(total).results(blogInfos);
    }

}
