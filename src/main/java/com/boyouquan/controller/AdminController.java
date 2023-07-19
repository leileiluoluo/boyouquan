package com.boyouquan.controller;

import com.boyouquan.model.BlogRequest;
import com.boyouquan.model.BlogRequestForm;
import com.boyouquan.model.BlogRequestInfo;
import com.boyouquan.service.BlogRequestService;
import com.boyouquan.util.EmailUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private BlogRequestService blogRequestService;

    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    @GetMapping("/blog-requests")
    public String blogRequests(Model model) {
        List<BlogRequestInfo> blogRequests = blogRequestService.listBlogRequestInfosByStatuses(
                Arrays.asList(BlogRequest.Status.submitted,
                        BlogRequest.Status.system_check_valid,
                        BlogRequest.Status.system_check_invalid,
                        BlogRequest.Status.approved,
                        BlogRequest.Status.rejected));

        model.addAttribute("blogRequests", blogRequests);

        return "admin/blog_requests/list";
    }

    @GetMapping("/blog-requests/add")
    public String addBlogRequestForm(BlogRequestForm blogRequestForm) {
        return "admin/blog_requests/add-form";
    }

    @PostMapping("/blog-requests/add")
    public String addBlogRequest(BlogRequestForm blogRequestForm, Errors errors, Model model) {
        // name
        if (StringUtils.isBlank(blogRequestForm.getName())) {
            errors.rejectValue("name", "fields.invalid", "博客名称不能为空");
        } else if (blogRequestForm.getName().length() > 20) {
            errors.rejectValue("name", "fields.invalid", "博客名称不能大于20个字");
        }

        // description
        if (StringUtils.isBlank(blogRequestForm.getDescription())) {
            errors.rejectValue("description", "fields.invalid", "博客描述不能为空");
        } else if (blogRequestForm.getDescription().length() < 10) {
            errors.rejectValue("description", "fields.invalid", "博客描述不能少于10个字");
        }

        // rss address
        if (StringUtils.isBlank(blogRequestForm.getRssAddress())) {
            errors.rejectValue("rssAddress", "fields.invalid", "RSS地址不能为空");
        } else if (!blogRequestForm.getRssAddress().startsWith("http")) {
            errors.rejectValue("rssAddress", "fields.invalid", "RSS地址不正确");
        }

        // email
        if (StringUtils.isBlank(blogRequestForm.getAdminEmail())) {
            errors.rejectValue("adminEmail", "fields.invalid", "博主邮箱不能为空");
        } else if (!EmailUtil.isEmailValid(blogRequestForm.getAdminEmail())) {
            errors.rejectValue("adminEmail", "fields.invalid", "邮箱格式不正确");
        }


        // exists?
        if (null != blogRequestService.getByRssAddress(blogRequestForm.getRssAddress())) {
            errors.rejectValue("rssAddress", "fields.invalid", "您要提交的博客已存在！");
        }

        if (errors.getErrorCount() > 0) {
            return "admin/blog_requests/add-form";
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
