package com.boyouquan.util;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;

public class PermissionUtil {

    public static boolean hasAdminPermission(HttpServletRequest request) {
        String sessionId = request.getHeader("sessionId");
        String username = request.getHeader("username");

        if (StringUtils.isBlank(sessionId) || StringUtils.isBlank(username)) {
            return false;
        }

        String sessionIdStored = LoginUtil.getSessionId(username);
        return null != sessionIdStored
                && sessionIdStored.equals(sessionId);
    }

}
