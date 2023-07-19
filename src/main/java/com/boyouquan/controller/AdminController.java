package com.boyouquan.controller;

import com.boyouquan.helper.BlogRequestFormHelper;
import com.boyouquan.model.BlogRequest;
import com.boyouquan.model.BlogRequestForm;
import com.boyouquan.model.BlogRequestInfo;
import com.boyouquan.service.BlogRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

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

    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    @GetMapping("/blog-requests")
    public String blogRequests(Model model) {
        List<BlogRequestInfo> blogRequests = blogRequestService.listBlogRequestInfosByStatuses(
                Arrays.asList(BlogRequest.Status.submitted,
                        BlogRequest.Status.system_check_valid,
                        BlogRequest.Status.system_check_invalid,
                        BlogRequest.Status.approved,
                        BlogRequest.Status.rejected));

        model.addAttribute("blogRequestInfos", blogRequests);

        return "admin/blog_requests/list";
    }

    @GetMapping("/blog-requests/{id}")
    public String getBlogRequestById(@PathVariable("id") Long id, Model model) {
        BlogRequestInfo blogRequestInfo = blogRequestService.getBlogRequestInfoById(id);

        model.addAttribute("blogRequestInfo", blogRequestInfo);

        return "admin/blog_requests/item";
    }

    @GetMapping("/blog-requests/add")
    public String addBlogRequestForm(BlogRequestForm blogRequestForm) {
        return "admin/blog_requests/form";
    }

    @PostMapping("/blog-requests/add")
    public String addBlogRequest(BlogRequestForm blogRequestForm, Errors errors, Model model) {
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

    @GetMapping("/blog-requests/approve")
    public String addBlogRequestForm(@RequestParam("rssAddress") String rssAddress) {
        blogRequestService.approve(rssAddress);
        return "redirect:/admin/blog-requests";
    }

}
