package com.boyouquan.controller;


import com.boyouquan.model.Statistics;
import com.boyouquan.service.AccessService;
import com.boyouquan.service.BlogService;
import com.boyouquan.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistics")
public class StatisticController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private PostService postService;
    @Autowired
    private AccessService accessService;

    @GetMapping("")
    public ResponseEntity<Statistics> getStatistics() {
        Statistics statistics = new Statistics();
        statistics.setTotalBlogs(blogService.countAll());
        statistics.setTotalPosts(postService.countAll());
        statistics.setTotalAccesses(accessService.countAll());
        return ResponseEntity.ok(statistics);
    }

}
