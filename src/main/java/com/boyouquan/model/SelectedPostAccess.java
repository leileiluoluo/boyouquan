package com.boyouquan.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class SelectedPostAccess {

    private String yearMonthStr;
    private String postLink;
    private String blogDomainName;
    private String accessCount;

}
