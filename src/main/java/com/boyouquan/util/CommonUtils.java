package com.boyouquan.util;

import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonUtils {

    private static final SimpleDateFormat COMMON_DATE_PATTERN = new SimpleDateFormat("yyyy年M月d日");

    private static final SimpleDateFormat MORE_COMMON_DATE_PATTERN = new SimpleDateFormat("yyyy/MM/dd");

    public static String dateCommonFormatDisplay(Date date) {
        return MORE_COMMON_DATE_PATTERN.format(date);
    }

    public static String dateFriendlyDisplay(Date date) {
        final long halfAHour = 30 * 60 * 1000;
        final long oneHour = 60 * 60 * 1000;
        final long oneDay = 24 * oneHour;
        final long tenDay = 10 * oneDay;

        long now = System.currentTimeMillis();
        long past = date.getTime();

        long timeDiff = now - past;
        if (timeDiff < halfAHour) {
            return "刚刚";
        } else if (timeDiff < oneHour) {
            return "半小时前";
        } else if (timeDiff < oneDay) {
            int hours = (int) (timeDiff / oneHour);
            return String.format("%d小时前", hours);
        } else if (timeDiff < tenDay) {
            int days = (int) (timeDiff / oneDay);
            return String.format("%d天前", days);
        }
        return COMMON_DATE_PATTERN.format(past);
    }

    public static String md5(String str) {
        StringBuilder md5 = new StringBuilder();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(str.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = messageDigest.digest();
            for (byte b : bytes) {
                md5.append(byteToHex(b));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5.toString();
    }

    private static String byteToHex(byte b) {
        String hex = Integer.toHexString(b & 0xFF);
        if (hex.length() < 2) {
            hex = "0" + hex;
        }
        return hex;
    }

    public static String trimHttpScheme(String address) {
        if (StringUtils.isBlank(address)) {
            return "";
        }

        if (address.startsWith("http://")) {
            address = address.substring(7);
        } else if (address.startsWith("https://")) {
            address = address.substring(8);
        }
        return address;
    }

}
