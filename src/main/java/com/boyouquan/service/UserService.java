package com.boyouquan.service;

import com.boyouquan.model.User;

public interface UserService {

    User getUserByUsername(String username);

    boolean isUsernamePasswordValid(String username, String password);

}
