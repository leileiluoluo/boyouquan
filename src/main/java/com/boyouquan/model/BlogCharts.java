package com.boyouquan.model;

import lombok.Data;

import java.util.List;

@Data
public class BlogCharts {

    private List<String> yearlyInitiatedDataLabels;
    private List<Integer> yearlyInitiatedDataValues;

    private boolean showLatestInitiatedChart;

    private List<String> yearlyAccessDataLabels;
    private List<Integer> yearlyAccessDataValues;

    private boolean showLatestPublishedAtChart;

    private List<String> yearlyPublishDataLabels;
    private List<Integer> yearlyPublishDataValues;

}
