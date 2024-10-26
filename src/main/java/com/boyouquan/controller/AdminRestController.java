package com.boyouquan.controller;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.model.*;
import com.boyouquan.service.*;
import com.boyouquan.util.LoginUtil;
import com.boyouquan.util.Pagination;
import com.boyouquan.util.PaginationBuilder;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/admin")
public class AdminRestController {

    @Autowired
    private UserService userService;
    @Autowired
    private BlogRequestService blogRequestService;
    @Autowired
    private BlogService blogService;
    @Autowired
    private PostService postService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private PinHistoryService pinHistoryService;

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody AdminLoginForm adminLoginForm) {
        Map<String, Object> result = new HashMap<>();

        // name
        if (StringUtils.isBlank(adminLoginForm.getUsername())) {
            result.put("status", "error");
            Map<String, String> message = new HashMap<>();
            message.put("username", "账号不能为空");
            result.put("message", message);
            return result;
        }
        if (StringUtils.isBlank(adminLoginForm.getPassword())) {
            result.put("status", "error");
            Map<String, String> message = new HashMap<>();
            message.put("password", "密码不能为空");
            result.put("message", message);
            return result;
        }

        // check user
        boolean isUserValid = userService.isUsernamePasswordValid(adminLoginForm.getUsername(), adminLoginForm.getPassword());
        if (!isUserValid) {
            result.put("status", "error");
            Map<String, String> message = new HashMap<>();
            message.put("username", "账号或密码无效！");
            result.put("message", message);
            return result;
        }

        // set session
        String sessionId = UUID.randomUUID().toString();
        LoginUtil.setSessionId(sessionId);

        result.put("status", "success");
        result.put("result", sessionId);
        return result;
    }

    @GetMapping("/logout")
    public Map<String, String> logout(HttpServletRequest request) {
        Map<String, String> result = new HashMap<>();

        // permission check
        String sessionId = request.getHeader("sessionId");
        if (StringUtils.isBlank(sessionId) || null == LoginUtil.getSessionId() || !LoginUtil.getSessionId().equals(sessionId)) {
            result.put("status", "error");
            result.put("message", "您无权限操作！");
            return result;
        }

        // remove session
        LoginUtil.removeSessionId();

        result.put("status", "success");
        return result;
    }

    @GetMapping("/blog-requests")
    public Map<String, Object> listBlogRequests(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();

        // permission check
        String sessionId = request.getHeader("sessionId");
        if (StringUtils.isBlank(sessionId) || null == LoginUtil.getSessionId() || !LoginUtil.getSessionId().equals(sessionId)) {
            result.put("status", "error");
            result.put("message", "您无权限操作！");
            return result;
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

        result.put("status", "success");
        result.put("result", pagination);

        return result;
    }

    @GetMapping("/blog-requests/{id}")
    public Map<String, Object> getBlogRequestById(@PathVariable("id") Long id, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();

        // permission check
        String sessionId = request.getHeader("sessionId");
        if (StringUtils.isBlank(sessionId) || null == LoginUtil.getSessionId() || !LoginUtil.getSessionId().equals(sessionId)) {
            result.put("status", "error");
            result.put("message", "您无权限操作！");
            return result;
        }

        // get
        BlogRequestInfo blogRequestInfo = blogRequestService.getBlogRequestInfoById(id);

        result.put("status", "success");
        result.put("result", blogRequestInfo);

        return result;
    }

    @PatchMapping("/blog-requests/{id}/uncollected")
    public Map<String, Object> uncollectedBlogById(@PathVariable("id") Long id, BlogUncollectedForm blogDeletedForm, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();

        // permission check
        String sessionId = request.getHeader("sessionId");
        if (StringUtils.isBlank(sessionId) || null == LoginUtil.getSessionId() || !LoginUtil.getSessionId().equals(sessionId)) {
            result.put("status", "error");
            result.put("message", "您无权限操作！");
            return result;
        }

        // get
        BlogRequest blogRequest = blogRequestService.getById(id);
        if (null != blogRequest) {
            blogRequestService.uncollectedByRssAddress(blogRequest.getRssAddress(), blogDeletedForm.getReason());
        }

        result.put("status", "success");
        return result;
    }

    @DeleteMapping("/blog-requests/{id}")
    public Map<String, Object> deleteBlogRequestById(@PathVariable("id") Long id, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();

        // permission check
        String sessionId = request.getHeader("sessionId");
        if (StringUtils.isBlank(sessionId) || null == LoginUtil.getSessionId() || !LoginUtil.getSessionId().equals(sessionId)) {
            result.put("status", "error");
            result.put("message", "您无权限操作！");
            return result;
        }

        // get
        BlogRequest blogRequest = blogRequestService.getById(id);
        if (null != blogRequest) {
            blogRequestService.deleteByRssAddress(blogRequest.getRssAddress());
        }

        result.put("status", "success");
        return result;
    }

    @PatchMapping("/blog-requests/approve/{id}")
    public Map<String, Object> approveBlogRequestById(@PathVariable("id") Long id, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();

        // permission check
        String sessionId = request.getHeader("sessionId");
        if (StringUtils.isBlank(sessionId) || null == LoginUtil.getSessionId() || !LoginUtil.getSessionId().equals(sessionId)) {
            result.put("status", "error");
            result.put("message", "您无权限操作！");
            return result;
        }

        // approve
        blogRequestService.approveById(id);

        result.put("status", "success");
        return result;
    }

    @PatchMapping("/blog-requests/reject/{id}")
    public Map<String, Object> rejectBlogRequestById(@PathVariable("id") Long id, BlogRequestRejectForm blogRequestRejectForm, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();

        // permission check
        String sessionId = request.getHeader("sessionId");
        if (StringUtils.isBlank(sessionId) || null == LoginUtil.getSessionId() || !LoginUtil.getSessionId().equals(sessionId)) {
            result.put("status", "error");
            result.put("message", "您无权限操作！");
            return result;
        }

        // reject
        if (StringUtils.isNotBlank(blogRequestRejectForm.getReason())) {
            blogRequestService.rejectById(id, blogRequestRejectForm.getReason());
        }

        result.put("status", "success");
        return result;
    }

    @GetMapping("/recommended-posts")
    public Map<String, Object> listRecommendedPosts(@RequestParam("page") int page, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();

        // permission check
        String sessionId = request.getHeader("sessionId");
        if (StringUtils.isBlank(sessionId) || null == LoginUtil.getSessionId() || !LoginUtil.getSessionId().equals(sessionId)) {
            result.put("status", "error");
            result.put("message", "您无权限操作！");
            return result;
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

        result.put("status", "success");
        result.put("result", postInfoPagination);
        return result;
    }

    @PostMapping("/recommended-posts/add")
    public Map<String, Object> recommendPostRequest(RecommendPostForm recommendPostForm, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();

        // permission check
        String sessionId = request.getHeader("sessionId");
        if (StringUtils.isBlank(sessionId) || null == LoginUtil.getSessionId() || !LoginUtil.getSessionId().equals(sessionId)) {
            result.put("status", "error");
            result.put("message", "您无权限操作！");
            return result;
        }

        postService.recommendByLink(recommendPostForm.getLink());

        // after
        Post post = postService.getByLink(recommendPostForm.getLink());
        Blog blog = blogService.getByDomainName(post.getBlogDomainName());
        emailService.sendPostRecommendedNotice(blog, post);

        result.put("status", "success");
        return result;
    }

    @GetMapping("/recommended-posts/unpin")
    public Map<String, Object> unpinPost(@RequestParam("link") String link, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();

        // permission check
        String sessionId = request.getHeader("sessionId");
        if (StringUtils.isBlank(sessionId) || null == LoginUtil.getSessionId() || !LoginUtil.getSessionId().equals(sessionId)) {
            result.put("status", "error");
            result.put("message", "您无权限操作！");
            return result;
        }

        // unpin
        postService.unpinByLink(link);

        result.put("status", "success");
        return result;
    }

    @GetMapping("/recommended-posts/pin")
    public Map<String, Object> pinPost(@RequestParam("link") String link, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();

        // permission check
        String sessionId = request.getHeader("sessionId");
        if (StringUtils.isBlank(sessionId) || null == LoginUtil.getSessionId() || !LoginUtil.getSessionId().equals(sessionId)) {
            result.put("status", "error");
            result.put("message", "您无权限操作！");
            return result;
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

        result.put("status", "success");
        return result;
    }

}
