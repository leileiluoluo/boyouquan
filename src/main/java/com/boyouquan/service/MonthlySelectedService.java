package com.boyouquan.service;

import com.boyouquan.model.SelectedPostAccess;

import java.util.List;

public interface MonthlySelectedService {

    List<String> listYearMonthStrs();

    List<SelectedPostAccess> listSelectedPostsByYearMonthStr(String yearMonthStr, int limit);

}
