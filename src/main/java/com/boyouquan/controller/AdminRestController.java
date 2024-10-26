package com.boyouquan.controller;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.model.AdminLoginForm;
import com.boyouquan.model.BlogRequest;
import com.boyouquan.model.BlogRequestInfo;
import com.boyouquan.model.User;
import com.boyouquan.service.BlogRequestService;
import com.boyouquan.service.UserService;
import com.boyouquan.util.Pagination;
import com.boyouquan.util.PermissionUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminRestController {

    @Autowired
    private UserService userService;
    @Autowired
    private BlogRequestService blogRequestService;

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody AdminLoginForm adminLoginForm, HttpSession session) {
        Map<String, Object> result = new HashMap<>();

        // name
        if (StringUtils.isBlank(adminLoginForm.getUsername())) {
            result.put("status", "error");
            Map<String, String> message = new HashMap<>();
            message.put("username", "账号不能为空");
            result.put("message", message);
            return result;
        }
        if (StringUtils.isBlank(adminLoginForm.getPassword())) {
            result.put("status", "error");
            Map<String, String> message = new HashMap<>();
            message.put("username", "密码不能为空");
            result.put("message", message);
            return result;
        }

        // check user
        boolean isUserValid = userService.isUsernamePasswordValid(adminLoginForm.getUsername(), adminLoginForm.getPassword());
        if (!isUserValid) {
            result.put("status", "error");
            Map<String, String> message = new HashMap<>();
            message.put("username", "账号或密码无效！");
            result.put("message", message);
            return result;
        }

        // set session
        User user = userService.getUserByUsername(adminLoginForm.getUsername());
        session.setAttribute("user", user);

        result.put("status", "success");
        result.put("result", user);
        return result;
    }

    @GetMapping("/logout")
    public Map<String, String> logout(HttpServletRequest request, HttpSession session) {
        Map<String, String> result = new HashMap<>();

        // permission check
        boolean hasAdminPermission = PermissionUtil.hasAdminPermission(request);
        if (!hasAdminPermission) {
            result.put("status", "error");
            result.put("message", "您无权限操作！");
            return result;
        }

        // remove session
        session.removeAttribute("user");

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
        boolean hasAdminPermission = PermissionUtil.hasAdminPermission(request);
        if (!hasAdminPermission) {
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

}
