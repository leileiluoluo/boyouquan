package com.boyouquan.util;

public class LoginUtil {

    private static String SESSION_ID = null;

    public static void setSessionId(String sessionId) {
        SESSION_ID = sessionId;
    }

    public static String getSessionId() {
        return SESSION_ID;
    }

    public static void removeSessionId() {
        SESSION_ID = null;
    }

}
