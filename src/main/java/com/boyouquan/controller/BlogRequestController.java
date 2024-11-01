package com.boyouquan.controller;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.enumration.ErrorCode;
import com.boyouquan.helper.BlogRequestFormHelper;
import com.boyouquan.model.BlogRequest;
import com.boyouquan.model.BlogRequestForm;
import com.boyouquan.model.BlogRequestInfo;
import com.boyouquan.service.BlogRequestService;
import com.boyouquan.util.Pagination;
import com.boyouquan.util.PaginationBuilder;
import com.boyouquan.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api/blog-requests")
public class BlogRequestController {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Autowired
    private BlogRequestFormHelper blogRequestFormHelper;
    @Autowired
    private BlogRequestService blogRequestService;

    @GetMapping("")
    public ResponseEntity<Pagination<BlogRequestInfo>> listBlogRequests(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "onlySelfSubmitted", required = false, defaultValue = "true") boolean onlySelfSubmitted) {
        List<BlogRequest.Status> statuses = Arrays.asList(
                BlogRequest.Status.submitted,
                BlogRequest.Status.system_check_valid,
                BlogRequest.Status.system_check_invalid,
                BlogRequest.Status.approved,
                BlogRequest.Status.rejected,
                BlogRequest.Status.uncollected
        );

        Pagination<BlogRequestInfo> blogRequestInfo = PaginationBuilder.buildEmptyResults();
        if (onlySelfSubmitted) {
            blogRequestInfo = blogRequestService.listBlogRequestInfosBySelfSubmittedAndStatuses(keyword, true, statuses, page, CommonConstants.DEFAULT_PAGE_SIZE);
        } else {
            blogRequestInfo = blogRequestService.listBlogRequestInfosByStatuses(
                    keyword, statuses, page, CommonConstants.DEFAULT_PAGE_SIZE);
        }

        return ResponseEntity.ok(blogRequestInfo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogRequestInfo> getBlogRequestById(@PathVariable("id") Long id) {
        BlogRequestInfo blogRequestInfo = blogRequestService.getBlogRequestInfoById(id);

        return ResponseEntity.ok(blogRequestInfo);
    }

    @PostMapping("")
    public ResponseEntity<?> addBlogRequest(@RequestBody BlogRequestForm blogRequestForm) {
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
        blogRequest.setSelfSubmitted(true);
        blogRequestService.submit(blogRequest);

        executorService.execute(() -> blogRequestService.processNewRequest(blogRequest.getRssAddress()));

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
