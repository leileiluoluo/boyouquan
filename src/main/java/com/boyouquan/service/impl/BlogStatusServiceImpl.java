package com.boyouquan.service.impl;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.dao.BlogStatusDaoMapper;
import com.boyouquan.model.Blog;
import com.boyouquan.model.BlogStatus;
import com.boyouquan.service.BlogStatusService;
import com.boyouquan.util.CommonUtils;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.time.Duration;
import java.time.Period;

@Service
public class BlogStatusServiceImpl implements BlogStatusService {

    private static final Logger logger = LoggerFactory.getLogger(BlogStatusServiceImpl.class);

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(Duration.ofMinutes(2))
            .readTimeout(Duration.ofMinutes(2))
            .callTimeout(Duration.ofMinutes(4))
            .build();

    @Autowired
    private BlogStatusDaoMapper blogStatusDaoMapper;

    @Override
    public String getUnOkInfo(String blogDomainName) {
        BlogStatus blogStatus = getLatestByBlogDomainName(blogDomainName);
        if (null == blogStatus
                || BlogStatus.Status.ok.equals(blogStatus.getStatus())) {
            return "";
        }

        String dateBeyondSomeRange = CommonUtils.dateBeyondSomeRangeStr(blogStatus.getDetectedAt());
        if (StringUtils.isNotBlank(dateBeyondSomeRange)) {
            return String.format("自我第一次检测到你无法访问以来，到现在%s，但你仍未恢复正常。在接下来的365个日夜里，我依旧会像往常一样，每天向你发射一次心跳，希望能得到你归来的讯息，而不希望你就此消散在网络的海洋里。", dateBeyondSomeRange);
        }

        return "系统检测到该博客暂时无法访问。";
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
                save(
                        blog.getDomainName(),
                        currentStatus,
                        code,
                        reason
                );
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
        if (!BlogStatus.Status.ok.equals(blogStatus.getStatus())) {
            long now = System.currentTimeMillis();
            long past = blogStatus.getDetectedAt().getTime();
            int oneDay = 24 * 60 * 60 * 1000;
            return (now - past) <= oneDay;
        }

        return true;
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

}
