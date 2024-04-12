package com.boyouquan.helper;

import com.boyouquan.config.BoYouQuanConfig;
import com.boyouquan.model.IpInfo;
import com.boyouquan.util.IPUtil;
import com.boyouquan.util.ObjectUtil;
import com.boyouquan.util.OkHttpUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class IPInfoHelper {

    private static final Logger logger = LoggerFactory.getLogger(IPInfoHelper.class);

    private static final OkHttpClient client = OkHttpUtil.getUnsafeOkHttpClient();

    @Autowired
    private BoYouQuanConfig boYouQuanConfig;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public String getIpInfoByDomainName(String domainName) {
        String ip = IPUtil.domainToIp(domainName);
        if (StringUtils.isBlank(ip)) {
            return null;
        }

        String url = boYouQuanConfig.getIpInfoQueryUrl() + ip;

        Request request = new Request.Builder()
                .url(url)
                .build();

        Call call = client.newCall(request);
        try (Response response = call.execute();
             ResponseBody responseBody = response.body()) {
            String body = responseBody.string();
            if (HttpStatus.OK.value() != response.code()) {
                logger.error("request ip info failed, status: {}, body: {}", response.code(), body);
            }

            JsonNode node = objectMapper.readTree(body);
            String info = node.get("ipdata").toString();

            IpInfo ipInfo = ObjectUtil.jsonToObject(info, IpInfo.class);

            return ipInfo.getAddressInfo();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

}
