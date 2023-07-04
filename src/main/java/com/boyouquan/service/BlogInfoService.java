package com.boyouquan.service;

import com.boyouquan.model.BlogInfo;
import com.boyouquan.util.Pagination;

public interface BlogInfoService {

    Pagination<BlogInfo> listBlogInfos(int page, int size);

}
