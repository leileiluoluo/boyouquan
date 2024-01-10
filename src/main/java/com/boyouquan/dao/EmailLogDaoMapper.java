package com.boyouquan.dao;

import com.boyouquan.model.EmailLog;

public interface EmailLogDaoMapper {

    boolean existsByBlogDomainNameAndType(String blogDomainName, EmailLog.Type type);

    EmailLog getLatestByBlogDomainNameAndType(String blogDomainName, EmailLog.Type type);

    void save(EmailLog emailLog);

}
