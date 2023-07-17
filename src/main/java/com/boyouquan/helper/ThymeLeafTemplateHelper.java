package com.boyouquan.helper;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.enumeration.BlogEnums;
import com.boyouquan.util.CommonUtils;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static com.boyouquan.util.CommonUtils.md5;

@Component
public class ThymeLeafTemplateHelper {

    public String getBlogAdminSmallImageURLByAddress(String blogAddress) {
        String email = BlogEnums.getEmailByBlogAddress(blogAddress);
        md5(email);
        return String.format(CommonConstants.GRAVATAR_ADDRESS_SMALL_SIZE, md5(email));
    }

    public String getBlogAdminLargeImageURLByAddress(String blogAddress) {
        String email = BlogEnums.getEmailByBlogAddress(blogAddress);
        md5(email);
        return String.format(CommonConstants.GRAVATAR_ADDRESS_LARGE_SIZE, md5(email));
    }

    public static String dateCommonFormatDisplay(Date date) {
        return CommonUtils.dateCommonFormatDisplay(date);
    }

    public String dateFriendlyDisplay(Date date) {
        return CommonUtils.dateFriendlyDisplay(date);
    }

    public String urlEncode(String url) {
        return URLEncoder.encode(url, StandardCharsets.UTF_8);
    }

    public String getDomain(String address) {
        return CommonUtils.getDomain(address);
    }

}
