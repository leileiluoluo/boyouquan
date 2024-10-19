package com.boyouquan.model;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
    private Date collectedAt;
    private Date updatedAt;
    private Boolean draft;
    private Boolean valid;
    protected Boolean deleted;

}
