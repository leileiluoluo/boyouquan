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

    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    @GetMapping("")
    public String listBlogRequests(Model model) {
        List<BlogRequestInfo> blogRequestInfos = blogRequestService.listBlogRequestInfosByStatuses(
                Arrays.asList(BlogRequest.Status.submitted,
                        BlogRequest.Status.system_check_valid,
                        BlogRequest.Status.system_check_invalid,
                        BlogRequest.Status.approved,
                        BlogRequest.Status.rejected));

        model.addAttribute("blogRequestInfos", blogRequestInfos);

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
            errors.rejectValue("rssAddress", "fields.invalid", "您提交的博客已存在！");
        }

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
