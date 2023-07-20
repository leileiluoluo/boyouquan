package com.boyouquan.model;

import lombok.Data;

@Data
public class User {

    private String username;
    private String md5Password;
    private Role role;

    public enum Role {
        admin
    }

}
