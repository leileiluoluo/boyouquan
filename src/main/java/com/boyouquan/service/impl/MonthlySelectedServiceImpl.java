package com.boyouquan.service.impl;

import com.boyouquan.dao.MonthlySelectedDaoMapper;
import com.boyouquan.model.SelectedPostAccess;
import com.boyouquan.service.MonthlySelectedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MonthlySelectedServiceImpl implements MonthlySelectedService {

    @Autowired
    private MonthlySelectedDaoMapper monthlySelectedDaoMapper;

    @Override
    public List<String> listYearMonthStrs() {
        return monthlySelectedDaoMapper.listYearMonthStrs();
    }

    @Override
    public List<SelectedPostAccess> listSelectedPostsByYearMonthStr(String yearMonthStr, int limit) {
        return monthlySelectedDaoMapper.listSelectedPostsByYearMonthStr(yearMonthStr, limit);
    }

}
