package com.boyouquan.controller;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.helper.AdminLoginFormHelper;
import com.boyouquan.helper.BlogRequestFormHelper;
import com.boyouquan.helper.RecommendPostFormHelper;
import com.boyouquan.model.*;
import com.boyouquan.service.*;
import com.boyouquan.util.CommonUtils;
import com.boyouquan.util.Pagination;
import com.boyouquan.util.PaginationBuilder;
import com.boyouquan.util.PermissionUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.lang.module.Configuration;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private BlogRequestService blogRequestService;
    @Autowired
    private BlogRequestFormHelper requestFormHelper;
    @Autowired
    private RecommendPostFormHelper recommendPostFormHelper;
    @Autowired
    private AdminLoginFormHelper adminLoginFormHelper;
    @Autowired
    private UserService userService;
    @Autowired
    private BlogService blogService;
    @Autowired
    private PostService postService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private PinHistoryService pinHistoryService;

    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    @GetMapping("/blog-requests")
    public String listBlogRequests(
            @RequestParam(value = "keyword", required = false) String keyword,
            Model model, HttpServletRequest request) {
        return listBlogRequests(keyword, 1, model, request);
    }

    @GetMapping("/blog-requests/page/{page}")
    public String listBlogRequests(
            @RequestParam(value = "keyword", required = false) String keyword,
            @PathVariable("page") int page,
            Model model, HttpServletRequest request) {
        // permission check
        boolean hasAdminPermission = PermissionUtil.hasAdminPermission(request);
        if (!hasAdminPermission) {
            return "redirect:/admin/login";
        }

        // list
        List<BlogRequest.Status> statuses = Arrays.asList(
                BlogRequest.Status.submitted,
                BlogRequest.Status.system_check_valid,
                BlogRequest.Status.system_check_invalid,
                BlogRequest.Status.approved,
                BlogRequest.Status.rejected,
                BlogRequest.Status.uncollected
        );

        Pagination<BlogRequestInfo> pagination = blogRequestService.listBlogRequestInfosByStatuses(
                keyword, statuses, page, CommonConstants.DEFAULT_PAGE_SIZE);

        model.addAttribute("pagination", pagination);
        model.addAttribute("hasKeyword", StringUtils.isNotBlank(keyword));
        model.addAttribute("keyword", keyword);

        // user
        User user = (User) request.getSession().getAttribute("user");
        model.addAttribute("user", user);

        return "admin/blog_requests/list";
    }

    @GetMapping("/blog-requests/{id}")
    public String getBlogRequestById(@PathVariable("id") Long id, Model model, HttpServletRequest request) {
        // permission check
        boolean hasAdminPermission = PermissionUtil.hasAdminPermission(request);
        if (!hasAdminPermission) {
            return "user/no_permission/notice";
        }

        // get
        BlogRequestInfo blogRequestInfo = blogRequestService.getBlogRequestInfoById(id);

        model.addAttribute("blogRequestInfo", blogRequestInfo);

        return "admin/blog_requests/item";
    }

    @PatchMapping("/blog-requests/{id}/uncollected")
    public String uncollectedBlogById(@PathVariable("id") Long id, BlogUncollectedForm blogDeletedForm, HttpServletRequest request) {
        // permission check
        boolean hasAdminPermission = PermissionUtil.hasAdminPermission(request);
        if (!hasAdminPermission) {
            return "user/no_permission/notice";
        }

        // get
        BlogRequest blogRequest = blogRequestService.getById(id);
        if (null != blogRequest) {
            blogRequestService.uncollectedByRssAddress(blogRequest.getRssAddress(), blogDeletedForm.getReason());
        }

        return "redirect:/admin/blog-requests";
    }

    @DeleteMapping("/blog-requests/{id}")
    public String deleteBlogRequestById(@PathVariable("id") Long id, HttpServletRequest request) {
        // permission check
        boolean hasAdminPermission = PermissionUtil.hasAdminPermission(request);
        if (!hasAdminPermission) {
            return "user/no_permission/notice";
        }

        // get
        BlogRequest blogRequest = blogRequestService.getById(id);
        if (null != blogRequest) {
            blogRequestService.deleteByRssAddress(blogRequest.getRssAddress());
        }

        return "redirect:/admin/blog-requests";
    }

    @GetMapping("/blog-requests/add")
    public String addBlogRequestForm(BlogRequestForm blogRequestForm, HttpServletRequest request) {
        // permission check
        boolean hasAdminPermission = PermissionUtil.hasAdminPermission(request);
        if (!hasAdminPermission) {
            return "user/no_permission/notice";
        }

        return "admin/blog_requests/form";
    }

    @PostMapping("/blog-requests/add")
    public String addBlogRequest(BlogRequestForm blogRequestForm, Errors errors, HttpServletRequest request) {
        // permission check
        boolean hasAdminPermission = PermissionUtil.hasAdminPermission(request);
        if (!hasAdminPermission) {
            return "user/no_permission/notice";
        }

        // params validation
        requestFormHelper.paramsValidation(blogRequestForm, errors);

        if (errors.getErrorCount() > 0) {
            return "admin/blog_requests/form";
        }

        // submit
        BlogRequest blogRequest = new BlogRequest();
        blogRequest.setName(blogRequestForm.getName().trim());
        blogRequest.setDescription(blogRequestForm.getDescription().trim());
        blogRequest.setRssAddress(blogRequestForm.getRssAddress().trim());
        blogRequest.setAdminEmail(blogRequestForm.getAdminEmail().trim());
        blogRequest.setSelfSubmitted(false);
        blogRequestService.submit(blogRequest);

        executorService.execute(() -> blogRequestService.processNewRequest(blogRequest.getRssAddress()));

        return "redirect:/admin/blog-requests";
    }

    @GetMapping("/blog-requests/approve/{id}")
    public String approveBlogRequestById(@PathVariable("id") Long id, HttpServletRequest request) {
        // permission check
        boolean hasAdminPermission = PermissionUtil.hasAdminPermission(request);
        if (!hasAdminPermission) {
            return "user/no_permission/notice";
        }

        // approve
        blogRequestService.approveById(id);
        return "redirect:/admin/blog-requests";
    }

    @PostMapping("/blog-requests/reject/{id}")
    public String rejectBlogRequestById(@PathVariable("id") Long id, BlogRequestRejectForm blogRequestRejectForm, HttpServletRequest request) {
        // permission check
        boolean hasAdminPermission = PermissionUtil.hasAdminPermission(request);
        if (!hasAdminPermission) {
            return "user/no_permission/notice";
        }

        // reject
        if (StringUtils.isNotBlank(blogRequestRejectForm.getReason())) {
            blogRequestService.rejectById(id, blogRequestRejectForm.getReason());
        }

        return "redirect:/admin/blog-requests";
    }

    @GetMapping("/login")
    public String adminLoginForm(AdminLoginForm adminLoginForm) {
        return "admin/login/form";
    }

    @PostMapping("/login")
    public String adminLogin(AdminLoginForm adminLoginForm, Errors errors, HttpSession session) {
        // params validation
        adminLoginFormHelper.paramsValidation(adminLoginForm, errors);

        if (errors.getErrorCount() > 0) {
            return "admin/login/form";
        }

        // set session
        User user = userService.getUserByUsername(adminLoginForm.getUsername());
        session.setAttribute("user", user);

        return "redirect:/admin/blog-requests";
    }

    @GetMapping("/logout")
    public String adminLogout(HttpServletRequest request, HttpSession session) {
        // permission check
        boolean hasAdminPermission = PermissionUtil.hasAdminPermission(request);
        if (!hasAdminPermission) {
            return "user/no_permission/notice";
        }

        // set session
        session.removeAttribute("user");

        return "redirect:/admin/blog-requests";
    }

    @GetMapping("/recommended-posts")
    public String listRecommendedPosts(Model model, HttpServletRequest request) {
        return listRecommendedPosts(1, model, request);
    }

    @GetMapping("/recommended-posts/page/{page}")
    public String listRecommendedPosts(@PathVariable("page") int page, Model model, HttpServletRequest request) {
        // permission check
        boolean hasAdminPermission = PermissionUtil.hasAdminPermission(request);
        if (!hasAdminPermission) {
            return "redirect:/admin/login";
        }

        // list
        Pagination<Post> postPagination = postService.listWithKeyWord(PostSortType.recommended, "", page, CommonConstants.DEFAULT_PAGE_SIZE);
        List<PostInfo> postInfos = new ArrayList<>();
        for (Post post : postPagination.getResults()) {
            PostInfo postInfo = new PostInfo();
            BeanUtils.copyProperties(post, postInfo);

            Blog blog = blogService.getByDomainName(post.getBlogDomainName());
            postInfo.setBlogName(blog.getName());
            postInfo.setBlogAddress(blog.getAddress());
            postInfos.add(postInfo);
        }

        Pagination<PostInfo> postInfoPagination = PaginationBuilder.<PostInfo>newBuilder()
                .pageNo(page)
                .pageSize(postPagination.getPageSize())
                .total(postPagination.getTotal())
                .results(postInfos).build();

        model.addAttribute("pagination", postInfoPagination);

        // user
        User user = (User) request.getSession().getAttribute("user");
        model.addAttribute("user", user);

        return "admin/recommended_posts/list";
    }

    @GetMapping("/recommended-posts/add")
    public String recommendPostForm(RecommendPostForm recommendPostForm, HttpServletRequest request) {
        // permission check
        boolean hasAdminPermission = PermissionUtil.hasAdminPermission(request);
        if (!hasAdminPermission) {
            return "user/no_permission/notice";
        }

        return "admin/recommended_posts/form";
    }

    @PostMapping("/recommended-posts/add")
    public String recommendPostRequest(RecommendPostForm recommendPostForm, Errors errors, HttpServletRequest request) {
        // permission check
        boolean hasAdminPermission = PermissionUtil.hasAdminPermission(request);
        if (!hasAdminPermission) {
            return "user/no_permission/notice";
        }

        // params validation
        recommendPostFormHelper.paramsValidation(recommendPostForm, errors);

        if (errors.getErrorCount() > 0) {
            return "admin/recommended_posts/form";
        }

        postService.recommendByLink(recommendPostForm.getLink());

        // after
        Post post = postService.getByLink(recommendPostForm.getLink());
        Blog blog = blogService.getByDomainName(post.getBlogDomainName());
        emailService.sendPostRecommendedNotice(blog, post);

        return "redirect:/admin/recommended-posts";
    }

    @GetMapping("/recommended-posts/unpin")
    public String unpinPost(@RequestParam("link") String link, HttpServletRequest request) {
        // permission check
        boolean hasAdminPermission = PermissionUtil.hasAdminPermission(request);
        if (!hasAdminPermission) {
            return "user/no_permission/notice";
        }

        // unpin
        postService.unpinByLink(link);

        return "redirect:/admin/recommended-posts";
    }

    @GetMapping("/recommended-posts/pin")
    public String pinPost(@RequestParam("link") String link, HttpServletRequest request) {
        // permission check
        boolean hasAdminPermission = PermissionUtil.hasAdminPermission(request);
        if (!hasAdminPermission) {
            return "user/no_permission/notice";
        }

        // pin
        postService.pinByLink(link);

        // after
        Post post = postService.getByLink(link);
        Blog blog = blogService.getByDomainName(post.getBlogDomainName());
        emailService.sendPostPinnedNotice(blog, post);

        // save pin history
        PinHistory pinHistory = new PinHistory();
        pinHistory.setBlogDomainName(post.getBlogDomainName());
        pinHistory.setLink(link);
        pinHistoryService.save(pinHistory);

        return "redirect:/admin/recommended-posts";
    }

}
