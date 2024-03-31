package com.boyouquan.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class BlogDomainNameInitiated {

    private String blogDomainName;
    private Long initiatedCount;

}
