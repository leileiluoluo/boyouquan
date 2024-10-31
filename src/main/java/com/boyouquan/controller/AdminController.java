package com.boyouquan.controller;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.enumration.ErrorCode;
import com.boyouquan.helper.BlogRequestFormHelper;
import com.boyouquan.model.*;
import com.boyouquan.service.*;
import com.boyouquan.util.*;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

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
    @Autowired
    private BlogRequestFormHelper blogRequestFormHelper;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AdminLoginForm adminLoginForm) {
        // validation
        if (StringUtils.isBlank(adminLoginForm.getUsername())) {
            return ResponseUtil.errorResponse(ErrorCode.LOGIN_USERNAME_INVALID);
        }
        if (StringUtils.isBlank(adminLoginForm.getPassword())) {
            return ResponseUtil.errorResponse(ErrorCode.LOGIN_PASSWORD_INVALID);
        }

        boolean isUserValid = userService.isUsernamePasswordValid(adminLoginForm.getUsername(), adminLoginForm.getPassword());
        if (!isUserValid) {
            return ResponseUtil.errorResponse(ErrorCode.LOGIN_USERNAME_PASSWORD_INVALID);
        }

        // set session
        String sessionId = UUID.randomUUID().toString();
        LoginUtil.setSessionId(sessionId);

        // return
        LoginSuccess loginSuccess = new LoginSuccess(sessionId);
        return ResponseEntity.ok(loginSuccess);
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

    @PostMapping("/blog-requests")
    public ResponseEntity<?> addBlogRequest(@RequestBody BlogRequestForm blogRequestForm, HttpServletRequest request) {
        // permission check
        if (!PermissionUtil.hasAdminPermission(request)) {
            return ResponseUtil.errorResponse(ErrorCode.UNAUTHORIZED);
        }

        ErrorCode error = blogRequestFormHelper.paramsValidation(blogRequestForm);
        if (null != error) {
            return ResponseUtil.errorResponse(error);
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

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/blog-requests/{id}/uncollected")
    public Map<String, Object> uncollectedBlogById(@PathVariable("id") Long id, @RequestBody BlogUncollectedForm blogDeletedForm, HttpServletRequest request) {
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
    public Map<String, Object> rejectBlogRequestById(@PathVariable("id") Long id, @RequestBody BlogRequestRejectForm blogRequestRejectForm, HttpServletRequest request) {
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
    public Map<String, Object> recommendPostRequest(@RequestBody RecommendPostForm recommendPostForm, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();

        // permission check
        String sessionId = request.getHeader("sessionId");
        if (StringUtils.isBlank(sessionId) || null == LoginUtil.getSessionId() || !LoginUtil.getSessionId().equals(sessionId)) {
            result.put("status", "error");
            result.put("message", "您无权限操作！");
            return result;
        }

        Post post = postService.getByLink(recommendPostForm.getLink());
        if (null == post) {
            result.put("status", "error");
            result.put("message", "文章链接不存在！");
            return result;
        }

        postService.recommendByLink(recommendPostForm.getLink());

        // after
        Blog blog = blogService.getByDomainName(post.getBlogDomainName());
        emailService.sendPostRecommendedNotice(blog, post);

        result.put("status", "success");
        return result;
    }

    @PatchMapping("/recommended-posts/unpin")
    public Map<String, Object> unpinPost(@RequestBody RecommendPostForm recommendPostForm, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();

        // permission check
        String sessionId = request.getHeader("sessionId");
        if (StringUtils.isBlank(sessionId) || null == LoginUtil.getSessionId() || !LoginUtil.getSessionId().equals(sessionId)) {
            result.put("status", "error");
            result.put("message", "您无权限操作！");
            return result;
        }

        String link = recommendPostForm.getLink();

        if (!postService.existsByLink(link)) {
            result.put("status", "error");
            result.put("message", "文章链接不存在！");
            return result;
        }

        // unpin
        postService.unpinByLink(link);

        result.put("status", "success");
        return result;
    }

    @PatchMapping("/recommended-posts/pin")
    public Map<String, Object> pinPost(@RequestBody RecommendPostForm recommendPostForm, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();

        // permission check
        String sessionId = request.getHeader("sessionId");
        if (StringUtils.isBlank(sessionId) || null == LoginUtil.getSessionId() || !LoginUtil.getSessionId().equals(sessionId)) {
            result.put("status", "error");
            result.put("message", "您无权限操作！");
            return result;
        }

        String link = recommendPostForm.getLink();

        if (!postService.existsByLink(link)) {
            result.put("status", "error");
            result.put("message", "文章链接不存在！");
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
