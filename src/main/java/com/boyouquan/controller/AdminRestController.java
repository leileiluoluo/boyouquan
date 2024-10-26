package com.boyouquan.controller;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.model.AdminLoginForm;
import com.boyouquan.model.BlogRequest;
import com.boyouquan.model.BlogRequestInfo;
import com.boyouquan.service.BlogRequestService;
import com.boyouquan.service.UserService;
import com.boyouquan.util.LoginUtil;
import com.boyouquan.util.Pagination;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/admin")
public class AdminRestController {

    @Autowired
    private UserService userService;
    @Autowired
    private BlogRequestService blogRequestService;

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody AdminLoginForm adminLoginForm) {
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
            message.put("password", "密码不能为空");
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
        String sessionId = UUID.randomUUID().toString();
        LoginUtil.setSessionId(sessionId);

        result.put("status", "success");
        result.put("result", sessionId);
        return result;
    }

    @GetMapping("/logout")
    public Map<String, String> logout(HttpServletRequest request) {
        Map<String, String> result = new HashMap<>();

        // permission check
        String sessionId = request.getHeader("sessionId");
        if (StringUtils.isBlank(sessionId) || null == LoginUtil.getSessionId() || !LoginUtil.getSessionId().equals(sessionId)) {
            result.put("status", "error");
            result.put("message", "您无权限操作！");
            return result;
        }

        // remove session
        LoginUtil.removeSessionId();

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
        String sessionId = request.getHeader("sessionId");
        if (StringUtils.isBlank(sessionId) || null == LoginUtil.getSessionId() || !LoginUtil.getSessionId().equals(sessionId)) {
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

    @GetMapping("/blog-requests/{id}")
    public Map<String, Object> getBlogRequestById(@PathVariable("id") Long id, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();

        // permission check
        String sessionId = request.getHeader("sessionId");
        if (StringUtils.isBlank(sessionId) || null == LoginUtil.getSessionId() || !LoginUtil.getSessionId().equals(sessionId)) {
            result.put("status", "error");
            result.put("message", "您无权限操作！");
            return result;
        }

        // get
        BlogRequestInfo blogRequestInfo = blogRequestService.getBlogRequestInfoById(id);

        result.put("status", "success");
        result.put("result", blogRequestInfo);

        return result;
    }

}
