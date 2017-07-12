/**
 * Created by lijianhui on 2016/12/24.
 */
// function GetQueryString(name)
// {
//     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
//     var r = window.location.search.substr(1).match(reg);
//     if(r!=null)return  unescape(r[2]); return null;
// }
// var LeadsID=GetQueryString("ID")
var  url=window.location.href;
var arr=url.split("&JSON=");
var LeadsID=arr[0].split("ID=")[1];
var JN=JSON.parse(decodeURIComponent(arr[1].split("&token")[0]));
console.log(JN);
$(".mall-inquiry-price").html(JN.Enquiry_Price+"元/吨");//询盘价格
$(".mall-order-price").html(JN.Price+"元/吨");//卖盘价格
// $(".mall-people").html(JN.EnquiryUser)//交易员
$(".mall-place").html(JN.Address)//仓库地址
$(".mall-offer-price").parent(".row").hide();//还盘价格默认隐藏
if(JN.CounterOfferPrice > 0)
{
    $(".mall-offer-price").parent(".row").show();
    $(".mall-offer-price").html(JN.CounterOfferPrice+"元/吨")
}

var Inquiryparam = {
    "MethodName":"GetLeadsDetailInfo",
    "ServiceName":"Shcem.Trade.ServiceContract.ILeadsService",
    "Params":"{\"LeadsID\":"+LeadsID+"}"
}
jsonAjax(Inquiryparam,Inquirycallback,Inquiryerrorback);
function Inquirycallback(data) {
    var inquiryobj=JSON.parse(data.DATA);
    //如果是现货 保证金比例和到货日期隐藏
    if(inquiryobj.FormatGoodsType == "现货")
    {
        $(".mall-sure").parent(".row").hide();//保证金隐藏
        $(".mall-chargedate").parent(".row").hide();//到货日期隐藏
        $(".mall-warehouseGrop").parent(".row").hide();//仓库组隐藏
        if(inquiryobj.FormatSourcePlaceType == "国产")
        {//现货进口
            $(".mall-forwarder").parent(".row").hide();//货代隐藏
            $(".yellow-bg").hide();
        }else{
            $(".blue-bg").hide()
        }
    }else if(inquiryobj.FormatGoodsType == "预售")
    {
        if(inquiryobj.FormatSourcePlaceType == "国产")
        {//预售国产
            $(".mall-forwarder").parent(".row").hide();//货代隐藏
            $(".mall-warehouseGrop").parent(".row").hide();//仓库组隐藏
            $(".yellow-bg").hide();
        }else if(inquiryobj.FormatSourcePlaceType == "进口")
        {//预售进口
            $(".blue-bg").hide();
            if(inquiryobj.WHAddress == "")
            {//预售仓库组
                $(".mall-place").parent(".row").hide()//仓库地址隐藏
                $(".mall-placename").parent(".row").hide()//仓库名称隐藏
                $(".mall-order-place").parent(".row").hide()//交货地隐藏
                $(".mall-phone").parent(".row").hide()//仓库联系人-电话
                $(".blue-bg").hide()
            }else{
                $(".mall-warehouseGrop").parent(".row").hide();//仓库组隐藏
            }
        }
    }
//页面赋值
    $(".cus-tag").html(inquiryobj.FormatGoodsType);//现货or预收
    $(".cus-way").html(inquiryobj.FormatSourcePlaceType);//进口or国产
$(".mall-people").html(inquiryobj.UserName)//交易员
    $(".cus-category").html((inquiryobj.CategoryLeafName)+"-"+(inquiryobj.BrandName));//
    $(".mall-kind").html(inquiryobj.FormatPalletType);//托盘种类
    $(".mall-forwarder").html(inquiryobj.FormatCargoAgent);//货代
    $(".mall-warehouseGrop").html(inquiryobj.WHGruopName);//仓库组
    $(".mall-address").html(inquiryobj.SourcePlaceName);//产地
    $(".mall-total").html(inquiryobj.TotalWeight+"吨");//总量
    $(".mall-have").html(inquiryobj.TotalAvailableWeight+"吨");//可售量
    $(".mall-small").html(inquiryobj.MinWeight+"吨");//最小交收量
    $(".mall-unit").html(inquiryobj.TradeUnitNumber+"吨/批");//交易单位
    $(".mall-order-place").html(inquiryobj.DeliveryPlace);//交货地
    $(".mall-phone").html(inquiryobj.StoreContactName);//仓库联系人及电话
    $(".mall-way").html(inquiryobj.FormatSettlementMethod);//交收方式
    $(".mall-date").html(inquiryobj.FormatDeliveryDate);//交收时间
    $(".mall-orderdate ").html(inquiryobj.FormatDeliveryStartDate+"-"+inquiryobj.FormatDeliveryEndDate);//报盘时间
    $(".mall-charge").html(inquiryobj.FormatExtraLogisticsCost+"元/吨");//买家提货附加费
    $(".mall-sure").html(inquiryobj.FormatDepositRate);//保证金比例
    $(".mall-chargedate").html((inquiryobj.FormatDeliveryStartDate)+"-"+(inquiryobj.FormatDeliveryEndDate));//到货日期
    // console.log(inquiryobj.FormatDeliveryPlace);
}
function Inquiryerrorback(data) {
    webToast("网络请求失败","middle",1000);
}