package com.boyouquan.dao;

import com.boyouquan.model.BlogAccess;

public interface BlogAccessDaoMapper {

    Long countByLink(String link);

    void save(BlogAccess blogAccess);

    Long countTotal();

}
