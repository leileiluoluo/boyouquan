package com.boyouquan.service.impl;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.dao.MonthlySelectedDaoMapper;
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
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class MonthlySelectedServiceImpl implements MonthlySelectedService {

    @Autowired
    private MonthlySelectedDaoMapper monthlySelectedDaoMapper;
    @Autowired
    private BlogService blogService;
    @Autowired
    private PostService postService;
    @Autowired
    private BlogStatusService blogStatusService;

    @Override
    public List<String> listYearMonthStrs() {
        return monthlySelectedDaoMapper.listYearMonthStrs();
    }

    @Override
    public MonthlySelectedPost listSelectedByYearMonth(String yearMonth) {
        List<SelectedPostAccess> selectedPostAccessList = monthlySelectedDaoMapper.listSelectedPostsByYearMonthStr(yearMonth, CommonConstants.MONTHLY_SELECTED_POSTS_LIMIT);

        List<MonthlySelectedPost.PostInfoWithBlogStatus> postInfos = selectedPostAccessList.stream()
                .sorted(Comparator.comparing(SelectedPostAccess::getPublishedAt).reversed())
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
        monthlySelectedPost.setYearMonthStr(yearMonth);
        monthlySelectedPost.setPostInfos(postInfos);

        return monthlySelectedPost;
    }

}
