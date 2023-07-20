package com.boyouquan.service.impl;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.dao.BlogRequestDaoMapper;
import com.boyouquan.helper.PostHelper;
import com.boyouquan.model.*;
import com.boyouquan.service.BlogCrawlerService;
import com.boyouquan.service.BlogRequestService;
import com.boyouquan.service.BlogService;
import com.boyouquan.service.PostService;
import com.boyouquan.util.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BlogRequestServiceImpl implements BlogRequestService {

    private final Logger logger = LoggerFactory.getLogger(BlogRequestServiceImpl.class);

    @Autowired
    private BlogRequestDaoMapper blogRequestDaoMapper;
    @Autowired
    private BlogCrawlerService blogCrawlerService;
    @Autowired
    private BlogService blogService;
    @Autowired
    private PostService postService;
    @Autowired
    private PostHelper postHelper;

    @Override
    public void processNewRequest(String rssAddress) {
        try {
            logger.info("start to process blog request, rssAddress: {}", rssAddress);

            BlogRequest blogRequest = getByRssAddress(rssAddress);
            if (null == blogRequest) {
                return;
            }

            // exists?
            boolean exists = blogService.existsByRssAddress(rssAddress);
            if (exists) {
                blogRequest.setStatus(BlogRequest.Status.system_check_invalid);
                blogRequest.setReason("博客已收录，请勿重复提交！");
                update(blogRequest);
                return;
            }

            // not exists
            logger.info("start to crawl: {}", rssAddress);

            RSSInfo rssInfo = blogCrawlerService.getRSSInfoByRSSAddress(rssAddress, CommonConstants.RSS_POST_COUNT_READ_LIMIT);
            if (null == rssInfo) {
                logger.error("rss info read failed, rssAddress: {}", rssAddress);
                blogRequest.setStatus(BlogRequest.Status.system_check_invalid);
                blogRequest.setReason("RSS地址不正确，抓取不到正确内容！");
                update(blogRequest);
                return;
            }

            // save blog and posts
            boolean saved = saveDraftBlogAndPosts(blogRequest, rssInfo);
            if (!saved) {
                blogRequest.setStatus(BlogRequest.Status.system_check_invalid);
                blogRequest.setReason("无法从RSS地址抓取正确内容！");
                update(blogRequest);
                return;
            }

            // success
            blogRequest.setStatus(BlogRequest.Status.system_check_valid);
            update(blogRequest);
        } catch (Exception e) {
            logger.error("new request process failed!", e);
        }
    }

    @Override
    public List<BlogRequestInfo> listBlogRequestInfosBySelfSubmittedAndStatuses(boolean selfSubmitted, List<BlogRequest.Status> statuses) {
        List<BlogRequestInfo> blogRequestInfos = new ArrayList<>();

        List<BlogRequest> blogRequests = blogRequestDaoMapper.listBySelfSubmittedAndStatuses(selfSubmitted, statuses);
        for (BlogRequest blogRequest : blogRequests) {
            blogRequestInfos.add(assembleBlogRequestInfo(blogRequest));
        }

        return blogRequestInfos;
    }

    @Override
    public List<BlogRequestInfo> listBlogRequestInfosByStatuses(List<BlogRequest.Status> statuses) {
        List<BlogRequestInfo> blogRequestInfos = new ArrayList<>();

        List<BlogRequest> blogRequests = blogRequestDaoMapper.listByStatuses(statuses);
        for (BlogRequest blogRequest : blogRequests) {
            blogRequestInfos.add(assembleBlogRequestInfo(blogRequest));
        }

        return blogRequestInfos;
    }

    @Override
    public BlogRequestInfo getBlogRequestInfoById(Long id) {
        BlogRequest blogRequest = getById(id);
        if (null == blogRequest) {
            return null;
        }

        return assembleBlogRequestInfo(blogRequest);
    }

    @Override
    public List<BlogRequest> listByStatus(BlogRequest.Status status) {
        return blogRequestDaoMapper.listByStatus(status);
    }

    @Override
    public BlogRequest getById(Long id) {
        return blogRequestDaoMapper.getById(id);
    }

    @Override
    public BlogRequest getByRssAddress(String rssAddress) {
        return blogRequestDaoMapper.getByRssAddress(rssAddress);
    }

    @Override
    public void approveById(Long id) {
        BlogRequest blogRequest = getById(id);
        if (null != blogRequest) {
            blogRequest.setStatus(BlogRequest.Status.approved);
            update(blogRequest);

            // change draft to false
            Blog blog = blogService.getByRSSAddress(blogRequest.getRssAddress());
            if (null != blog) {
                blog.setDraft(false);
                blogService.update(blog);

                postService.batchUpdateDraftByBlogDomainName(blog.getDomainName(), false);
            }
        }
    }

    @Override
    public void rejectById(Long id, String reason) {
        BlogRequest blogRequest = getById(id);
        if (null != blogRequest) {
            blogRequest.setStatus(BlogRequest.Status.rejected);
            blogRequest.setReason(reason);
            update(blogRequest);
        }
    }

    @Override
    public void update(BlogRequest blogRequest) {
        blogRequestDaoMapper.update(blogRequest);
    }

    @Override
    public void submit(BlogRequest blogRequest) {
        blogRequestDaoMapper.submit(blogRequest);
    }

    @Override
    public void deleteByRssAddress(String rssAddress) {
        BlogRequest blogRequest = getByRssAddress(rssAddress);
        if (null != blogRequest) {
            Blog blog = blogService.getByRSSAddress(rssAddress);
            if (null != blog) {
                // delete blogs
                blogService.deleteByDomainName(blog.getDomainName());

                // delete posts
                postService.deleteByBlogDomainName(blog.getDomainName());
            }
        }

        // delete blog requests
        blogRequestDaoMapper.deleteByRssAddress(rssAddress);
    }

    private boolean saveDraftBlogAndPosts(BlogRequest blogRequest, RSSInfo rssInfo) {
        String blogDomainName = CommonUtils.getDomain(rssInfo.getBlogAddress());

        boolean exists = blogService.existsByDomainName(blogDomainName);
        if (!exists && !rssInfo.getBlogPosts().isEmpty()) {
            Blog blog = new Blog();
            blog.setDomainName(blogDomainName);
            blog.setAdminEmail(blogRequest.getAdminEmail());
            blog.setName(blogRequest.getName());
            blog.setAddress(rssInfo.getBlogAddress());
            blog.setRssAddress(blogRequest.getRssAddress());
            blog.setDescription(blogRequest.getDescription());
            blog.setSelfSubmitted(blogRequest.getSelfSubmitted());
            blog.setCollectedAt(blogRequest.getRequestedAt());
            blog.setUpdatedAt(blogRequest.getUpdatedAt());
            blog.setDraft(true);

            // save posts
            boolean success = postHelper.savePosts(blogDomainName, rssInfo, true);
            if (!success) {
                logger.error("posts save failed, blogDomainName: {}", blogDomainName);
                return false;
            }

            // save blog
            blogService.save(blog);

            logger.info("blog and posts saved, blogDomainName: {}", blog.getDomainName());
        }

        return true;
    }

    private BlogRequestInfo assembleBlogRequestInfo(BlogRequest blogRequest) {
        BlogRequestInfo blogRequestInfo = new BlogRequestInfo();
        BeanUtils.copyProperties(blogRequest, blogRequestInfo);

        List<BlogRequest.Status> successStatusList = List.of(BlogRequest.Status.submitted, BlogRequest.Status.system_check_valid);
        List<BlogRequest.Status> failureStatusList = List.of(BlogRequest.Status.system_check_invalid, BlogRequest.Status.rejected);
        if (successStatusList.contains(blogRequest.getStatus())) {
            blogRequestInfo.setStatusInfo("等待审核");
        } else if (failureStatusList.contains(blogRequest.getStatus())) {
            blogRequestInfo.setStatusInfo("未通过");
            blogRequestInfo.setFailed(true);
        } else if (BlogRequest.Status.approved.equals(blogRequest.getStatus())) {
            blogRequestInfo.setStatusInfo("通过");
        }

        Blog blog = blogService.getByRSSAddress(blogRequest.getRssAddress());
        blogRequestInfo.setApproved(BlogRequest.Status.approved.equals(blogRequest.getStatus()));
        if (BlogRequest.Status.approved.equals(blogRequest.getStatus())) {
            if (null != blog) {
                blogRequestInfo.setDomainName(blog.getDomainName());
            }
        }

        // admin email
        String email = blogRequest.getAdminEmail();
        String emailPart1 = email.split("@")[0];
        String emailPart2 = email.split("@")[1];
        blogRequestInfo.setAdminEmail(emailPart1.charAt(0) + "****@" + emailPart2);

        // posts
        if (null != blog) {
            List<Post> posts = postService.listByDraftAndBlogDomainName(true, blog.getDomainName(), 10);
            blogRequestInfo.setPosts(posts);
        }

        return blogRequestInfo;
    }

}
