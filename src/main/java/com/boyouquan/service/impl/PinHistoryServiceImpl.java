package com.boyouquan.service.impl;

import com.boyouquan.dao.PinHistoryDaoMapper;
import com.boyouquan.model.PinHistory;
import com.boyouquan.service.PinHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PinHistoryServiceImpl implements PinHistoryService {

    @Autowired
    private PinHistoryDaoMapper pinHistoryDaoMapper;

    @Override
    public void save(PinHistory pinHistory) {
        pinHistoryDaoMapper.save(pinHistory);
    }

}
