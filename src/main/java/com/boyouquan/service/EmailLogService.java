package com.boyouquan.service;

import com.boyouquan.model.EmailLog;

public interface EmailLogService {

    boolean existsByBlogDomainNameAndType(String blogDomainName, EmailLog.Type type);

    EmailLog getLatestByBlogDomainNameAndType(String blogDomainName, EmailLog.Type type);

    void save(EmailLog emailLog);

}
