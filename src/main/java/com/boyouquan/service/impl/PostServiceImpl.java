package com.boyouquan.service.impl;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.dao.PostDaoMapper;
import com.boyouquan.model.BlogDomainNamePublish;
import com.boyouquan.model.MonthPublish;
import com.boyouquan.model.Post;
import com.boyouquan.service.PostService;
import com.boyouquan.util.Pagination;
import com.boyouquan.util.PaginationBuilder;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.time.Duration;
import java.util.Date;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(Duration.ofMinutes(2))
            .readTimeout(Duration.ofMinutes(2))
            .callTimeout(Duration.ofMinutes(4))
            .build();

    @Autowired
    private PostDaoMapper postDaoMapper;

    @Override
    public void detectPostStatus(String blogDomainName, String link) {
        try (Response response = requestPostLink(link);
             ResponseBody responseBody = response.body()) {

            int code = response.code();
            String responseBodyString = responseBody.string();
            if (HttpStatus.OK.value() != code) {
                logger.info("response code is not 200 (responseBody: {}), post will be deleted!", responseBodyString);
                deleteByLink(link);
            }
        } catch (SocketTimeoutException e) {
            logger.error("timeout exception", e);
        } catch (Exception e) {
            logger.error("exception caught", e);
            deleteByLink(link);
        }
    }

    @Override
    public BlogDomainNamePublish getMostPublishedInLatestOneMonth() {
        return postDaoMapper.getMostPublishedInLatestOneMonth();
    }

    @Override
    public List<MonthPublish> getBlogPostPublishSeriesInLatestOneYear(String blogDomainName) {
        return postDaoMapper.getBlogPostPublishSeriesInLatestOneYear(blogDomainName);
    }

    @Override
    public Pagination<Post> listWithKeyWord(String keyword, int page, int size) {
        if (page < 1 || size <= 0) {
            return PaginationBuilder.buildEmptyResults();
        }

        int offset = (page - 1) * size;
        List<Post> posts = postDaoMapper.listWithKeyWord(keyword, offset, size);
        Long total = postDaoMapper.countWithKeyWord(keyword);
        return PaginationBuilder.<Post>newBuilder()
                .pageNo(page)
                .pageSize(size)
                .total(total)
                .results(posts).build();
    }

    @Override
    public Long countAll() {
        return postDaoMapper.countAll();
    }

    @Override
    public Date getLatestPublishedAtByBlogDomainName(String blogDomainName) {
        return postDaoMapper.getLatestPublishedAtByBlogDomainName(blogDomainName);
    }

    @Override
    public Long countByBlogDomainName(String blogDomainName) {
        return postDaoMapper.countByBlogDomainName(blogDomainName);
    }

    @Override
    public Pagination<Post> listByDraftAndBlogDomainName(boolean draft, String blogDomainName, int page, int size) {
        if (page < 1 || size <= 0) {
            return PaginationBuilder.buildEmptyResults();
        }

        int offset = (page - 1) * size;
        List<Post> posts = postDaoMapper.listByDraftAndBlogDomainName(draft, blogDomainName, offset, size);
        Long total = postDaoMapper.countByDraftAndBlogDomainName(draft, blogDomainName);
        return PaginationBuilder.<Post>newBuilder()
                .pageNo(page)
                .pageSize(size)
                .total(total)
                .results(posts).build();
    }

    @Override
    public boolean existsByLink(String link) {
        return postDaoMapper.existsByLink(link);
    }

    @Override
    public Post getByLink(String link) {
        return postDaoMapper.getByLink(link);
    }

    @Override
    public boolean batchSave(List<Post> posts) {
        try {
            if (null != posts && !posts.isEmpty()) {
                postDaoMapper.batchSave(posts);
                return true;
            }
        } catch (Exception e) {
            logger.error("batch save failed!", e);
        }
        return false;
    }

    @Override
    public boolean batchUpdateDraftByBlogDomainName(String blogDomainName, boolean draft) {
        try {
            postDaoMapper.batchUpdateDraftByBlogDomainName(blogDomainName, draft);
            return true;
        } catch (Exception e) {
            logger.error("batch save failed!", e);
        }
        return false;
    }

    @Override
    public void deleteByBlogDomainName(String blogDomainName) {
        postDaoMapper.deleteByBlogDomainName(blogDomainName);
    }

    @Override
    public void deleteByLink(String link) {
        postDaoMapper.deleteByLink(link);
        logger.info("link deleted! {}", link);
    }

    private Response requestPostLink(String link) throws IOException {
        Request request = new Request.Builder()
                .addHeader(CommonConstants.HEADER_USER_AGENT, CommonConstants.DATA_SPIDER_USER_AGENT)
                .url(link)
                .build();

        Call call = client.newCall(request);
        return call.execute();
    }

}
