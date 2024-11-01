package com.boyouquan.controller;

import com.boyouquan.model.LatestNews;
import com.boyouquan.service.LatestNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/latest-news")
public class LatestNewsController {

    @Autowired
    private LatestNewsService latestNewsService;

    @GetMapping("")
    public ResponseEntity<List<LatestNews>> latestNews() {
        List<LatestNews> latestNews = latestNewsService.getLatestNews();
        return ResponseEntity.ok(latestNews);
    }

}
