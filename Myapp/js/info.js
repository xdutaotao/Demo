function GetQueryString(name)
{
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
}


var Title=GetQueryString("title");
if(Title=="InfoPrice"){
    Title="化交价格"
}else if(Title=="InfoNews"){
    Title="化交资讯"
}else if(Title=="InfoReport"){
    Title="化交报告"
}

var theProductID = 1;   //PE/PP/PVC的productID
var theProductName = "PE";  //PE/PP/PVC的名字,用于K线图传参
var thePageSize = 1;    //分页
var arrayCategoryID = ["price", theCategoryIDHotSpot, theCategoryIDReportDaily, theCategoryIDNewsMarket, theCategoryIDDataProduce,
                        theCategoryIDDailyDeal,theCategoryIDSurvey, theCategoryIDLecture];
var indexInfo;          //切换到的index(指化交资讯、数据中心、热点访谈)
var SelectNewsIndex;    //化交资讯的index
var SelectDatasIndex;   //数据中心的index
var SelectReportIndex;  //化交报告的idnex
var arrayNews = [theCategoryIDNewsMarket, theCategoryIDNewsFactory, theCategoryIDNewsDevice, theCategoryIDNews];
var arrayData = [theCategoryIDDataProduce, theCategoryIDDataImport, theCategoryIDData];
var arrayReport = [theCategoryIDReportDaily, theCategoryIDReportWeek, theCategoryIDReportTopic];


var pageIndex = 0;
var num = 10;
var pageStart = 0;
var pageEnd = 0;
var dropload;
var detailContentInfoList = '';
var imageCategoryIDDetail;


//第一次进入页面,需要调用的service
requestInfoKPIData();
requestInfoPriceData(theProductID);
requestInfoImageData(theInfoImageCategoryID, 1);
getMonthDateFromNow(30);



//头部切换
$(".info-title .row .col").click(function(){
    var ind=$(".info-title .row .col").index($(this));
    $(".info-title ul li.active").removeClass("active");
    var theProductNameArray = ["PE", "PP", "PVC"];
    theProductName = theProductNameArray[ind];
    theProductID = ind+1;
    requestInfoPriceData(theProductID);
    reloadRefreshData();
    if(indexInfo == 0) {
        requestInfoPriceData(theProductID);
    } else if(indexInfo == 1 || indexInfo == 5 || indexInfo == 6 || indexInfo == 7){
        requestInfoServiceLoadMoreData(arrayCategoryID[indexInfo],theProductID,thePageSize);
    } else if(SelectNewsIndex || SelectDatasIndex || SelectReportIndex){
        requestMoreChance();
    } else {
        requestInfoServiceLoadMoreData(arrayCategoryID[indexInfo],theProductID,thePageSize);
    }
    $(this).addClass("active");
});

//点击加号
$(".search_logo").text("+");
	$(".search_logo").click(function(){
	    $(".nav_list").slideToggle();
		$("#backdrop").toggle();
		if($(".search_logo").text()=="+"){
		$(this).text("x");
		}else{
            document.documentElement.style.overflow='hidden';
            document.body.style.overflow='hidden';
           init();
		}
	});
//+遍历-----点击+号弹出下拉框的点击
$(".nav_list ul li").click(function(){
    var ind=$(".nav_list ul li").index($(this));
    // console.log("点击了"+ind);
    $(".nav_list ul li a.active").removeClass("active");
    $(this).find("a").addClass("active");
    var a_nav=$(this).find("a").text();
	$(".nav_list").hide();
	$("#backdrop").hide();
    $(".search_logo").text("+");
    var o_nav=a_nav.split("|")[1];
    var s_nav=a_nav.split("|")[0];
    if(a_nav.indexOf(o_nav)>=0){
        //点击了化交报告的子选项
        $(".nav_list1 ul li").each(function () {
            var d_nav=$(this).find("a").text();
            if(d_nav.indexOf(o_nav)>=0){
                $(".nav_list1 ul li").find("img").hide();
                $(this).find("img").show();
            }
        });
        //点击了化交资讯的三个子选项
        $(".nav_list2 ul li").each(function () {
            var d_nav=$(this).find("a").text();
            if(d_nav.indexOf(o_nav)>=0){
                $(".nav_list2 ul li").find("img").hide();
                $(this).find("img").show();
            }
        });
        //点击了数据中心的三个子选项
        $(".nav_list3 ul li").each(function () {
            var d_nav=$(this).find("a").text();
            if(d_nav.indexOf(o_nav)>=0){
                $(".nav_list3 ul li").find("img").hide();
                $(this).find("img").show();
            }
        })
    }

    $(".find_nav span").each(function(){
        if($(this).find("a").text().indexOf(s_nav)>=0){
            indexInfo = $(".find_nav span").index($(this));
            // console.log(indexInfo)
            $(".find_nav span").find("a").removeClass("active");
            $(this).find("a").addClass("active");
            if($(this).find("a").text()=="化交价格"){
                $(".info-content-list").show();
                $(".info-content-list2").hide();
                $('.dropload-down').hide();
            }else{
                $(".info-content-list").hide();
                $(".info-content-list2").show();
                $('.dropload-down').show();
            }
            reloadRefreshData();
            if(indexInfo == 0) {
                requestInfoPriceData(theProductID);
            } else if(indexInfo == 1 || indexInfo == 5 || indexInfo == 6 || indexInfo == 7){
                requestInfoServiceLoadMoreData(arrayCategoryID[indexInfo],theProductID,thePageSize);
            } else if(SelectNewsIndex || SelectDatasIndex || SelectReportIndex){
                requestMoreChance();
            } else {
                requestInfoServiceLoadMoreData(arrayCategoryID[indexInfo],theProductID,thePageSize);
            }
        }
    });
    reloadRefreshData();
    if(ind == 0 ){
        // console.log("点击化交价格");
    } else if(ind == 2 && SelectReportIndex){
        requestInfoServiceLoadMoreData(arrayReport[SelectReportIndex],theProductID,thePageSize);
    } else if(ind == 3 && SelectNewsIndex){
        requestInfoServiceLoadMoreData(arrayNews[SelectNewsIndex],theProductID,thePageSize);
    } else if(ind == 4 && SelectDatasIndex) {
        requestInfoServiceLoadMoreData(arrayData[SelectDatasIndex],theProductID,thePageSize);
    } else {
        if(ind > 0 && ind <=7) {
            indexInfo = ind;
        } else if(ind >=8 && ind <= 10) {
            SelectReportIndex = ind - 8;
        } else if(ind >= 11 && ind <= 13){
            SelectNewsIndex = ind - 11;
        } else if(ind >= 12 ){
            SelectDatasIndex = ind - 12;
        }
        var array = ["", theCategoryIDHotSpot, theCategoryIDReportDaily, theCategoryIDNewsMarket, theCategoryIDDataProduce, theCategoryIDDailyDeal,
            theCategoryIDSurvey, theCategoryIDLecture, theCategoryIDReportDaily, theCategoryIDReportWeek, theCategoryIDReportTopic, theCategoryIDNewsMarket,
            theCategoryIDNewsFactory, theCategoryIDNewsDevice, theCategoryIDDataProduce, theCategoryIDDataImport];
        requestInfoServiceLoadMoreData(array[ind],theProductID,thePageSize);
    }

});


function init() {
    $(".search_logo").text("+");
    $(".nav_list").hide();
    $(".nav_list1").hide();
    $(".nav_list2").hide();
    $(".nav_list3").hide();
    $("#backdrop").hide();
}
//点击灰色背景关闭
$("#backdrop").click(function(){
   init();
});


//化交报告列表点击
$(".nav_list1 ul li").click(function(){
    SelectReportIndex=$(".nav_list1 ul li").index($(this));
    reloadRefreshData();
    requestInfoServiceLoadMoreData(arrayReport[SelectReportIndex],theProductID,thePageSize);
    $(".nav_list1 ul li").find("img").hide();
    $(this).find("img").show();
    init()
})

//化交资讯列表点击
$(".nav_list2 ul li").click(function(){
	SelectNewsIndex =$(".nav_list2 ul li").index($(this));
    reloadRefreshData();
    requestInfoServiceLoadMoreData(arrayNews[SelectNewsIndex],theProductID,thePageSize);
    $(".nav_list2 ul li").find("img").hide();
	$(this).find("img").show();
	init()
});
//数据中心列表点击
$(".nav_list3 ul li").click(function(){
    SelectDatasIndex=$(".nav_list3 ul li").index($(this));
    reloadRefreshData();
    requestInfoServiceLoadMoreData(arrayData[SelectDatasIndex],theProductID,thePageSize);

	$(".nav_list3 ul li").find("img").hide();
	$(this).find("img").show();
	init()
});

//滑动点击事件
$(".find_nav span").on('click', function Touchclick(){
    indexInfo = $(".find_nav span").index($(this));
    $(".find_nav span").find("a").removeClass("active");
    $(this).find("a").addClass("active");
    reloadRefreshData();
    if(indexInfo == 0) {
        requestInfoPriceData(theProductID);
    } else if(indexInfo == 1 || indexInfo == 5 || indexInfo == 6 || indexInfo == 7){
        requestInfoServiceLoadMoreData(arrayCategoryID[indexInfo],theProductID,thePageSize);
    } else if(SelectNewsIndex || SelectDatasIndex || SelectReportIndex){
        requestMoreChance();
    } else {
        requestInfoServiceLoadMoreData(arrayCategoryID[indexInfo],theProductID,thePageSize);
    }

    //如果是化交价格
    if($(this).find("a").text()=="化交价格"){
        $(".info-content-list").show();
        $(".info-content-list2").hide();
        $('.dropload-down').hide();
    }else{
        $(".info-content-list").hide();
        $(".info-content-list2").show();
        $('.dropload-down').show();
    }
    //如果是化交资讯
    if($(this).find("a").text()=="化交资讯"){
        $(".nav_list2").show();
        $("#backdrop").show();
        $(".search_logo").text("x")
    }else{
        init();
    }
    //如果是数据中心
    if($(this).find("a").text()=="数据中心"){
        $(".search_logo").text("x");
        $(".nav_list3").show();
        $("#backdrop").show();
    }else{
        $(".nav_list3").hide();
    }
    //如果是化交报告
    if($(this).find("a").text()=="化交报告"){
        $(".search_logo").text("x");
        $(".nav_list1").show();
        $("#backdrop").show();
    }else{
        $(".nav_list1").hide();
    }
    var c_nav=$(this).find("a").text();

    $(".nav_list ul li").each(function(){
        if($(this).find("a").text()==c_nav){
            $(".nav_list ul li a.active").removeClass("active");
            $(this).find("a").addClass("active");
        }
    })
});

function reloadRefreshData() {
    pageIndex = 0;
    num = 10;
    pageStart = 0;
    pageEnd = 0;
    detailContentInfoList = '';
}

function requestMoreChance() {
    if(SelectNewsIndex) {
        requestInfoServiceLoadMoreData(arrayNews[SelectNewsIndex],theProductID,thePageSize);
    } else if(SelectDatasIndex) {
        requestInfoServiceLoadMoreData(arrayData[SelectDatasIndex],theProductID,thePageSize);
    } else if(SelectReportIndex) {
        requestInfoServiceLoadMoreData(arrayReport[SelectReportIndex],theProductID,thePageSize);
    }
}

$(".info-img").on('click', function (){
    // console.log("点击了图片");
    window.location.href='info_detail.html?ID='+imageCategoryIDDetail;
});



//-----------------------------------下拉刷新 上拉加载------------------------------------------------------------------------------华丽丽的分割线
$(function() {
    dropload = $('.info-content').dropload({
        scrollArea : window,
        domUp: {
            domClass: 'dropload-up',
            domRefresh: '<div class="dropload-refresh">↓下拉刷新</div>',
            domUpdate: '<div class="dropload-update">↑释放更新</div>',
            domLoad: '<div class="dropload-load"><span class="loading"></span>加载中...</div>'
        },
        domDown: {
            domClass: 'dropload-down',
            domRefresh: '<div class="dropload-refresh">↑上拉加载更多</div>',
            domLoad: '<div class="dropload-load"><span class="loading"></span>加载中...</div>',
            domNoData: '<div class="dropload-noData">加载完成</div>'
        },
        //下拉刷新
        loadUpFn: function (me) {
            setTimeout(function () {
                requestInfoKPIData();
                requestInfoPriceData(theProductID);
                requestInfoImageData(theInfoImageCategoryID, 1);
                reloadRefreshData();
                if(indexInfo == 0) {
                    requestInfoPriceData(theProductID);
                } else if(indexInfo == 1 || indexInfo == 5 || indexInfo == 6 || indexInfo == 7){
                    requestInfoServiceLoadMoreData(arrayCategoryID[indexInfo],theProductID,thePageSize);
                } else if(SelectNewsIndex || SelectDatasIndex || SelectReportIndex){
                    requestMoreChance();
                } else {
                    requestInfoServiceLoadMoreData(arrayCategoryID[indexInfo],theProductID,thePageSize);
                }

                // 每次数据加载完，必须重置
                dropload.resetload();
            }, 1000);
        },
        //上拉加载 
        loadDownFn: function (me) {
            if (indexInfo) {
                if(indexInfo == 1 || indexInfo == 5 || indexInfo == 6 || indexInfo == 7){
                    requestInfoServiceLoadMoreData(arrayCategoryID[indexInfo], theProductID, (pageIndex + 1), me);
                } else {
                    if(SelectNewsIndex){
                        requestInfoServiceLoadMoreData(arrayNews[SelectNewsIndex], theProductID, (pageIndex + 1), me);
                    } else if(SelectDatasIndex){
                        requestInfoServiceLoadMoreData(arrayData[SelectDatasIndex], theProductID, (pageIndex + 1), me);
                    } else if(SelectReportIndex){
                        requestInfoServiceLoadMoreData(arrayReport[SelectReportIndex], theProductID, (pageIndex + 1), me);
                    }
                }
            }

        }
    });
});

//-----------------------热点访谈、化交报告、化交资讯、数据中心、每日成交、下游调研、化交讲堂等等service-----上拉加载------------------华丽丽的分割线
function requestInfoServiceLoadMoreData(categoryID, productID, pagesize, me) {
    if(me){

    } else {
        me = dropload;
    }
    var paramInfoLoadMore = {
        "MethodName":"getInformList",
        "ServiceName":"Shcem.Inform.ServiceContract.IQueryInfoService",
        "Params": JSON.stringify({
            'PageCount':10,
            'CatogoryID':categoryID,
            'page':pagesize,
            'ProductID':productID,
            'AppTypeID':5
        })
    };
    jsonAjax(paramInfoLoadMore, callBackInfoLoadMore, errorBackInfoLoadMore);
    function callBackInfoLoadMore(data) {
        pageIndex++;
        var page = 1;
        pageEnd = num * page;
        pageStart = pageEnd - num;
        if (data.CODE == "MSG00000"){
            var obj = JSON.parse(data.DATA);
            if(pageStart < obj.length){
                for(var i = pageStart; i < pageEnd; i++){
                    var item = obj[i];
                    detailContentInfoList += '<div class="row info-row-list"  id="'+item.ID+'">'+'<div class="col col-70">'+item.InfoTitle+'</div>'+
                        '<div class="col text-right">'+item.Date.substr(0,10)+'</div>'+'</div>';
                    if(obj.length < 10) {
                        if((i + 1) >= obj.length){
                            // 锁定

                            // console.log(me);

                            me.lock();
                            // 无数据
                            me.noData();
                            break;
                        }
                    } else {
                        me.unlock();
                        me.noData(false);
                    }
                }
                // 为了测试，延迟1秒加载
                setTimeout(function(){
                    $(".info-content-list2").html(detailContentInfoList);
                    $(".info-row-list").click(function () {
                        var ID=this.id;
                        window.location.href='info_detail.html?ID='+ID;
                    });
                    // 每次数据加载完，必须重置
                    me.resetload();
                    me.unlock();
                    me.noData(false);
                },0);
            }
        } else {
            me.lock();
            // 无数据
            me.noData();
            me.resetload();
        }
    }
    function errorBackInfoLoadMore(data) {
        webToast("网络请求失败","middle",1000);
    }
}
//-----------------------------------KPI数据------------------------------------------------------------------------------华丽丽的分割线
function requestInfoKPIData() {
    var paramKPI = {
        "MethodName":"getMarketQuo",
        "ServiceName":"Shcem.Inform.ServiceContract.IQueryInfoService"
    };
    jsonAjax(paramKPI,callbackKPI,errorbackKPI);
    function callbackKPI(data) {
        var obj=JSON.parse(data.DATA);
        var detailcontentKPI='';
        $.each(obj,function(i,item){
            if(item.mqWave>0){
                item.mqUP = "↑";
                detailcontentKPI += '<span class="col-25 text-center">'+item.mqType+'<br />'+'<span class="col-25 text-center cus-red">'+item.mqPrice+item.mqUP+'</span>'+'</span>';
                $(".info-list").html(detailcontentKPI);
            } else if(item.mqWave == 0) {
                item.mqUP = "-";
                detailcontentKPI += '<span class="col-25 text-center">'+item.mqType+'<br />'+'<span class="col-25 text-center gray-text-color">'+item.mqPrice+item.mqUP+'</span>'+'</span>';
                $(".info-list").html(detailcontentKPI);
            } else if(item.mqWave < 0){
                item.mqUP = "↓";
                detailcontentKPI += '<span class="col-25 text-center">'+item.mqType+'<br />'+'<span class="col-25 text-center green-color">'+item.mqPrice+item.mqUP+'</span>'+'</span>';
                $(".info-list").html(detailcontentKPI);
            }
        })
    }
    function errorbackKPI(data){
        webToast("网络请求失败","middle",1000);
    }
}
//-----------------------------------化交价格service------------------------------------------------------------------------------华丽丽的分割线
function requestInfoPriceData(ProductID) {
    var paramPrice = {
        "MethodName":"getScemQuo",
        "ServiceName":"Shcem.Inform.ServiceContract.IQueryInfoService",
        "Params":"{\"ProductID\":"+ProductID+",\"TOP\":8}"
    };
    jsonAjax(paramPrice, callbackPrice, errorbackPrice);
    function callbackPrice(data) {
        $("#backloading").hide();
        var obj = JSON.parse(data.DATA);
        var detailContentPrice = '';
        $.each(obj, function (i, item) {
            var detailDrop = '';
            var detailUp = '';
            if(item.SQDropRange>0){
                detailDrop += '<span class="cus-red">'+'+'+item.SQDropRange+'</span>'
            } else{
                detailDrop += '<span class="green-color">'+item.SQDropRange+'</span>'
            }
            if(item.SQUpRange>0){
                detailUp += '<span class="cus-red">'+'+'+item.SQUpRange+'</span>'
            } else{
                detailUp += '<span class="green-color">'+item.SQUpRange+'</span>'
            }
            detailContentPrice += '<div class="info_chat_list" id="">'+'<div class="row">'+'<div class="col">'+item.SQRank+'-'+item.SQNo+'</div>'+'<div class="col">'+
                                 '<span class="green-color">'+item.SQType+'</span>'+'<span class="cus-red">'+item.sqArea+'</span>'+'</div>'+'</div>'+
                                '<div class="row borderdown">'+'<div class="col">'+'价格：'+item.SQMinPrice+'-'+item.SQMaxPrice+'</div>'+
                                '<div class="col">'+'涨跌：'+detailDrop+'/'+detailUp+'</div>'+'</div>'+'<div class="box">'+'</div>'+'<div class="box2">'+'</div>'+
                                '</div>'+'<div id="info_chat'+i+'" style="height: 200px;display: none;margin-top: -40px;" class="close_charts">'+'</div>'
            $(".info-content-list").html(detailContentPrice);

            //点击化交价格cell,展示K线图
            $(".info_chat_list").click(function(){

                    var ind=$(".info_chat_list").index($(this));
                    var theItem = obj[ind];



                if($(this).attr('id')==""){//判断元素的id
                    $(this).attr({id:ind});//设置元素的id
                    $(this).next("div").click(function () {
                        window.location.href="chart_detail.html?ind="+ind+"&key="+theProductID;//跳转到图表详情页，并传当前第几个
                    });
//-----------------------K线图service-------------------------------------------------------------------------------------------华丽丽的分割线
                    var ID = $(this).next("div").attr('id');
                    var id = document.getElementById(ID);
                    var booel = false;

                    //K线图service请求
                    var paramKChart = {
                        "MethodName": "getScemQuoDiagram",
                        "ServiceName": "Shcem.Inform.ServiceContract.IQueryInfoService",
                        "Params": JSON.stringify({
                            'EndDate': theItem.SQDate,
                            'StartDate': startDate,
                            'Rank': theItem.SQRank,
                            'Type': theItem.SQType,
                            'ProductID': theProductID,
                            'ProductName': "",
                            'NO': theItem.SQNo
                     })
                    };
                    jsonAjax(paramKChart, callBackKChart, errorBackKChart);
                    function callBackKChart(data) {
                        if(data.CODE == "MSG00000"){
                            $(".info_chat_list").eq(ind).find($(".box")).toggle();
                            $(".info_chat_list").eq(ind).find($(".box2")).toggle();
                            $(".info_chat_list").eq(ind).next("div").toggle();

                            var obj = JSON.parse(data.DATA);
                            minKChartData = obj.ScemQuoPriceList[0];
                            for (var j = 1; j < obj.ScemQuoPriceList.length; j++) {
                                if (obj.ScemQuoPriceList[j] < minKChartData) {
                                    minKChartData = obj.ScemQuoPriceList[j];
                                }
                            }
                            commonCharts(id, booel, obj.ScemQuoDateList, obj.ScemQuoPriceList);
                        } else {
                            popTipShow.alert('提示','没有查询结果', ['确定'],
                                function(e){
                                    //callback 处理按钮事件
                                    var button = $(e.target).attr('class');
                                    if(button == 'ok'){
                                        //按下确定按钮执行的操作
                                        //todo ....
                                        this.hide();
                                        $(".info_chat_list").eq(ind).find($(".box")).show();
                                        $(".info_chat_list").eq(ind).find($(".box2")).hide();
                                        $(".info_chat_list").eq(ind).next("div").hide();
                                    }
                                }
                            );

                        }

                    }
                    function errorBackKChart(data) {
                        webToast("网络请求失败","middle",1000);
                    }
                }else {
                    $(this).attr({id:""});
                }
            })
        })
    }
    function errorbackPrice(data) {
        webToast("网络请求失败","middle",1000);
    }
}
//-----------------------热点访谈、化交报告、化交资讯、数据中心、每日成交、下游调研、化交讲堂等等service---------------------------------------华丽丽的分割线
function requestInfoServiceData(categoryID, productID, pagesize) {
    var paramInfo = {
        "MethodName":"getInformList",
        "ServiceName":"Shcem.Inform.ServiceContract.IQueryInfoService",
        "Params": JSON.stringify({
            'PageCount':10,
            'CatogoryID':categoryID,
            'page':pagesize,
            'ProductID':productID,
            'AppTypeID':5
        })
    };
    jsonAjax(paramInfo, callBackInfo, errorBackInfo);
    function callBackInfo(data) {
        $(document.body).remove(boarddiv);
        var obj = JSON.parse(data.DATA);
        var detailContentInfoList = '';
        $.each(obj, function (i, item) {
            detailContentInfoList += '<div class="row info-row-list"  id="'+item.ID+'">'+'<div class="col-70">'+item.InfoTitle+'</div>'+
                                    '<div class="col text-right">'+item.Date.substr(0,10)+'</div>'+'</div>'
            $(".info-content-list2").html(detailContentInfoList);
            $(".info-row-list").click(function () {
                var ID=this.id;
                window.location.href='info_detail.html?ID='+ID;
            })
        });

    }
    function errorBackInfo(data) {
        webToast("网络请求失败","middle",1000);
    }
}
//-----------------------资讯图片显示service----------------------------------------------------------------------------------------华丽丽的分割线
function requestInfoImageData(catogoryID, top) {
    var paramInfoImage = {
        "ServiceName": "Shcem.Inform.ServiceContract.IQueryInfoService",
        "MethodName": "getTopInformList",
        "Params": JSON.stringify({
            'ProductID': 0,
            'AppTypeID':0,
            'CatogoryID':catogoryID,
            'Top':top
        })
    };
    jsonAjax(paramInfoImage, callBackInfoImage, errorBackInfoImage);
    function callBackInfoImage(data) {
        var obj = JSON.parse(data.DATA);
        var imageUrl = downloadUrl + obj[0].FileID;
        imageCategoryIDDetail = obj[0].InfoLinkId.substr(obj[0].InfoLinkId.length - 5, 5);
        $(".info-img img").attr('src',imageUrl);
    }
    function errorBackInfoImage(data) {
        // console.log(data);
    }
}

// 遍历
$(function () {
    $(".find_nav span").each(function(){

        if($(this).find("a").text().indexOf(Title)>=0){
            indexInfo = $(".find_nav span").index($(this));
            // console.log(indexInfo)
            $(".find_nav span").find("a").removeClass("active");
            $(this).find("a").addClass("active");
            if($(this).find("a").text()=="化交价格"){
                $(".info-content-list").show();
                $(".info-content-list2").hide();
                $('.dropload-down').hide();
            }else{
                $(".info-content-list").hide();
                $(".info-content-list2").show();
                $('.dropload-down').show();
            }
            reloadRefreshData();
            if(indexInfo == 0) {
                requestInfoPriceData(theProductID);
            } else if(indexInfo == 1 || indexInfo == 5 || indexInfo == 6 || indexInfo == 7){
                requestInfoServiceLoadMoreData(arrayCategoryID[indexInfo],theProductID,thePageSize);
            } else if(SelectNewsIndex || SelectDatasIndex || SelectReportIndex){
                requestMoreChance();
            } else {
                requestInfoServiceLoadMoreData(arrayCategoryID[indexInfo],theProductID,thePageSize);
            }
        }
    });
})

