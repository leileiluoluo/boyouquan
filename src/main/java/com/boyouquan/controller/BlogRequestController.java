package com.boyouquan.controller;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.helper.BlogRequestFormHelper;
import com.boyouquan.model.BlogRequest;
import com.boyouquan.model.BlogRequestForm;
import com.boyouquan.model.BlogRequestInfo;
import com.boyouquan.service.BlogRequestService;
import com.boyouquan.util.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
@RequestMapping("/blog-requests")
public class BlogRequestController {

    @Autowired
    private BlogRequestService blogRequestService;
    @Autowired
    private BlogRequestFormHelper blogRequestFormHelper;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @GetMapping("")
    public String listBlogRequests(Model model) {
        return listBlogRequests(1, model);
    }

    @GetMapping("/page/{page}")
    public String listBlogRequests(@PathVariable("page") int page, Model model) {
        List<BlogRequest.Status> statuses = Arrays.asList(BlogRequest.Status.submitted, BlogRequest.Status.system_check_valid, BlogRequest.Status.system_check_invalid, BlogRequest.Status.approved, BlogRequest.Status.rejected);

        Pagination<BlogRequestInfo> pagination = blogRequestService.listBlogRequestInfosBySelfSubmittedAndStatuses(true, statuses, page, CommonConstants.DEFAULT_PAGE_SIZE);

        model.addAttribute("pagination", pagination);

        return "blog_requests/list";
    }

    @GetMapping("/{id}")
    public String getBlogRequestById(@PathVariable("id") Long id, Model model) {
        BlogRequestInfo blogRequestInfo = blogRequestService.getBlogRequestInfoById(id);

        model.addAttribute("blogRequestInfo", blogRequestInfo);

        return "blog_requests/item";
    }

    @GetMapping("/add")
    public String addBlogRequestForm(BlogRequestForm blogRequestForm) {
        return "blog_requests/form";
    }

    @PostMapping("/add")
    public String addBlogRequest(BlogRequestForm blogRequestForm, Errors errors, Model model) {
        // params validation
        blogRequestFormHelper.paramsValidation(blogRequestForm, errors);

        if (errors.getErrorCount() > 0) {
            return "blog_requests/form";
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

        return "redirect:/blog-requests";
    }

}

