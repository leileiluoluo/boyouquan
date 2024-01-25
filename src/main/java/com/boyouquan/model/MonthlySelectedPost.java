package com.boyouquan.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class MonthlySelectedPost {

    private String yearMonthStr;
    private List<PostInfo> postInfos;

}
