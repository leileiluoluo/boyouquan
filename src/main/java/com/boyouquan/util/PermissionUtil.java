package com.boyouquan.util;

import com.boyouquan.model.User;
import jakarta.servlet.http.HttpServletRequest;

public class PermissionUtil {

    public static boolean hasAdminPermission(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (null != user && User.Role.admin.equals(user.getRole())) {
            return true;
        }
        return false;
    }

}
