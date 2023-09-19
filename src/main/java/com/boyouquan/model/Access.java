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
public class Access {

    private String link;
    private String blogDomainName;
    private String ip;
    private From from;
    private Date accessedAt;

    public enum From {
        website,
        feed,
        unknown;

        public static From of(String from) {
            for (From f : From.values()) {
                if (f.name().equals(from)) {
                    return f;
                }
            }
            return From.unknown;
        }
    }

}
