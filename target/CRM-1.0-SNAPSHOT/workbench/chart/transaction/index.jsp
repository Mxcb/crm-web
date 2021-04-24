<%@page contentType="text/html;charset=UTF-8" language="java"%>
<% String basePath=request.getScheme()+"://"+request.getServerName()+
        ":"+request.getServerPort()+request.getContextPath()+"/";%>
<html>
<head>
   <base href="<%=basePath%>">
   <meta charset="utf-8">
   <script src="jquery/jquery-3.5.0.min.js"></script>
   <script src="Echarts/echarts.min.js"></script>
   <script>
      $(function () {
         getCharts()
      })
      function getCharts() {
         var myChart=echarts.init(document.getElementById("main"));
         $.ajax({
            url:"workbench/chart/transaction/getCharts.do",
            type:"get",
            dataType:"json",
            success:function (data) {
                  var option = {
                     title: {
                        text: '交易漏斗图',
                        subtext: '各阶段的交易数量'
                     },
                     tooltip: {
                        trigger: 'item',
                        formatter: "{a} <br/>{b} : {c}%"
                     },
                     toolbox: {
                        feature: {
                           dataView: {readOnly: false},
                           restore: {},
                           saveAsImage: {}
                        }
                     },
                     legend: {
                        data: ['展现','点击','访问','咨询','订单']
                     },
                     series: [
                        {
                           name:'漏斗图',
                           type:'funnel',
                           left: '10%',
                           top: 60,
                           //x2: 80,
                           bottom: 60,
                           width: '80%',
                           // height: {totalHeight} - y - y2,
                           min: 0,
                           max: data.total,
                           minSize: '0%',
                           maxSize: '100%',
                           sort: 'descending',
                           gap: 2,
                           label: {
                              show: true,
                              position: 'inside'
                           },
                           labelLine: {
                              length: 10,
                              lineStyle: {
                                 width: 1,
                                 type: 'solid'
                              }
                           },
                           itemStyle: {
                              borderColor: '#fff',
                              borderWidth: 1
                           },
                           emphasis: {
                              label: {
                                 fontSize: 20
                              }
                           },
                           data:data.dataList
                        }
                     ]
                  };
               myChart.setOption(option);
            }
         })
      }
   </script>
</head>
<body>
   <div id="main" style="width: 600px;height:400px;"></div>
</body>
</html>