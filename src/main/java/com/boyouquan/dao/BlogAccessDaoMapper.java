package com.boyouquan.dao;

import com.boyouquan.model.BlogAccess;

public interface BlogAccessDaoMapper {

    Long countBlogAccessByLinkPrefix(String linkPrefix);

    Long countByLink(String link);

    void save(BlogAccess blogAccess);

    Long countTotal();

}
