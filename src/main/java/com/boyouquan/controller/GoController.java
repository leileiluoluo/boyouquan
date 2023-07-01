package com.boyouquan.controller;

import com.boyouquan.model.BlogAccess;
import com.boyouquan.service.BlogAccessService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
@RequestMapping("/go")
public class GoController {

    @Autowired
    private BlogAccessService blogAccessService;

    @GetMapping("")
    public void index(@RequestParam("link") String link, HttpServletRequest request, HttpServletResponse response) {
        String ip = request.getRemoteAddr();
        try {
            saveAccessInfo(ip, link);

            response.sendRedirect(link);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveAccessInfo(String ip, String link) {
        BlogAccess blogAccess = new BlogAccess();
        blogAccess.setLink(link);
        blogAccess.setIp(ip);
        blogAccessService.saveBlogAccess(blogAccess);
    }

}
