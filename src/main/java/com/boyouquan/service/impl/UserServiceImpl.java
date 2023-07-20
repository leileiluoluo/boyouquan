package com.boyouquan.service.impl;

import com.boyouquan.dao.UserDaoMapper;
import com.boyouquan.model.User;
import com.boyouquan.service.UserService;
import com.boyouquan.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDaoMapper userDaoMapper;

    @Override
    public User getUserByUsername(String username) {
        return userDaoMapper.getUserByUsername(username);
    }

    @Override
    public boolean isUsernamePasswordValid(String username, String password) {
        User user = getUserByUsername(username);
        if (null == user) {
            return false;
        }

        return CommonUtils.md5(password).equals(user.getMd5Password());
    }

}
