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

        if (isp.length() > 30) {
            isp = "";
        }

        String info = location + isp;
        info = info.replaceAll("，", "");
        info = info.replaceAll("。", "");

        return info;
    }

}
