package com.boyouquan.service.impl;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.dao.BlogStatusDaoMapper;
import com.boyouquan.model.Blog;
import com.boyouquan.model.BlogStatus;
import com.boyouquan.service.BlogStatusService;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;

@Service
public class BlogStatusServiceImpl implements BlogStatusService {

    private static final Logger logger = LoggerFactory.getLogger(BlogStatusServiceImpl.class);

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(Duration.ofMinutes(1))
            .readTimeout(Duration.ofMinutes(1))
            .callTimeout(Duration.ofMinutes(2))
            .build();

    @Autowired
    private BlogStatusDaoMapper blogStatusDaoMapper;

    @Override
    public void detectBlogStatus(Blog blog) {
        BlogStatus status = getLatestByBlogDomainName(blog.getDomainName());

        try (Response response = requestBlogAddress(blog.getAddress());
             ResponseBody responseBody = response.body()) {

            int code = response.code();
            String responseBodyString = responseBody.string();

            // ok
            if (HttpStatus.OK.value() == code) {
                if (null == status || !BlogStatus.Status.ok.equals(status.getStatus())) { // status not exists
                    BlogStatus blogStatus = new BlogStatus();
                    blogStatus.setBlogDomainName(blog.getDomainName());
                    blogStatus.setStatus(BlogStatus.Status.ok);
                    blogStatus.setCode(code);
                    save(blogStatus);
                    return;
                }
            }

            // not ok
            logger.info("blog not ok, blogDomainName: {}, statusCode: {}, responseBodyString: {}", blog.getDomainName(), code, responseBodyString);

            if (null == status || !BlogStatus.Status.can_not_be_accessed.equals(status.getStatus())) {
                BlogStatus blogStatus = new BlogStatus();
                blogStatus.setBlogDomainName(blog.getDomainName());
                blogStatus.setStatus(BlogStatus.Status.can_not_be_accessed);
                blogStatus.setCode(code);
                blogStatus.setReason("blog can bot be accessed");
                save(blogStatus);
            }
        } catch (Exception e) {
            logger.error("error in detect blog status", e);
        }
    }

    @Override
    public boolean isStatusOkByBlogDomainName(String blogDomainName) {
        BlogStatus blogStatus = getLatestByBlogDomainName(blogDomainName);
        if (null == blogStatus) {
            return true;
        }
        return BlogStatus.Status.ok.equals(blogStatus.getStatus());
    }

    @Override
    public BlogStatus getLatestByBlogDomainName(String blogDomainName) {
        return blogStatusDaoMapper.getLatestByBlogDomainName(blogDomainName);
    }

    @Override
    public void save(BlogStatus blogStatus) {
        blogStatusDaoMapper.save(blogStatus);
    }

    private Response requestBlogAddress(String blogAddress) throws IOException {
        Request request = new Request.Builder()
                .addHeader(CommonConstants.HEADER_USER_AGENT, CommonConstants.DATA_SPIDER_USER_AGENT)
                .url(blogAddress)
                .build();

        Call call = client.newCall(request);
        return call.execute();
    }

}
