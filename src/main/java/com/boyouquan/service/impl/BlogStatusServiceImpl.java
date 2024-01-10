package com.boyouquan.service.impl;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.dao.BlogStatusDaoMapper;
import com.boyouquan.model.Blog;
import com.boyouquan.model.BlogStatus;
import com.boyouquan.model.EmailLog;
import com.boyouquan.service.BlogStatusService;
import com.boyouquan.service.EmailLogService;
import com.boyouquan.service.EmailService;
import com.boyouquan.util.CommonUtils;
import com.boyouquan.util.OkHttpUtil;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.SocketTimeoutException;

@Service
public class BlogStatusServiceImpl implements BlogStatusService {

    private static final Logger logger = LoggerFactory.getLogger(BlogStatusServiceImpl.class);

    private static final OkHttpClient client = OkHttpUtil.getUnsafeOkHttpClient();

    @Autowired
    private BlogStatusDaoMapper blogStatusDaoMapper;
    @Autowired
    private EmailLogService emailLogService;
    @Autowired
    private EmailService emailService;

    @Override
    public String getUnOkInfo(String blogDomainName) {
        BlogStatus blogStatus = getLatestByBlogDomainName(blogDomainName);
        if (null == blogStatus
                || BlogStatus.Status.ok.equals(blogStatus.getStatus())) {
            return "";
        }

        return CommonUtils.getBlogCannotBeAccessedInfo(blogStatus.getDetectedAt());
    }

    @Override
    public void detectBlogStatus(Blog blog) {
        BlogStatus latestStatus = getLatestByBlogDomainName(blog.getDomainName());
        BlogStatus.Status currentStatus = BlogStatus.Status.ok;
        int code = HttpStatus.OK.value();
        int maxReasonLength = 200;
        String reason = "";

        try (Response response = requestBlogAddress(blog.getAddress());
             ResponseBody responseBody = response.body()) {

            code = response.code();
            String responseBodyString = responseBody.string();
            if (HttpStatus.OK.value() != code) {
                currentStatus = BlogStatus.Status.can_not_be_accessed;
                if (StringUtils.isNotBlank(responseBodyString)) {
                    reason = (responseBodyString.length() >= maxReasonLength)
                            ? responseBodyString.substring(0, maxReasonLength)
                            : responseBodyString;
                }
            }
        } catch (SocketTimeoutException e) {
            logger.error("timeout", e);
            currentStatus = BlogStatus.Status.timeout;
            code = HttpStatus.GATEWAY_TIMEOUT.value();
            reason = "timeout";
        } catch (Exception e) {
            logger.error("error in detect blog status", e);
            currentStatus = BlogStatus.Status.can_not_be_accessed;
            code = HttpStatus.INTERNAL_SERVER_ERROR.value();
            reason = (e.getMessage().length() >= maxReasonLength)
                    ? e.getMessage().substring(0, maxReasonLength)
                    : e.getMessage();
        } finally {
            if (null == latestStatus || !currentStatus.equals(latestStatus.getStatus())) {

                // save
                save(
                        blog.getDomainName(),
                        currentStatus,
                        code,
                        reason
                );

                // send email
                sendEmail(blog, currentStatus);
            }
        }
    }

    @Override
    public boolean isStatusOkByBlogDomainName(String blogDomainName) {
        BlogStatus blogStatus = getLatestByBlogDomainName(blogDomainName);
        if (null == blogStatus) {
            return true;
        }

        // FIXME: maybe the algorithm should be modified later
        return BlogStatus.Status.ok.equals(blogStatus.getStatus());
    }

    @Override
    public BlogStatus getLatestByBlogDomainName(String blogDomainName) {
        return blogStatusDaoMapper.getLatestByBlogDomainName(blogDomainName);
    }

    @Override
    public void save(String blogDomainName, BlogStatus.Status status, int code, String reason) {
        BlogStatus blogStatus = new BlogStatus();
        blogStatus.setBlogDomainName(blogDomainName);
        blogStatus.setStatus(status);
        blogStatus.setCode(code);
        blogStatus.setReason(reason);

        // save
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

    private void sendEmail(Blog blog, BlogStatus.Status status) {
        try {
            EmailLog emailLog = emailLogService.getLatestByBlogDomainNameAndType(blog.getDomainName(), EmailLog.Type.blog_can_not_be_accessed);

            boolean need2SendEmail = false;
            if (null == emailLog) {
                need2SendEmail = true;
            } else {
                long now = System.currentTimeMillis();
                long sendAt = emailLog.getSendAt().getTime();
                long oneMonth = (long) 30 * 24 * 60 * 60 * 1000;

                need2SendEmail = (now > sendAt) && ((now - sendAt) > oneMonth);
            }

            if (need2SendEmail) {
                emailService.sendBlogStatusNotOkNotice(blog, status);

                // save email log
                EmailLog newEmailLog = new EmailLog();
                newEmailLog.setBlogDomainName(blog.getDomainName());
                newEmailLog.setEmail(blog.getAdminEmail());
                newEmailLog.setType(EmailLog.Type.blog_can_not_be_accessed);
                emailLogService.save(newEmailLog);

                logger.info("blog can not access notice sent, blog: {}", blog.getDomainName());
            }
        } catch (Exception e) {
            logger.error("email send failed", e);
        }
    }

}
