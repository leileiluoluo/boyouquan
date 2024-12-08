package com.boyouquan.service;

import com.boyouquan.model.PinHistory;

import java.util.Date;
import java.util.List;

public interface PinHistoryService {

    void save(PinHistory pinHistory);

    List<String> listLinksByBlogDomainName(String blogDomainName, Date startDate);

}
