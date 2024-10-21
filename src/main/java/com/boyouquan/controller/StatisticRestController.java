package com.boyouquan.controller;


import com.boyouquan.service.AccessService;
import com.boyouquan.service.BlogService;
import com.boyouquan.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/statistic")
public class StatisticRestController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private PostService postService;
    @Autowired
    private AccessService accessService;

    @GetMapping("")
    public Map<String, Long> getStatistic() {
        Map<String, Long> result = new HashMap<>();
        result.put("totalBlogs", blogService.countAll());
        result.put("totalPosts", postService.countAll());
        result.put("totalAccesses", accessService.countAll());
        return result;
    }

}
