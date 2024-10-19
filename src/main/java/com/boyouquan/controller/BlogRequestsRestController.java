package com.boyouquan.controller;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.helper.BlogRequestFormHelper;
import com.boyouquan.model.BlogRequest;
import com.boyouquan.model.BlogRequestForm;
import com.boyouquan.model.BlogRequestInfo;
import com.boyouquan.service.BlogRequestService;
import com.boyouquan.util.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api/blog-requests")
public class BlogRequestsRestController {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Autowired
    private BlogRequestFormHelper blogRequestFormHelper;
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

    @GetMapping("/{id}")
    public BlogRequestInfo getBlogRequestById(@PathVariable("id") Long id) {
        return blogRequestService.getBlogRequestInfoById(id);
    }

    @PostMapping("")
    public Map<String, Object> addBlogRequest(@RequestBody BlogRequestForm blogRequestForm) {
        Map<String, Object> result = new HashMap<>();
        Map<String, String> error = blogRequestFormHelper.paramsValidation(blogRequestForm);
        if (null != error) {
            result.put("status", "error");
            result.put("message", error);
            return result;
        }

        // params validation
        if (null != blogRequestService.getByRssAddress(blogRequestForm.getRssAddress())) {
            result.put("status", "error");
            result.put("message", "您要提交的博客已存在！");
            return result;
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

        result.put("status", "success");
        return result;
    }

}
