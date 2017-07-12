
//调用public.js
document.write("<script  src='js/public.js'></script>");
// $.getScript('public.js',function(){
//     Public()
// });

var startDate ;
const GOODS_DELIVERY = '现货配送';

//获取当前日期的前30天。获取30或90天之内的K线图数据
function getMonthDateFromNow(days) {
    var myDate = new Date(); //获取今天日期
    myDate.setDate(myDate.getDate() - days);
    var dateArray = [];
    var dateTemp;
    var flag = 1;
    for (var i = 0; i < days; i++) {
        dateTemp = myDate.getFullYear()+"-"+(myDate.getMonth()+1)+"-"+myDate.getDate();
        dateArray.push(dateTemp);
        myDate.setDate(myDate.getDate() + flag);
    }
    startDate = dateArray[0];
}
//动态加载动画

    var boarddiv = "<div id='load' style='background:#999;width:100%;height:100%;z-index:999;position:fixed;top:0;'><div class='loading'></div></div>";

    var url = window.location.href; //获取url中"?"符后的字串
    if(url.indexOf("token")>=0){
        var arr = url.split("token=");
        window.localStorage.setItem("token",arr[1]);
    }else{
        token="UserCode";
    }
//ajax函数
 function jsonAjax(param,callback,errorback) {
 	//param['Userid'] =window.localStorage.getItem("token");//从缓存获取token
    param['Userid'] = "C1nqH97nO33qJb8a2mDrDO7IZ3h5QaoS3qgvt5gs5Wq77Pxo8fSFz1EUGlnuiq76IzCGTx5RmMbsSKm6r7gMGz8ASMRQ6/WGaCtQj+xUkTQ=";
    param['Version']=Version;
            $.ajax({
                type: "post",
                url: serviceURL,
                data: JSON.stringify({"json":param}),
                cache:true,
                asay:false,
                dataType:"json",
                beforeSend: function (request) {
                    request.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=utf-8");
                },
                success: callback,
                error: errorback
            });  
        }



        var booel=false;
// 图表函数
function commonCharts(id,booel,data1,data2){
    var myChart = echarts.init(id);
    // 指定图表的配置项和数据
    var option = {
        legend: {
        },
        toolbox: {
            show: booel,
            // feature: {
            //     dataView: {readOnly: false, show:false},
            //     magicType: {type: ['bar']},
            //     restore: {}
            // }
        },
        xAxis: {
            type: 'category',
            boundaryGap: false,
            interval:0,
            axisLabel:{
                rotate:45 //刻度旋转45度角
            },
            data: data1
        },
        grid: { // 控制图的大小，调整下面这些值就可以，
            x:50,
            y:50,
            x2:5,
            y2:80// y2可以控制 X轴跟Zoom控件之间的间隔，避免以为倾斜后造成 label重叠到zoom上
        },
        yAxis: {
            type: 'value',
            min : minKChartData,
            splitNumber:2
            // interval:0
        },
        series: [{
            type: 'line',
            data: data2
        }]
    };
// 使用刚指定的配置项和数据显示图表。
      myChart.setOption(option);
   }
//页面数据解码
var html_decode = function(str) {
    var s = "";
    if (str.length == 0) return "";
    s = str.replace(/&amp;/g, "&");
    s = s.replace(/&lt;/g, "<");
    s = s.replace(/&gt;/g, ">");
    s = s.replace(/&nbsp;/g, " ");
    s = s.replace(/&#39;/g, "\'");
    s = s.replace(/&quot;/g, "\"");
    s = s.replace(/<br\/>/g, "\n");
    return s;
}

/**
 * 解析URL获取参数
 * @param name
 * @returns {null}
 * @constructor
 */
function GetQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null)return unescape(r[2]);
    return null;
}

