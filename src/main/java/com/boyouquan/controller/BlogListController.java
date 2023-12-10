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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/blogs")
public class BlogListController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private PostService postService;
    @Autowired
    private AccessService accessService;

    @GetMapping("")
    public String list(
            @RequestParam(value = "sort", required = false, defaultValue = "access_count") String sort,
            @RequestParam(value = "keyword", required = false) String keyword,
            Model model) {
        return list(sort, keyword, 1, model);
    }

    @GetMapping("/page/{page}")
    public String list(
            @RequestParam(value = "sort", required = false, defaultValue = "access_count") String sort,
            @RequestParam(value = "keyword", required = false) String keyword, @PathVariable("page") int page,
            Model model) {
        if (null == keyword) {
            keyword = "";
        }
        keyword = StringUtils.trim(keyword);

        Pagination<BlogInfo> blogInfoPagination = blogService.listBlogInfosWithKeyWord(BlogSortType.of(sort), keyword, page, CommonConstants.DEFAULT_PAGE_SIZE);
        model.addAttribute("pagination", blogInfoPagination);
        model.addAttribute("sort", sort);
        model.addAttribute("hasKeyword", StringUtils.isNotBlank(keyword));
        model.addAttribute("keyword", keyword);
        model.addAttribute("totalBlogs", blogService.countAll());
        model.addAttribute("totalBlogPosts", postService.countAll());
        model.addAttribute("accessTotal", accessService.countAll());
        return "blogs/list";
    }

}
