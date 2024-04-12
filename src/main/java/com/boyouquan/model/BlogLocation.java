package com.boyouquan.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BlogLocation {

    private String domainName;
    @JsonProperty("info1")
    private String location;
    private String isp;
    private Date createdAt;
    private Date updatedAt;
    protected Boolean deleted;

    public String getLocationInfo() {
        if (StringUtils.isBlank(location)) {
            return null;
        }

        if ("保留IP".equals(location)) {
            return null;
        }

        if (StringUtils.isBlank(isp)) {
            return location;
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

        return location + isp;
    }

}
