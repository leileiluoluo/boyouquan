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
public class BlogAggregate {

    private String name;
    private String address;
    private Date latestUpdatedAt;
    private Long postCount;

}
