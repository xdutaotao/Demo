/**
 * Created by lijianhui on 2016/12/23.
 */
//获取URL
function GetQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null)return unescape(r[2]);
    return null;
}
var LeadsID = GetQueryString("ID")
var EnquiryID = GetQueryString("EnquiryId")
var Tradeparam = {
    "MethodName": "GetLeadsDetailInfo",
    "ServiceName": "Shcem.Trade.ServiceContract.ILeadsService",
    "Params": "{\"LeadsID\":" + LeadsID + "}"
}
// console.log(Tradeparam);
//调取页面数据
jsonAjax(Tradeparam, Tradecallback, Tradeerrorback);
function Tradecallback(data) {
    // console.log(data);
    var Tradeobj = JSON.parse(data.DATA);
    $(".isHide").hide()//默认隐藏
    $(".isShow").hide();
    //中石化现货配送
    if (Tradeobj.FormatSettlementMethod == GOODS_DELIVERY) {//如果是中石化现货配送
        var mallcontent = ""
        for (var i = 0; i < Tradeobj.TempPriceList.length; i++) {
            $(".isHide").show()
            mallcontent += '<li class="col-50 text-center" style="float: left;list-style: none;padding-right: 20px;padding-bottom:                      5px;">' + Tradeobj.TempPriceList[i].FormatAreaPrice + '</li>'
        }
    } else {
        $(".isHide").hide()
        $(".isShow").show()
    }
    if (Tradeobj.FormatGoodsType == "现货") {
        $(".mall-warehouseGrop").parent(".row").hide();//仓库组隐藏
        $(".mall-sure").parent(".row").hide();//保证金隐藏
        $(".mall-chargedate").parent(".row").hide();//到货日期隐藏
        if (Tradeobj.FormatSourcePlaceType == "国产") {//现货国产
            $(".mall-forwarder").parent(".row").hide();//货代隐藏
        } else {
        }
    } else if (Tradeobj.FormatGoodsType == "预售") {
        if (Tradeobj.FormatSourcePlaceType == "国产") {//预售国产
            $(".mall-forwarder").parent(".row").hide();//货代隐藏
            $(".mall-warehouseGrop").parent(".row").hide();//仓库组隐藏
        } else if (Tradeobj.FormatSourcePlaceType == "进口") {//预售进口
            if (Tradeobj.WHAddress == "") {//预售仓库组不为空
                $(".mall-orderplace").parent(".row").hide()//交货地隐藏
                $(".mall-placename").parent(".row").hide()//仓库名称隐藏
                $(".mall-place").parent(".row").hide()//仓库地址隐藏
                $(".mall-phone").parent(".row").hide()//仓库联系人-电话
            } else {
                $(".mall-warehouseGrop").parent(".row").hide();//仓库组隐藏
            }
        }
    }
//调取页面数据2
    var Inquiryparam = {
        "MethodName": "GetInfo",
        "ServiceName": "Shcem.Trade.ServiceContract.IEnquiryService",
        "Params": "{\"TraderID\":" + Tradeobj.TraderID + ",\"FirmID\":" + Tradeobj.FirmID + ",\"EnquiryID\":" + EnquiryID + ",                    \"LeadsID\":" + LeadsID + "}"
    }
    jsonAjax(Inquiryparam, Inquirycallback, Inquiryerrorback);
    function Inquirycallback(data2) {
        var Inquiryobj = JSON.parse(data2.DATA);
        // console.log(Inquiryobj)
        $(".cus-way").html(Inquiryobj.SourcePlaceTypeShow)//国产或进口
        $(".mall-people").html(Inquiryobj.EnquiryUser)//交易员
        $(".mall-requireprice").html(Inquiryobj.Enquiry_Price + "元/吨");//询盘价格
        $(".mall-requirenum").html(Inquiryobj.Enquiry_Weight + "吨");//询盘总量
        $(".mall-offerprice").parent(".row").hide()//还盘价格默认隐藏
        if (Inquiryobj.CounterOfferPrice > 0) {//判断是否有还盘
            $(".mall-offerprice").parent(".row").show();
            $(".mall-offerprice").html(Inquiryobj.CounterOfferPrice + "元/吨");
        }
    }

    function Inquiryerrorback(data2) {
        webToast("网络请求失败", "middle", 1000);
    }

    //如果是现货 保证金比例和到货日期隐藏
    if (Tradeobj.FormatGoodsType == "现货") {
        $(".mall-sure").parent(".row").hide();
        $(".mall-chargedate").parent(".row").hide();
    }
//页面元素赋值
    $(".cus-tag").html(Tradeobj.FormatGoodsType);//现货
    $(".cus-category").html((Tradeobj.CategoryLeafName) + (Tradeobj.BrandName));
    $(".mall-address").html(Tradeobj.SourcePlaceName);//产地
    $(".mall-pack").html(Tradeobj.FormatPackageStandard);//包装
    $(".mall-unit").html(Tradeobj.TradeUnitNumber + "吨/批");//交易单位
    $(".mall-member").html(Tradeobj.UserName)//交易员
    $(".mall-number").html(Tradeobj.Quantity + "批/" + Tradeobj.TotalWeight + "吨");//数量
    $(".mall-small").html(Tradeobj.MinQuantity + "批/" + Tradeobj.MinWeight + "吨");//最小交收量
    $(".mall-price").html(Tradeobj.Price + "元/吨");//价格
    $(".mall-place").html(Tradeobj.WHAddress);//仓库地址
    $(".mall-placename").html(Tradeobj.StoreHouseFN);//仓库名称
    $(".mall-orderplace").html(Tradeobj.DeliveryPlace);//交货地
    $(".mall-phone").html(Tradeobj.StoreContactName);//联系人-及电话
    $(".mall-way").html(Tradeobj.FormatSettlementMethod);//交收方式
    $(".mall-charge").html(Tradeobj.ExtraLogisticsCost + "元/吨");//买家提货附加费
    $(".mall-chargedate").html(Tradeobj.FormatDeliveryStartDate);//到货日期
    $(".mall-sure").html(Tradeobj.FormatDepositRate);//保证金
    $(".mall-kind").html(Tradeobj.FormatPalletType);//托盘种类
    $(".mall-date").html(Tradeobj.FormatDeliveryEndDate);//交货日期
    $(".mall-chargedate").html((Tradeobj.FormatDeliveryStartDate) + "-" + (Tradeobj.FormatDeliveryEndDate));//到货日期
    $(".mall-forwarder").html(Tradeobj.FormatCargoAgent)//货代
    $(".mall-warehouseGrop").html(Tradeobj.WHGruopName);//仓库组
    $(".mall-moderange").html(mallcontent)
}
function Tradeerrorback(data) {
    webToast("网络请求失败", "middle", 1000);
}