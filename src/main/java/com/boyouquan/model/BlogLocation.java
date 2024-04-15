package com.boyouquan.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BlogLocation {

    private String domainName;
    private String location;
    private Date createdAt;
    private Date updatedAt;
    protected Boolean deleted;

    public String getLocationShort() {
        if (StringUtils.isBlank(location)) {
            return null;
        }
        if (location.length() > 20) {
            String separator = "国";
            if (location.contains(separator)) {
                return location.substring(0, location.indexOf(separator) + separator.length());
            }
            return null;
        }
        return location;
    }

}
