package com.boyouquan.controller;

import com.boyouquan.model.PostSortType;
import com.boyouquan.service.FeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FeedController {

    @Autowired
    private FeedService feedService;

    @GetMapping(value = "/feed.xml", produces = MediaType.TEXT_XML_VALUE)
    @ResponseBody
    public String feed(@RequestParam(value = "sort", required = false, defaultValue = "recommended") String sort) {
        PostSortType sortType = PostSortType.of(sort);
        return feedService.generateFeedXML(sortType);
    }

}
