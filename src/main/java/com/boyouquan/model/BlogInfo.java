package com.boyouquan.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class BlogInfo extends Blog {

    private Long postCount;
    private Long accessCount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
    private Date latestPublishedAt;
    private List<Post> posts = Collections.emptyList();
    private String blogAdminMediumImageURL;
    private String blogAdminLargeImageURL;
    private String submittedInfo;
    private String submittedInfoTip;
    private boolean statusOk;
    private boolean sunset;
    private String statusUnOkInfo;
    private String blogServerLocation;

}
