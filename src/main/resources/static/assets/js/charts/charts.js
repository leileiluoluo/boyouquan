// 基于准备好的dom，初始化echarts实例
              var myChart = echarts.init(document.getElementById('charts'));

              // 指定图表的配置项和数据
              const data = [["2023-06-05", 116], ["2023-06-06", 129], ["2023-06-07", 135], ["2023-06-08", 86], ["2023-06-09", 73], ["2023-06-10", 85], ["2023-06-11", 73], ["2023-06-12", 68], ["2023-06-13", 92], ["2023-06-14", 130], ["2023-06-15", 245], ["2023-06-16", 139], ["2023-06-17", 115], ["2023-06-18", 111], ["2023-06-19", 309], ["2023-06-20", 206], ["2023-06-21", 137], ["2023-06-22", 128], ["2023-06-23", 85], ["2023-06-24", 94], ["2023-06-25", 71], ["2023-06-26", 106], ["2023-06-27", 84], ["2023-06-28", 93], ["2023-06-29", 85], ["2023-06-30", 73], ["2023-07-01", 83], ["2023-07-02", 125], ["2023-07-03", 107], ["2023-07-04", 82]];
              const dateList = data.map(function (item) {
                return item[0];
              });
              const valueList = data.map(function (item) {
                return item[1];
              });
              option = {
                // Make gradient line here
                visualMap: [
                  {
                    show: false,
                    type: 'continuous',
                    seriesIndex: 0,
                    min: 0,
                    max: 300
                  }
                ],
                title: [
                  {
                    left: 'center',
//                    text: 'Gradient along the y axis'
                  }
                ],
                tooltip: {
                  trigger: 'axis'
                },
                xAxis: [
                  {
                    data: dateList
                  }
                ],
                yAxis: [
                  {}
                ],
                grid: [
                  {
                    bottom: '60%'
                  }
                ],
                series: [
                  {
                    type: 'line',
                    showSymbol: false,
                    data: valueList
                  }
                ]
              };

              // 使用刚指定的配置项和数据显示图表。
              myChart.setOption(option);