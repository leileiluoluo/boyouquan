package com.boyouquan.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class IPLocationInfo {

    @JsonProperty("pro")
    private String province;
    private String city;
    @JsonProperty("addr")
    private String address;

    public String getLocationInfo() {
        String info = null;
        if (StringUtils.isNotBlank(city)) {
            if (StringUtils.isNotBlank(province)) {
                if (!city.equals(province)) {
                    info = province + city;
                } else {
                    info = city;
                }
            } else {
                info = city;
            }
            return info;
        }

        if (StringUtils.isNotBlank(address)) {
            return address;
        }

        return info;
    }

}
