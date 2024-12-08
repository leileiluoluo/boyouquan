package com.boyouquan.dao;

import com.boyouquan.model.PinHistory;

import java.util.Date;
import java.util.List;

public interface PinHistoryDaoMapper {

    void save(PinHistory pinHistory);

    List<String> listLinksByBlogDomainName(String blogDomainName, Date startDate);

}
