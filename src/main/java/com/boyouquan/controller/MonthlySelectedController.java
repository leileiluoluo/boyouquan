package com.boyouquan.controller;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.model.Blog;
import com.boyouquan.model.MonthlySelectedPost;
import com.boyouquan.model.Post;
import com.boyouquan.model.SelectedPostAccess;
import com.boyouquan.service.BlogService;
import com.boyouquan.service.BlogStatusService;
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
    @Autowired
    private BlogStatusService blogStatusService;

    @GetMapping("")
    public String monthlySelected(Model model) {
        List<String> yearMonthStrs = monthlySelectedService.listYearMonthStrs();

        List<MonthlySelectedPost> monthlySelectedPosts = new ArrayList<>();

        yearMonthStrs.forEach(yearMonthStr -> {
            List<SelectedPostAccess> selectedPostAccessList = monthlySelectedService.listSelectedPostsByYearMonthStr(yearMonthStr, CommonConstants.MONTHLY_SELECTED_POSTS_LIMIT);

            List<MonthlySelectedPost.PostInfoWithBlogStatus> postInfos = selectedPostAccessList.stream()
                    .map(selectedPostAccess -> {
                        Post post = postService.getByLink(selectedPostAccess.getPostLink());

                        MonthlySelectedPost.PostInfoWithBlogStatus postInfo = new MonthlySelectedPost.PostInfoWithBlogStatus();
                        BeanUtils.copyProperties(post, postInfo);

                        // blog
                        Blog blog = blogService.getByDomainName(post.getBlogDomainName());
                        postInfo.setBlogName(blog.getName());
                        postInfo.setBlogAddress(blog.getAddress());

                        // blog status
                        boolean blogStatusOk = blogStatusService.isStatusOkByBlogDomainName(selectedPostAccess.getBlogDomainName());
                        postInfo.setBlogStatusOk(blogStatusOk);

                        return postInfo;
                    }).toList();

            MonthlySelectedPost monthlySelectedPost = new MonthlySelectedPost();
            monthlySelectedPost.setYearMonthStr(yearMonthStr);
            monthlySelectedPost.setPostInfos(postInfos);

            monthlySelectedPosts.add(monthlySelectedPost);
        });

        model.addAttribute("monthlySelectedPosts", monthlySelectedPosts);

        return "monthly_selected/list";
    }

}
