package com.boyouquan.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BlogLocation {

    private String domainName;
    private String location;
    private String isp;
    private Date createdAt;
    private Date updatedAt;
    protected Boolean deleted;

}
