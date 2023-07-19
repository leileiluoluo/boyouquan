package com.boyouquan.helper;

import com.boyouquan.util.CommonUtils;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;


@Component
public class ThymeLeafTemplateHelper {

    public static String dateCommonFormatDisplay(Date date) {
        return CommonUtils.dateCommonFormatDisplay(date);
    }

    public static String dateHourCommonFormatDisplay(Date date) {
        return CommonUtils.dateHourCommonFormatDisplay(date);
    }

    public String dateFriendlyDisplay(Date date) {
        return CommonUtils.dateFriendlyDisplay(date);
    }

    public String urlEncode(String url) {
        return URLEncoder.encode(url, StandardCharsets.UTF_8);
    }

}
