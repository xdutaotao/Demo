/**
 * Created by lijianhui on 2017.2.10.
 */

    

var theCategoryIDNews = ["1775","1776","1777"]; //化交资讯-全部
var theCategoryIDNewsMarket = ["1775"];         //化交资讯-市场动态
var theCategoryIDNewsFactory = ["1776"];        //化交资讯-工厂价格
var theCategoryIDNewsDevice = ["1777"];         //化交资讯-装置
var theCategoryIDData = ["1779","1780"];        //数据中心
var theCategoryIDDataProduce = ["1779"];        //数据中心-生产统计
var theCategoryIDDataImport = ["1780"];         //数据中心-进出口
var theCategoryIDDailyDeal = ["1785"];          //每日成交
var theCategoryIDSurvey = ["1778"];             //下游调研
var theCategoryIDLecture = ["1783"];            //化交讲堂
var theCategoryIDReportTopic = ["1782"];        //化交报告-专题


// uat
//var theInfoImageCategoryID = 1880;              //咨询图片ID  uat:1880,生产:1896
//var downloadUrl = "https://fms.uat.shcem.com/mapi/file/dynamicimage?id=";    //资讯图片url   uat
//var theCategoryIDHotSpot = ["1887"];            //热点访谈 uat-1887   生产-1918
//var theCategoryIDReportDaily = ["1888"];        //化交报告-日报 uat-1888 生产-1935
//var theCategoryIDReportWeek = ["1889"];        //化交报告-日报 uat-1889 生产-1936
//var memberUrl="https://scdn.uat.shcem.com/assetsPublic/img/pdf.png";  //企业认证图片

//生产
 var theInfoImageCategoryID = 1896;               //咨询图片ID  uat:1880,生产:1896
 var downloadUrl = "https://fms.shcem.com/mapi/file/dynamicimage?id=";    //资讯图片url   生产
 var theCategoryIDHotSpot = ["1918"];             //热点访谈 uat-1887   生产-1918
 var theCategoryIDReportDaily = ["1935"];            //化交报告-日报 uat-1888 生产-1935
 var theCategoryIDReportWeek = ["1936"];             //化交报告-日报 uat-1889 生产-1936



//获取K线图数据数组中最小值
var minKChartData = "";

//uat 服务地址
 serviceURL="https://appnativepost.uat.shcem.com/shcem";
//生产
//serviceURL="https://appnativepost.shcem.com/shcem";

 //版本
Version="Vlatest";

