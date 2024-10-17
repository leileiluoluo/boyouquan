package com.boyouquan.controller;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.model.BlogRequest;
import com.boyouquan.model.BlogRequestInfo;
import com.boyouquan.service.BlogRequestService;
import com.boyouquan.util.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/blog-requests")
public class BlogRequestsRestController {

    @Autowired
    private BlogRequestService blogRequestService;

    @GetMapping("")
    public Pagination<BlogRequestInfo> listBlogRequests(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        List<BlogRequest.Status> statuses = Arrays.asList(
                BlogRequest.Status.submitted,
                BlogRequest.Status.system_check_valid,
                BlogRequest.Status.system_check_invalid,
                BlogRequest.Status.approved,
                BlogRequest.Status.rejected,
                BlogRequest.Status.uncollected
        );

        return blogRequestService.listBlogRequestInfosBySelfSubmittedAndStatuses(keyword, true, statuses, page, CommonConstants.DEFAULT_PAGE_SIZE);
    }

}
