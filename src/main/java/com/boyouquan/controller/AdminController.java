package com.boyouquan.controller;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.helper.BlogRequestFormHelper;
import com.boyouquan.model.*;
import com.boyouquan.service.BlogRequestService;
import com.boyouquan.service.UserService;
import com.boyouquan.util.Pagination;
import com.boyouquan.util.PermissionUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
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
    @Autowired
    private UserService userService;

    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    @GetMapping("/blog-requests")
    public String listBlogRequests(Model model, HttpServletRequest request) {
        return listBlogRequests(1, model, request);
    }

    @GetMapping("/blog-requests/page/{page}")
    public String listBlogRequests(@PathVariable("page") int page, Model model, HttpServletRequest request) {
        // permission check
        boolean hasAdminPermission = PermissionUtil.hasAdminPermission(request);
        if (!hasAdminPermission) {
            return "redirect:/admin/login";
        }

        // list
        List<BlogRequest.Status> statuses = Arrays.asList(BlogRequest.Status.submitted,
                BlogRequest.Status.system_check_valid,
                BlogRequest.Status.system_check_invalid,
                BlogRequest.Status.approved,
                BlogRequest.Status.rejected);

        Pagination<BlogRequestInfo> pagination = blogRequestService.listBlogRequestInfosByStatuses(
                statuses, page, CommonConstants.DEFAULT_PAGE_SIZE);

        model.addAttribute("pagination", pagination);

        // user
        User user = (User) request.getSession().getAttribute("user");
        model.addAttribute("user", user);

        return "admin/blog_requests/list";
    }

    @GetMapping("/blog-requests/{id}")
    public String getBlogRequestById(@PathVariable("id") Long id, Model model, HttpServletRequest request) {
        // permission check
        boolean hasAdminPermission = PermissionUtil.hasAdminPermission(request);
        if (!hasAdminPermission) {
            return "user/no_permission/notice";
        }

        // get
        BlogRequestInfo blogRequestInfo = blogRequestService.getBlogRequestInfoById(id);

        model.addAttribute("blogRequestInfo", blogRequestInfo);

        return "admin/blog_requests/item";
    }

    @DeleteMapping("/blog-requests/{id}")
    public String deleteBlogRequestById(@PathVariable("id") Long id, HttpServletRequest request) {
        // permission check
        boolean hasAdminPermission = PermissionUtil.hasAdminPermission(request);
        if (!hasAdminPermission) {
            return "user/no_permission/notice";
        }

        // get
        BlogRequest blogRequest = blogRequestService.getById(id);
        if (null != blogRequest) {
            blogRequestService.deleteByRssAddress(blogRequest.getRssAddress());
        }

        return "redirect:/admin/blog-requests";
    }

    @GetMapping("/blog-requests/add")
    public String addBlogRequestForm(BlogRequestForm blogRequestForm, HttpServletRequest request) {
        // permission check
        boolean hasAdminPermission = PermissionUtil.hasAdminPermission(request);
        if (!hasAdminPermission) {
            return "user/no_permission/notice";
        }

        return "admin/blog_requests/form";
    }

    @PostMapping("/blog-requests/add")
    public String addBlogRequest(BlogRequestForm blogRequestForm, Errors errors, HttpServletRequest request) {
        // permission check
        boolean hasAdminPermission = PermissionUtil.hasAdminPermission(request);
        if (!hasAdminPermission) {
            return "user/no_permission/notice";
        }

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

    @GetMapping("/blog-requests/approve/{id}")
    public String approveBlogRequestById(@PathVariable("id") Long id, HttpServletRequest request) {
        // permission check
        boolean hasAdminPermission = PermissionUtil.hasAdminPermission(request);
        if (!hasAdminPermission) {
            return "user/no_permission/notice";
        }

        // approve
        blogRequestService.approveById(id);
        return "redirect:/admin/blog-requests";
    }

    @PostMapping("/blog-requests/reject/{id}")
    public String rejectBlogRequestById(@PathVariable("id") Long id, BlogRequestRejectForm blogRequestRejectForm, HttpServletRequest request) {
        // permission check
        boolean hasAdminPermission = PermissionUtil.hasAdminPermission(request);
        if (!hasAdminPermission) {
            return "user/no_permission/notice";
        }

        // reject
        if (StringUtils.isNotBlank(blogRequestRejectForm.getReason())) {
            blogRequestService.rejectById(id, blogRequestRejectForm.getReason());
        }

        return "redirect:/admin/blog-requests";
    }

    @GetMapping("/login")
    public String adminLoginForm(AdminLoginForm adminLoginForm) {
        return "admin/login/form";
    }

    @PostMapping("/login")
    public String adminLogin(AdminLoginForm adminLoginForm, Errors errors, HttpSession session) {
        // name
        if (StringUtils.isBlank(adminLoginForm.getUsername())) {
            errors.rejectValue("username", "fields.invalid", "账号不能为空");
        }
        if (StringUtils.isBlank(adminLoginForm.getPassword())) {
            errors.rejectValue("password", "fields.invalid", "密码不能为空");
        }

        // check user
        User user = userService.getUserByUsername(adminLoginForm.getUsername());
        boolean isUserValid = userService.isUsernamePasswordValid(adminLoginForm.getUsername(), adminLoginForm.getPassword());
        if (!isUserValid) {
            errors.rejectValue("username", "fields.invalid", "账号或密码无效！");
            errors.rejectValue("password", "fields.invalid", "账号或密码无效！");
        }

        if (errors.getErrorCount() > 0) {
            return "admin/login/form";
        }

        // set session
        session.setAttribute("user", user);

        return "redirect:/admin/blog-requests";
    }

    @GetMapping("/logout")
    public String adminLogout(HttpServletRequest request, HttpSession session) {
        // permission check
        boolean hasAdminPermission = PermissionUtil.hasAdminPermission(request);
        if (!hasAdminPermission) {
            return "user/no_permission/notice";
        }

        // set session
        session.removeAttribute("user");

        return "redirect:/admin/blog-requests";
    }

}
