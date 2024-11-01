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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
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
        LoginUtil.setSessionId(adminLoginForm.getUsername(), sessionId);

        // return
        LoginSuccess loginSuccess = new LoginSuccess(adminLoginForm.getUsername(), sessionId);
        return ResponseEntity.ok(loginSuccess);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String username = request.getHeader("username");

        // permission check
        if (!PermissionUtil.hasAdminPermission(request)) {
            return ResponseUtil.errorResponse(ErrorCode.UNAUTHORIZED);
        }

        // remove session
        LoginUtil.removeSessionId(username);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/blog-requests")
    public ResponseEntity<?> listBlogRequests(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            HttpServletRequest request) {
        // permission check
        if (!PermissionUtil.hasAdminPermission(request)) {
            return ResponseUtil.errorResponse(ErrorCode.UNAUTHORIZED);
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

        return ResponseEntity.ok(pagination);
    }

    @GetMapping("/blog-requests/{id}")
    public ResponseEntity<?> getBlogRequestById(@PathVariable("id") Long id, HttpServletRequest request) {
        // permission check
        if (!PermissionUtil.hasAdminPermission(request)) {
            return ResponseUtil.errorResponse(ErrorCode.UNAUTHORIZED);
        }

        // get
        BlogRequestInfo blogRequestInfo = blogRequestService.getBlogRequestInfoById(id);

        return ResponseEntity.ok(blogRequestInfo);
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
    public ResponseEntity<?> uncollectedBlogById(@PathVariable("id") Long id, @RequestBody BlogUncollectedForm blogDeletedForm, HttpServletRequest request) {
        // permission check
        if (!PermissionUtil.hasAdminPermission(request)) {
            return ResponseUtil.errorResponse(ErrorCode.UNAUTHORIZED);
        }

        // get
        BlogRequest blogRequest = blogRequestService.getById(id);
        if (null != blogRequest) {
            blogRequestService.uncollectedByRssAddress(blogRequest.getRssAddress(), blogDeletedForm.getReason());
        }

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/blog-requests/{id}")
    public ResponseEntity<?> deleteBlogRequestById(@PathVariable("id") Long id, HttpServletRequest request) {
        // permission check
        if (!PermissionUtil.hasAdminPermission(request)) {
            return ResponseUtil.errorResponse(ErrorCode.UNAUTHORIZED);
        }

        // get
        BlogRequest blogRequest = blogRequestService.getById(id);
        if (null != blogRequest) {
            blogRequestService.deleteByRssAddress(blogRequest.getRssAddress());
        }

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/blog-requests/approve/{id}")
    public ResponseEntity<?> approveBlogRequestById(@PathVariable("id") Long id, HttpServletRequest request) {
        // permission check
        if (!PermissionUtil.hasAdminPermission(request)) {
            return ResponseUtil.errorResponse(ErrorCode.UNAUTHORIZED);
        }

        // approve
        blogRequestService.approveById(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/blog-requests/reject/{id}")
    public ResponseEntity<?> rejectBlogRequestById(@PathVariable("id") Long id, @RequestBody BlogRequestRejectForm blogRequestRejectForm, HttpServletRequest request) {
        // permission check
        if (!PermissionUtil.hasAdminPermission(request)) {
            return ResponseUtil.errorResponse(ErrorCode.UNAUTHORIZED);
        }

        // reject
        if (StringUtils.isNotBlank(blogRequestRejectForm.getReason())) {
            blogRequestService.rejectById(id, blogRequestRejectForm.getReason());
        }

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/recommended-posts")
    public ResponseEntity<?> listRecommendedPosts(@RequestParam("page") int page, HttpServletRequest request) {
        // permission check
        if (!PermissionUtil.hasAdminPermission(request)) {
            return ResponseUtil.errorResponse(ErrorCode.UNAUTHORIZED);
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

        return ResponseEntity.ok(postInfoPagination);
    }

    @PostMapping("/recommended-posts")
    public ResponseEntity<?> recommendPostRequest(@RequestBody RecommendPostForm recommendPostForm, HttpServletRequest request) {
        // permission check
        if (!PermissionUtil.hasAdminPermission(request)) {
            return ResponseUtil.errorResponse(ErrorCode.UNAUTHORIZED);
        }

        Post post = postService.getByLink(recommendPostForm.getLink());
        if (null == post) {
            return ResponseUtil.errorResponse(ErrorCode.POST_NOT_EXISTS);
        }

        postService.recommendByLink(recommendPostForm.getLink());

        // after
        Blog blog = blogService.getByDomainName(post.getBlogDomainName());
        emailService.sendPostRecommendedNotice(blog, post);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/recommended-posts/unpin")
    public ResponseEntity<?> unpinPost(@RequestBody RecommendPostForm recommendPostForm, HttpServletRequest request) {
        // permission check
        if (!PermissionUtil.hasAdminPermission(request)) {
            return ResponseUtil.errorResponse(ErrorCode.UNAUTHORIZED);
        }

        String link = recommendPostForm.getLink();

        if (!postService.existsByLink(link)) {
            return ResponseUtil.errorResponse(ErrorCode.POST_NOT_EXISTS);
        }

        // unpin
        postService.unpinByLink(link);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/recommended-posts/pin")
    public ResponseEntity<?> pinPost(@RequestBody RecommendPostForm recommendPostForm, HttpServletRequest request) {
        // permission check
        if (!PermissionUtil.hasAdminPermission(request)) {
            return ResponseUtil.errorResponse(ErrorCode.UNAUTHORIZED);
        }

        String link = recommendPostForm.getLink();

        if (!postService.existsByLink(link)) {
            return ResponseUtil.errorResponse(ErrorCode.POST_NOT_EXISTS);
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

        return ResponseEntity.noContent().build();
    }

}
