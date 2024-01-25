package com.boyouquan.controller;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.model.*;
import com.boyouquan.service.BlogService;
import com.boyouquan.service.MonthlySelectedService;
import com.boyouquan.service.PostService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/monthly-selected")
public class MonthlySelectedController {

    @Autowired
    private MonthlySelectedService monthlySelectedService;
    @Autowired
    private PostService postService;
    @Autowired
    private BlogService blogService;

    @GetMapping("")
    public String monthlySelected(Model model) {
        List<String> yearMonthStrs = monthlySelectedService.listYearMonthStrs();

        List<MonthlySelectedPost> monthlyPostInfosList = new ArrayList<>();

        yearMonthStrs.stream().forEach(yearMonthStr -> {
            List<SelectedPostAccess> selectedPostAccessList = monthlySelectedService.listSelectedPostsByYearMonthStr(yearMonthStr, CommonConstants.MONTHLY_SELECTED_POSTS_LIMIT);

            List<PostInfo> postInfos = selectedPostAccessList.stream()
                    .map(selectedPostAccess -> {
                        Post post = postService.getByLink(selectedPostAccess.getPostLink());

                        PostInfo postInfo = new PostInfo();
                        BeanUtils.copyProperties(post, postInfo);

                        Blog blog = blogService.getByDomainName(post.getBlogDomainName());
                        postInfo.setBlogName(blog.getName());
                        postInfo.setBlogAddress(blog.getAddress());

                        return postInfo;
                    }).toList();

            MonthlySelectedPost monthPostInfos = new MonthlySelectedPost();
            monthPostInfos.setYearMonthStr(yearMonthStr);
            monthPostInfos.setPostInfos(postInfos);

            monthlyPostInfosList.add(monthPostInfos);
        });

        model.addAttribute("monthlyPostInfosList", monthlyPostInfosList);

        return "monthly_selected/list";
    }

}
