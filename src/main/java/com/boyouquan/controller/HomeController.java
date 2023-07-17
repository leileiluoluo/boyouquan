package com.boyouquan.controller;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.model.*;
import com.boyouquan.service.*;
import com.boyouquan.util.Pagination;
import com.boyouquan.util.PaginationBuilder;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("")
public class HomeController {

    private final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private BlogService blogService;
    @Autowired
    private PostService postService;
    @Autowired
    private AccessService accessService;
    @Autowired
    private LatestNewsService latestNewsService;

    @GetMapping("")
    public void index(HttpServletResponse response) {
        try {
            response.sendRedirect("/home");
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @GetMapping("/home")
    public String home(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        return homeWithPagination(keyword, 1, model);
    }

    @GetMapping("/home/page/{page}")
    public String homeWithPagination(@RequestParam(value = "keyword", required = false) String keyword, @PathVariable("page") int page, Model model) {
        if (null == keyword) {
            keyword = "";
        }
        keyword = StringUtils.trim(keyword);

        // posts
        Pagination<Post> postPagination = postService.listWithKeyWord(keyword, page, CommonConstants.DEFAULT_PAGE_SIZE);
        List<PostInfo> postInfos = new ArrayList<>();
        for (Post post : postPagination.getResults()) {
            PostInfo postInfo = new PostInfo();
            BeanUtils.copyProperties(post, postInfo);

            Blog blog = blogService.getByDomainName(post.getBlogDomainName());
            postInfo.setBlogName(blog.getName());
            postInfo.setBlogAddress(blog.getAddress());
            String blogAdminSmallImageURL = blogService.getBlogAdminSmallImageURLByDomainName(blog.getDomainName());
            postInfo.setBlogAdminSmallImageURL(blogAdminSmallImageURL);

            Long linkAccessCount = accessService.countByLink(post.getLink());
            postInfo.setLinkAccessCount(linkAccessCount);
            postInfos.add(postInfo);
        }

        Pagination<PostInfo> postInfoPagination = PaginationBuilder.<PostInfo>newBuilder()
                .pageNo(page)
                .pageSize(postPagination.getPageSize())
                .total(postPagination.getTotal())
                .results(postInfos).build();

        boolean hasLatestNews = false;
        List<LatestNews> latestNews = latestNewsService.getLatestNews();
        hasLatestNews = latestNews.size() > 1;

        model.addAttribute("pagination", postInfoPagination);
        model.addAttribute("totalBlogs", blogService.countAll());
        model.addAttribute("totalBlogPosts", postService.countAll());
        model.addAttribute("accessTotal", accessService.countAll());
        model.addAttribute("hasKeyword", StringUtils.isNotBlank(keyword));
        model.addAttribute("keyword", keyword);
        model.addAttribute("hasLatestNews", hasLatestNews);
        model.addAttribute("latestNews", latestNews);

        return "home";
    }

}
