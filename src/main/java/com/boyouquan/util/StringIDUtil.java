package com.boyouquan.util;

import java.util.UUID;

public class StringIDUtil {

    public static String generateId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
