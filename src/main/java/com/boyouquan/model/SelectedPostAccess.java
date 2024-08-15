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
public class SelectedPostAccess {

    private String yearMonthStr;
    private String postLink;
    private Date publishedAt;
    private String blogDomainName;
    private String accessCount;

}
