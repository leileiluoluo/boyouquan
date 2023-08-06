package com.boyouquan.service;

public interface GravatarService {

    byte[] getImage(String md5Email, int size);

    void refreshLocalImage(String md5Email, int size);

}
