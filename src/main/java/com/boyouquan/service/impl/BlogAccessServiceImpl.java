package com.boyouquan.service.impl;

import com.boyouquan.dao.BlogAccessDaoMapper;
import com.boyouquan.model.BlogAccess;
import com.boyouquan.service.BlogAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlogAccessServiceImpl implements BlogAccessService {

    @Autowired
    private BlogAccessDaoMapper blogAccessDaoMapper;

    @Override
    public Long countBlogAccessByLink(String link) {
        try {
            return blogAccessDaoMapper.countByLink(link);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0L;
    }

    @Override
    public void saveBlogAccess(BlogAccess blogAccess) {
        try {
            blogAccessDaoMapper.save(blogAccess);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Long totalCount() {
        return blogAccessDaoMapper.countTotal();
    }

}
