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
public class BlogCandidate {

    private String adminEmail;
    private String rssAddress;
    private String description;
    private Boolean selfSubmitted;
    private Date collected;

}
