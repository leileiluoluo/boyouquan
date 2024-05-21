package com.boyouquan.util;

import jakarta.servlet.http.HttpServletRequest;

public class UserAgentUtil {

    public static String getUserAgent(HttpServletRequest request) {
        return request.getHeader("User-Agent");
    }

}
