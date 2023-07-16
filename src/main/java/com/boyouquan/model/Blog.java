package com.boyouquan.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class Blog {

    private String domainName;
    private String adminEmail;
    private String name;
    private String address;
    private String rssAddress;
    private String description;
    private Boolean selfSubmitted;
    private Date collectedAt;
    private Date updatedAt;
    private Boolean valid;
    protected Boolean deleted;

}
