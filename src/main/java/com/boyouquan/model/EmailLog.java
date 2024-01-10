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
public class EmailLog {

    private String blogDomainName;
    private String email;
    private Type type;
    private Date sendAt;

    public enum Type {
        blog_can_not_be_accessed
    }

}
