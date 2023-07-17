package com.boyouquan.service.impl;

import com.boyouquan.dao.AccessDaoMapper;
import com.boyouquan.model.Access;
import com.boyouquan.service.AccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccessServiceImpl implements AccessService {

    @Autowired
    private AccessDaoMapper accessDaoMapper;

    @Override
    public void save(Access access) {
        accessDaoMapper.save(access);
    }

}
