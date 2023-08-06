package com.boyouquan.service;

public interface GravatarService {

    byte[] getImage(String md5Email, int size);

}
