/**
 * Created by lijianhui on 2016/12/24.
 */
function GetQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null)return unescape(r[2]);
    return null;
}
var LeadesID = GetQueryString("ID")


$(".havequality").hide();//可售量默认隐藏
$(".smallquality").hide();//最小交收量默认隐藏
//获取页面数据
var Sellorderparam = {
    "MethodName": "GetLeadsDetailInfo",
    "ServiceName": "Shcem.Trade.ServiceContract.ILeadsService",
    "Params": "{\"LeadsID\":" + LeadesID + "}"
}
jsonAjax(Sellorderparam, Sellordercallback, Sellordererrorback);
function Sellordercallback(data) {
    var sellobj = JSON.parse(data.DATA);
    $(".isShow").hide()//默认隐藏
    $(".isShow").hide();
    if (sellobj.FormatSettlementMethod == GOODS_DELIVERY) {//如果是中石化现货配送
        $(".isShow").hide();
        $(".isHide").hide()
        var mallcontent = ""
        for (var i = 0; i < sellobj.TempPriceList.length; i++) {
            $(".isHide").show()
            mallcontent += '<li class="col-50 text-center" style="float: left;list-style: none;padding-right: 20px;padding-bottom:                      5px;">' + sellobj.TempPriceList[i].FormatAreaPrice + '</li>'
        }
    } else {
        $(".isHide").hide()
        $(".isShow").show()
    }
    //如果是现货 保证金比例和到货日期隐藏
    if (sellobj.FormatGoodsType == "现货") {
        $(".mall-warehouseGrop").parent(".row").hide();//仓库组隐藏
        $(".mall-sure").parent(".row").hide();//保证金隐藏
        $(".mall-chargedate").parent(".row").hide();//到货日期隐藏
        if (sellobj.FormatSourcePlaceType == "国产") {//现货进口
            $(".yellow-bg").hide();
            $(".mall-forwarder").parent(".row").hide();//货代隐藏
        } else {
            $(".blue-bg").hide();
        }
    } else if (sellobj.FormatGoodsType == "预售") {
        if (sellobj.FormatSourcePlaceType == "国产") {//预售国产
            $(".yellow-bg").hide();
            $(".mall-forwarder").parent(".row").hide();//货代隐藏
            $(".mall-warehouseGrop").parent(".row").hide();//仓库组隐藏
        } else if (sellobj.FormatSourcePlaceType == "进口") {//预售进口
            $(".blue-bg").hide();
            if (sellobj.WHAddress == "") {//预售仓库组
                $(".mall-warehouse").parent(".row").hide()//仓库地址隐藏
                $(".mall-placename").parent(".row").hide()//仓库名称隐藏
                $(".mall-place").parent(".row").hide()//交货地隐藏
                $(".mall-phone").parent(".row").hide()//仓库联系人-电话
            } else {
                $(".mall-warehouseGrop").parent(".row").hide();//仓库组隐藏
            }
        }
    }
    if (sellobj.FormatLeadsStatus == "失效" || sellobj.FormatLeadsStatus == "撤销" || sellobj.FormatLeadsStatus == "成交") {
        $(".mallcontact,.mallcontact2").hide();
        $(".mallcontact3").show();
    } else if (sellobj.FormatLeadsStatus == "有效") {
        $(".mallcontact2,.mallcontact3").hide();
        $(".mallcontact").show();
    }
//页面赋值
    $(".cus-tag").html(sellobj.FormatGoodsType);//现货or预售
    $(".cus-way").html(sellobj.FormatSourcePlaceType);//国产or进口
    $(".cus-category").html(sellobj.FormatCategoryBrand);//
    $(".mall-address").html(sellobj.SourcePlaceName);//产地
    $(".mall-pack").html(sellobj.FormatPackageStandard);//包装
    $(".mall-kind").html(sellobj.FormatPalletType);//托盘种类
    $(".mall-forwarder").html(sellobj.FormatCargoAgent);//货代
    $(".mall-warehouseGrop").html(sellobj.WHGruopName);//仓库组
    $(".mall-quality").html(sellobj.TotalWeight + "吨");//总量
    $(".mall-unit").html(sellobj.TradeUnitNumber + "吨/批");//交易单位
    $(".mall-price").html(sellobj.Price + "元/吨");//价格
    $(".mall-place").html(sellobj.DeliveryPlace);//交货地
    $(".mall-warehouse").html(sellobj.StoreHouseFN);//仓库地址
    $(".mall-phone").html(sellobj.StoreContactName);//仓库联系人及电话
    $(".mall-way").html(sellobj.FormatSettlementMethod);//交收方式
    $(".mall-date").html(sellobj.FormatDeliveryDate);//交收时间
    $(".mall-orderdate ").html(sellobj.FormatCreateDate);//报盘时间
    $(".mall-charge").html(sellobj.FormatExtraLogisticsCost + "元/吨");//买家提货附加费
    $(".mall-sure").html(sellobj.FormatDepositRate);//保证金比例
    $(".mall-chargedate").html((sellobj.FormatDeliveryStartDate) + " - " + (sellobj.FormatDeliveryEndDate));//到货日期
    $(".price-input input").val(sellobj.Price);
    $(".have-input input").val(sellobj.Quantity);
    $(".small-input input").val(sellobj.MinQuantity);
    $(".mall-qualified").html(sellobj.FormatConformProduct)//货物质量
    $(".mall-smallcharge").html(sellobj.FormatPettyCost + "元/吨")
    $(".mall-smallrange").html("每笔交收量" + sellobj.FormatPettyCostRange + "吨")
    $(".mall-moderange").html(mallcontent)
    //处理小数点
    if (sellobj.TradeUnitNumber.toString().indexOf(".") == -1) {
        HaveQuantity = sellobj.Quantity * sellobj.TradeUnitNumber;
        SmallQuanity = sellobj.MinQuantity * sellobj.TradeUnitNumber;
        $(".mall-haveWeight").html(($(".have-input input").val() * sellobj.TradeUnitNumber) + "吨")
        $(".mall-smallWeight").html(($(".small-input input").val() * sellobj.TradeUnitNumber) + "吨")
        $(".have-input input").bind('input porpertychange', function () {
            $(".mall-haveWeight").html(($(this).val() * sellobj.TradeUnitNumber) + "吨")
        });
        $(".small-input input").bind('input porpertychange', function () {
            $(".mall-smallWeight").html(($(this).val() * sellobj.TradeUnitNumber) + "吨")
        });
    } else {
        var Lenth = sellobj.TradeUnitNumber.toString().split(".")[1].length;
        console.log(Lenth)
        HaveQuantity = sellobj.Quantity * (sellobj.TradeUnitNumber * Math.pow(10, Lenth + 1)) / Math.pow(10, Lenth + 1);
        SmallQuanity = sellobj.MinQuantity * (sellobj.TradeUnitNumber * Math.pow(10, Lenth + 1)) / Math.pow(10, Lenth + 1);
        $(".mall-haveWeight").html(($(".have-input input").val() * (sellobj.TradeUnitNumber * Math.pow(10, Lenth + 1))) / Math.pow(10, Lenth + 1) + "吨")
        $(".mall-smallWeight").html(($(".small-input input").val() * (sellobj.TradeUnitNumber * Math.pow(10, Lenth + 1))) / Math.pow(10, Lenth + 1) + "吨")
        $(".have-input input").bind('input porpertychange', function () {
            $(".mall-haveWeight").html(($(this).val() * (sellobj.TradeUnitNumber * Math.pow(10, Lenth + 1))) / Math.pow(10, Lenth + 1) + "吨")
        });
        $(".small-input input").bind('input porpertychange', function () {
            $(".mall-smallWeight").html(($(this).val() * (sellobj.TradeUnitNumber * Math.pow(10, Lenth + 1))) / Math.pow(10, Lenth + 1) + "吨")
        });
    }
    $(".mall-have").html(sellobj.Quantity + "批" + "/" + HaveQuantity + "吨")//可售量
    $(".mall-small").html(sellobj.MinQuantity + "批" + "/" + SmallQuanity + "吨");//最小交收量
    function getPrice(data) {
        var Priceparam = {
            "MethodName": "LeadsPriceCheck",
            "ServiceName": "Shcem.Trade.ServiceContract.ILeadsService",
            "Params": "{\"BrandID\":" + sellobj.BrandID + ",\"SourcePlaceID\":" + sellobj.CategoryLeafID + ",\"GoodsType\":" + sellobj.GoodsType + ",\"Pirce\":" + sellobj.Price + "}"
        }
        jsonAjax(Priceparam, Pricecallback, Priceerrorback);
        function Pricecallback(data) {
            var Priecobj = JSON.parse(data.DATA);
            console.log(Priecobj)
            if (Priecobj == 1) {
                var priceval = escape($(".price-input input").val());
                var haveval = escape($(".have-input input").val());
                var smallval = escape($(".small-input input").val());
                window.location.href = "change-inquiry.html?Priceval=" + priceval + "&Haveval=" + haveval + "&Smallval=" + smallval + "&LeadesID=" + LeadesID;
                console.log("change-inquiry.html?Priceval=" + priceval + "&Haveval=" + haveval + "&Smallval=" + smallval + "&LeadesID=" + LeadesID)
//            if(sellobj.FormatGoodsType=="现货"){
//            $(".mask").show();
//            popTipShow.confirm('','当前价格超出30日平均价格上下10%，是否继续发盘？',['确 定','取 消'],function(e){
//                this.hide();
//                $(".mask").hide();
//                var button = $(e.target).attr('class');
//                if(button == 'ok'){
//                    var priceval=escape($(".price-input input").val());
//                    var haveval=escape($(".have-input input").val());
//                    var smallval=escape($(".small-input input").val());
//
//                    window.location.href="change-inquiry.html?Priceval="+priceval+"&Haveval="+haveval+"&Smallval="+smallval+"&LeadesID="+LeadesID;
// console.log("change-inquiry.html?Priceval="+priceval+"&Haveval="+haveval+"&Smallval="+smallval+"&LeadesID="+LeadesID)
//                    // window.nativeObj.finish();
//                    // window.location.href="mall_detail?Sure="+0;
//                }
//            })
//            }else{
//
//            }

            }
        }

        function Priceerrorback(data) {
            console.log(data);
        }
    }

//点击撤销
    $(".revocation").click(function () {
        $(".mask").show();
        popTipShow.confirm('', '确定撤销卖盘', ['确 定', '取 消'], function (e) {
            this.hide();
            $(".mask").hide();
            var button = $(e.target).attr('class');
            if (button == 'ok') {
                var Reparam = {
                    "MethodName": "CancelLeads",
                    "ServiceName": "Shcem.Trade.ServiceContract.ILeadsService",
                    "Params": "{\"leadsID\":" + LeadesID + ",\"userCode\":" + sellobj.UserCode + "}"
                }
                console.log(Reparam);
                jsonAjax(Reparam, Recallback, Reerrorback);
                function Recallback(data) {

                    if (/(iPhone|iPad|iPod|iOS)/i.test(navigator.userAgent)) {  //判断iPhone|iPad|iPod|iOS

                        window.location.href = "mall_detail?Item=" + 0;
                    } else if (/(Android)/i.test(navigator.userAgent)) {   //判断Android
                        //alert(navigator.userAgent);
                        window.nativeObj.finish();
                    } else {  //pc

                    }
                    ;
                }

                function Reerrorback(data) {
                    webToast("撤盘失败", "middle", 1000);
                }
            }
        })
    })
    //点击确定
    $(".mallcontact2 .row .button").click(function () {
        if ($(".price-input input").val() == sellobj.Price && $(".have-input input").val() == sellobj.Quantity && $(".small-input input").val() == sellobj.MinQuantity) {

            webToast("没有任何修改", "middle", 1000);
            // }else{
            //
            //     var Param={"MethodName":"chkAuthByTraderID","ServiceName":"shcem.common.service.ICommonMgrService","Params":"{\"brandID\":"+sellobj.BrandID+",\"traderID\":"+sellobj.TraderID+",\"cateID\":"+sellobj.CategoryLeafID+",\"tradeRole\":0,\"tmplID\":\"1\"}"}
            //     console.log(Param);
            //     jsonAjax(Param,callback,errorback);
            //     function callback(data) {
            //         console.log(data.CODE)
            //         if(data.CODE.indexOf("MSG00000")>=0){
            //             var serviceObj=JSON.parse(data.DATA);
            //             console.log(serviceObj.auth);
            //             if(serviceObj.auth==true){
            //                 getPrice();
            //             }else{
            //
            //             }
            //
            //         } else {
            //             webToast("没有买卖权限","middle",1000);
            //         }
            //     }
            //     function errorback(data) {
            //         console.log(data);
            //     }
        } else if ($(".small-input input").val() * sellobj.TradeUnitNumber > $(".have-input input").val()) {
            popTipShow.alert('信息', '最小交收量不可大于数量', ['确定'],
                function (e) {
                    $(".mask").show()
                    //callback 处理按钮事件
                    var button = $(e.target).attr('class');
                    if (button == 'ok') {
                        //按下确定按钮执行的操作
                        //todo ....
                        this.hide();
                        $(".mask").hide();
                    }
                }
            );
        } else {
            getPrice();
        }
    })
    $(".mallcontact3 .row .button").click(function () {//点击返回
        window.location.href = "mall_detail?Back=" + 0;
        window.nativeObj.finish();
    })
}
function Sellordererrorback() {
    // console.log(data)
    webToast("网络请求失败", "middle", 1000);
}
//input默认隐藏
$(".small-input").hide()
$(".price-input").hide();
$(".have-input").hide();
//点击编辑
$(".redact").click(function () {
    $(".havequality").show();
    $(".smallquality").show();
    $(".price-input").show();
    $(".mall-price").hide();
    $(".mallcontact").hide();
    $(".mallcontact2").show();
    $(".have-input").show();
    $(".small-input").show()
    $(".mall-have").hide();
    $(".mall-small").hide()
})

