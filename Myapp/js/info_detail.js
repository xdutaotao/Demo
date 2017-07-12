/**
 * Created by lijianhui on 2016/12/16.
 */
$(".test").html(window.screen.height);
$(".test2").html($("body").height());
alert(window.screen.height)
alert($("body").height());
//    获取URL
var url = window.location.href; //获取url中"?"符后的字串
var arr = url.split("=");
var QuotationID=arr[1];
// console.log(QuotationID)
//   数据请求
window.onload=function () {
    var Infoparam = {
        "MethodName":"getInfoTitleDetail",
        "ServiceName":"Shcem.Inform.ServiceContract.IQueryInfoService",
        "Params":"{\"QuotationID\":"+QuotationID+"}"
    };
    jsonAjax(Infoparam, Infocallback, Infoerrorback);
    function Infocallback(data) {
        var obj = JSON.parse(data.DATA);
        // console.log(obj)
        var content=decodeURIComponent(obj.InfoContent);//转义
        var DetailContent=html_decode(content);
        // console.log(DetailContent)
        $(".detail-content").html(DetailContent);
        var str=obj.Date.substr(5,11);//分割字符串
        $(".info-time").html(str);
        $("h3").html(obj.InfoTitle);
        // for(var i=0;i<$(".detail-content img").length;i++){
        //     console.log($(".detail-content img")[i].src)
        // }
       console.log($(".detail-content img").attr("src"))
    }
    function Infoerrorback(data) {
        webToast("网络请求失败","top",1000);
    }
}
