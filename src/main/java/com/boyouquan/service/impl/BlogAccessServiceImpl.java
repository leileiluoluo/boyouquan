package com.boyouquan.service.impl;

import com.boyouquan.dao.BlogAccessDaoMapper;
import com.boyouquan.model.BlogAccess;
import com.boyouquan.model.BlogAccessSummary;
import com.boyouquan.model.DayAccess;
import com.boyouquan.service.BlogAccessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogAccessServiceImpl implements BlogAccessService {

    private final Logger logger = LoggerFactory.getLogger(BlogAccessServiceImpl.class);

    @Autowired
    private BlogAccessDaoMapper blogAccessDaoMapper;

    @Override
    public List<DayAccess> getBlogAccessSeriesInLatestOneMonth(String blogAddress) {
        return blogAccessDaoMapper.getBlogAccessSeriesInLatestOneMonth(blogAddress);
    }

    @Override
    public BlogAccessSummary getMostAccessedBlogByLatestMonth() {
        return blogAccessDaoMapper.getMostAccessedBlogByLatestMonth();
    }

    @Override
    public Long countBlogAccessByLinkPrefix(String linkPrefix) {
        try {
            return blogAccessDaoMapper.countBlogAccessByLinkPrefix(linkPrefix);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return 0L;
    }

    @Override
    public Long countBlogAccessByLink(String link) {
        try {
            return blogAccessDaoMapper.countByLink(link);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return 0L;
    }

    @Override
    public void saveBlogAccess(BlogAccess blogAccess) {
        try {
            blogAccessDaoMapper.save(blogAccess);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public Long totalCount() {
        return blogAccessDaoMapper.countTotal();
    }

}
