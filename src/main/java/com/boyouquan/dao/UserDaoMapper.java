package com.boyouquan.dao;


import com.boyouquan.model.User;

public interface UserDaoMapper {

    User getUserByUsername(String username);

}
