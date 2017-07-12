/**
 * Created by lijianhui on 2016/12/23.
 */
//获取URL
// function GetQueryString(name)
// {
//     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
//     var r = window.location.search.substr(1).match(reg);
//     if(r!=null)return  unescape(r[2]); return null;
// }

// var OrderID=GetQueryString("ID")
// console.log(OrderID);
(function ($) {


    var url = window.location.href;
    var arr = url.split("&JSON=");
    var OrderID = arr[0].split("ID=")[1];
    var JN = JSON.parse(decodeURIComponent(arr[1].split("&token")[0]));
// console.log(JN);
    $(".mall-loan").html((JN.Amount * 10) / 10 + "元");//贷款总额
    $(".mall-poundage").html(JN.BuyFee + "元");//手续费
    $(".mall-noorder").html((JN.Weight - JN.DeliveryWeight) + "吨");//未交收量
    $(".mall-state").html(JN.TradeStatusShow);//成交状态

    var Tradeparam = {
        "MethodName": "GetOrderDetailInfo",
        "ServiceName": "Shcem.Trade.ServiceContract.IOrderService",
        "Params": "{\"OrderID\":" + OrderID + "}"
    }
    jsonAjax(Tradeparam, Tradecallback, Tradeerrorback);
    function Tradecallback(data) {
        var Tradeobj = JSON.parse(data.DATA);
        $(".isHide").hide()//默认隐藏
        $(".isShow").hide();
        //中石化现货配送
        if (Tradeobj.SettlementMethodShow == GOODS_DELIVERY) {//如果是中石化现货配送
            $(".isHide").show()
            var mallcontent = ""
            for (var i = 0; i < Tradeobj.TempPriceList.length; i++) {
                mallcontent += '<li class="col-50 text-center" style="float: left;list-style: none;padding-right: 20px;padding-bottom:                      5px;">' + Tradeobj.TempPriceList[i].FormatAreaPrice + '</li>'
            }
        } else {
            $(".isHide").hide()
            $(".isShow").show()
        }
        if (Tradeobj.GoodsTypeShow == "现货") {
            $(".mall-sure").parent(".row").hide();//保证金隐藏
            $(".mall-chargedate").parent(".row").hide();//到货日期隐藏
            $(".mall-warehouseGrop").parent(".row").hide();//仓库组隐藏
            if (Tradeobj.SourcePlaceTypeShow == "国产") {//现货进口
                $(".yellow-bg").hide();
                $(".mall-forwarder").parent(".row").hide();//货代隐藏
            } else {
                $(".blue-bg").hide();
            }
        } else if (Tradeobj.GoodsTypeShow == "预售") {
            if (Tradeobj.SourcePlaceTypeShow == "国产") {//预售国产
                $(".yellow-bg").hide();
                $(".mall-forwarder").parent(".row").hide();//货代隐藏
                $(".mall-warehouseGrop").parent(".row").hide();//仓库组隐藏
            } else if (Tradeobj.SourcePlaceTypeShow == "进口") {//预售进口
                $(".blue-bg").hide();
                if (Tradeobj.WHAddress == "") {//预售仓库组
                    $(".mall-warehouse").parent(".row").hide()//仓库地址隐藏
                    $(".mall-placename").parent(".row").hide()//仓库名称隐藏
                    $(".mall-orderplace").parent(".row").hide()//交货地隐藏
                    $(".mall-phone").parent(".row").hide()//仓库联系人-电话
                } else {
                    (".mall-warehouseGrop").parent(".row").hide();//仓库组隐藏
                }
            }
        }
        //判断单位是否含有小数点
        if (Tradeobj.TradeUnitNumber.toString().indexOf(".") == -1) {
            $(".mall-small").html(Tradeobj.MinQuantity + "批/" + Tradeobj.MinQuantity * Tradeobj.TradeUnitNumber + "吨");//最小交收量
        } else {
            var Lenth = Tradeobj.TradeUnitNumber.toString().split(".")[1].length;
            // console.log(Lenth)
            $(".mall-small").html(Tradeobj.MinQuantity + "批/" + Tradeobj.MinQuantity * (Tradeobj.TradeUnitNumber * Math.pow(10, Lenth + 1)) / Math.pow(10, Lenth + 1) + "吨");//最小交收量
        }
//页面元素赋值
        $(".cus-tag").html(Tradeobj.GoodsTypeShow);
        $(".cus-way").html(Tradeobj.SourcePlaceTypeShow);
        $(".cus-category").html((Tradeobj.CategoryShow) + "-" + (Tradeobj.BrandShow));
        $(".mall-kind").html(Tradeobj.PalletTypeShow);//托盘种类
        $(".mall-forwarder").html(Tradeobj.FormatCargoAgent)//货代
        $(".mall-warehouseGrop").html(Tradeobj.WHGruopName)//仓库组
        $(".mall-trade").html(OrderID);//成交号
        $(".mall-address").html(Tradeobj.SourcePlaceShow);//产地
        $(".mall-pack").html(Tradeobj.PackageStandardShow);//包装
        $(".mall-number").html(Tradeobj.Quantity + "批/" + Tradeobj.Weight + "吨");//数量
        $(".mall-unitnumber").html(Tradeobj.TradeUnitNumber + "吨/批")//交易单位
        $(".mall-qualified").html(Tradeobj.FormatConformProduct);//货物质量
        $(".mall-price").html(Tradeobj.Price + "元/吨");//价格
        $(".mall-warehouse").html(Tradeobj.WHAddress);//仓库地址
        $(".mall-placename").html(Tradeobj.WareHouseFN);//仓库名称
        $(".mall-orderplace").html(Tradeobj.StoreHouseShow);//交货地
        $(".mall-phone").html(Tradeobj.WareHouseContactName);//仓库联系人及电话
        $(".mall-way").html(Tradeobj.SettlementMethodShow);//交收方式
        $(".mall-date").html(Tradeobj.DelievryDateShow);//交货日期
        $(".mall-charge").html(Tradeobj.ExtraLogisticsCost + "元/吨");//买家提货附加费
        $(".mall-chargedate").html(Tradeobj.DeliveryStartDate.split(" ")[0].split("-").join("/") + "-" + Tradeobj.DelievryDateShow);//提货日期
        $(".mall-sure").html(Tradeobj.BuyDepositRateShow);//保证金
        $(".mall-forwarder").html(Tradeobj.FormatCargoAgent)//货代
        $(".mall-warehouseGrop").html(Tradeobj.WHGruopName);//仓库组
        $(".mall-smallcharge").html(Tradeobj.PettyCost + "元/吨")//小单费
        $(".mall-smallrange").html("每笔交收量" + Tradeobj.PettyFloor + "-" + Tradeobj.PettyCeiling + "吨")//小单范围
        $(".mall-moderange").html(mallcontent)//配送范围
        $(".mall-freight").html();//运费总额
    }

//最小交收量 贷款总额 手续费 未交售量 成交状态
    function Tradeerrorback(data) {
        webToast("网络请求失败", "middle", 1000);
    }
})(jQuery);