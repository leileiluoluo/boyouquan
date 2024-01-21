package com.boyouquan.controller;

import com.boyouquan.model.Blog;
import com.boyouquan.model.BlogInfo;
import com.boyouquan.model.Post;
import com.boyouquan.model.PostInfo;
import com.boyouquan.service.AccessService;
import com.boyouquan.service.BlogService;
import com.boyouquan.service.BlogStatusService;
import com.boyouquan.service.PostService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/sharing")
public class PostSharingController {

    @Autowired
    private PostService postService;
    @Autowired
    private BlogService blogService;
    @Autowired
    private AccessService accessService;
    @Autowired
    private BlogStatusService blogStatusService;

    @GetMapping("")
    public String sharePost(@RequestParam("link") String link, Model model) {
        Post post = postService.getByLink(link);
        if (null == post) {
            return "error/404";
        }

        // post info
        PostInfo postInfo = new PostInfo();
        BeanUtils.copyProperties(post, postInfo);

        Blog blog = blogService.getByDomainName(post.getBlogDomainName());
        postInfo.setBlogName(blog.getName());
        postInfo.setBlogAddress(blog.getAddress());
        String blogAdminMediumImageURL = blogService.getBlogAdminMediumImageURLByDomainName(blog.getDomainName());
        postInfo.setBlogAdminMediumImageURL(blogAdminMediumImageURL);

        Long linkAccessCount = accessService.countByLink(post.getLink());
        postInfo.setLinkAccessCount(linkAccessCount);

        model.addAttribute("postInfo", postInfo);

        // blog info
        BlogInfo blogInfo = blogService.getBlogInfoByDomainName(post.getBlogDomainName());

        // blog status
        boolean isStatusOk = blogStatusService.isStatusOkByBlogDomainName(blog.getDomainName());
        blogInfo.setStatusOk(isStatusOk);
        boolean sunset = blogStatusService.isBlogSunset(blog.getDomainName());
        blogInfo.setSunset(sunset);

        // blog info
        model.addAttribute("blogInfo", blogInfo);

        // summary
        model.addAttribute("totalBlogs", blogService.countAll());
        model.addAttribute("totalBlogPosts", postService.countAll());
        model.addAttribute("accessTotal", accessService.countAll());

        return "post/sharing";
    }

}
