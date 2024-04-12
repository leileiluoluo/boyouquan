package com.boyouquan.util;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IPUtil {

    private static final Logger logger = LoggerFactory.getLogger(IPUtil.class);

    public static String getRealIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        return checkIp(ip) ? ip : (
                checkIp(ip = request.getHeader("Proxy-Client-IP")) ? ip : (
                        checkIp(ip = request.getHeader("WL-Proxy-Client-IP")) ? ip :
                                request.getRemoteAddr()));
    }

    private static boolean checkIp(String ip) {
        return !StringUtils.isEmpty(ip) && !"unknown".equalsIgnoreCase(ip);
    }

    public static String domainToIp(String domain) {
        try {
            InetAddress inetAddress = InetAddress.getByName(domain);
            return inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

}