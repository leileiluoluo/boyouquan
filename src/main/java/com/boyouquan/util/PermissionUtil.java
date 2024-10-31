package com.boyouquan.util;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;

public class PermissionUtil {

    public static boolean hasAdminPermission(HttpServletRequest request) {
        String sessionId = request.getHeader("sessionId");
        return !StringUtils.isBlank(sessionId)
                && null != LoginUtil.getSessionId()
                && LoginUtil.getSessionId().equals(sessionId);
    }

}
