package com.boyouquan.controller;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.model.BlogInfo;
import com.boyouquan.model.BlogSortType;
import com.boyouquan.service.AccessService;
import com.boyouquan.service.BlogService;
import com.boyouquan.service.PostService;
import com.boyouquan.util.Pagination;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/blogs")
public class BlogListRestController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private PostService postService;
    @Autowired
    private AccessService accessService;

    @GetMapping("")
    public Pagination<BlogInfo> list(
            @RequestParam(value = "sort", required = false, defaultValue = "collect_time") String sort,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        if (null == keyword) {
            keyword = "";
        }
        keyword = StringUtils.trim(keyword);

        return blogService.listBlogInfosWithKeyWord(BlogSortType.of(sort), keyword, page, CommonConstants.DEFAULT_PAGE_SIZE);
    }

}
