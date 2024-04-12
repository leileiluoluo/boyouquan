package com.boyouquan.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class IpInfo {

    private String info1;
    private String isp;

    public String getAddressInfo() {
        if (StringUtils.isBlank(info1)) {
            return null;
        }
        if (StringUtils.isBlank(isp)) {
            return info1;
        }
        if (isp.contains("阿里")) {
            isp = "阿里云";
        } else if (isp.contains("腾讯")) {
            isp = "腾讯";
        } else if (isp.contains("华为")) {
            isp = "华为云";
        } else if (isp.contains("DigitalOcean")) {
            isp = " DigitalOcean";
        } else if (isp.length() > 100) {
            isp = "";
        }

        return info1 + isp;
    }

}
