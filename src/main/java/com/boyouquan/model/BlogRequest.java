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
public class BlogRequest {

    private Long id;
    private String rssAddress;
    private String adminEmail;
    private String name;
    private String description;
    private Boolean selfSubmitted;
    private Date requestedAt;
    private Date updatedAt;
    private Status status;
    private String reason;
    private Boolean deleted;

    public enum Status {
        submitted,
        system_check_valid,
        system_check_invalid,
        approved,
        rejected,
        uncollected
    }

}
