package com.boyouquan.service.impl;

import com.boyouquan.dao.PinHistoryDaoMapper;
import com.boyouquan.model.PinHistory;
import com.boyouquan.service.PinHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PinHistoryServiceImpl implements PinHistoryService {

    @Autowired
    private PinHistoryDaoMapper pinHistoryDaoMapper;

    @Override
    public void save(PinHistory pinHistory) {
        pinHistoryDaoMapper.save(pinHistory);
    }

    @Override
    public List<String> listLinksByBlogDomainName(String blogDomainName, Date startDate) {
        return pinHistoryDaoMapper.listLinksByBlogDomainName(blogDomainName, startDate);
    }

}
