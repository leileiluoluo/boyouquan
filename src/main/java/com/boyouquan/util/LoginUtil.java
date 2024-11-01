package com.boyouquan.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LoginUtil {

    private static final Map<String, String> SESSION_ID = new ConcurrentHashMap<>();

    public static void setSessionId(String username, String sessionId) {
        SESSION_ID.put(username, sessionId);
    }

    public static String getSessionId(String username) {
        return SESSION_ID.get(username);
    }

    public static void removeSessionId(String username) {
        SESSION_ID.remove(username);
    }

}
