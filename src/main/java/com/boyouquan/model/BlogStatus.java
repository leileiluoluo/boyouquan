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
public class BlogStatus {

    private String blogDomainName;
    private Status status;
    private Integer code;
    private String reason;
    private Date detectedAt;

    public enum Status {
        ok,
        timeout,
        can_not_be_accessed
    }

}
