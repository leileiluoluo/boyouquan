package com.boyouquan.service.impl;

import com.boyouquan.dao.EmailLogDaoMapper;
import com.boyouquan.model.EmailLog;
import com.boyouquan.service.EmailLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailLogServiceImpl implements EmailLogService {

    @Autowired
    private EmailLogDaoMapper emailLogDaoMapper;

    @Override
    public boolean existsByBlogDomainNameAndType(String blogDomainName, EmailLog.Type type) {
        return emailLogDaoMapper.existsByBlogDomainNameAndType(blogDomainName, type);
    }

    @Override
    public EmailLog getLatestByBlogDomainNameAndType(String blogDomainName, EmailLog.Type type) {
        return emailLogDaoMapper.getLatestByBlogDomainNameAndType(blogDomainName, type);
    }

    @Override
    public void save(EmailLog emailLog) {
        emailLogDaoMapper.save(emailLog);
    }

}
