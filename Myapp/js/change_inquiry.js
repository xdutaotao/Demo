/**
 * Created by lijianhui on 2017/1/4.
 */
var url=window.location.href;
var LeadesID=url.split("&LeadesID=")[1]
var smallval=url.split("&LeadesID=")[0].split("&Smallval=")[1]
var haveval=url.split("&LeadesID=")[0].split("&Smallval=")[0].split("&Haveval=")[1];
var priceval=url.split("&LeadesID=")[0].split("&Smallval=")[0].split("&Haveval=")[0].split("Priceval=")[1];
var Sellorderparam= {
    "MethodName":"GetLeadsDetailInfo",
    "ServiceName":"Shcem.Trade.ServiceContract.ILeadsService",
    "Params":"{\"LeadsID\":"+LeadesID+"}"
}
jsonAjax(Sellorderparam,Sellordercallback,Sellordererrorback);
function Sellordercallback(data) {
    var sellobj=JSON.parse(data.DATA);
    //判断中石化
    $(".isShow").hide()//默认隐藏
    $(".isShow").hide();
    if(sellobj.FormatSettlementMethod == GOODS_DELIVERY)
    {//如果是中石化现货配送
        $(".isShow").hide();
        $(".isHide").hide()
        var mallcontent=""
        for(var i=0;i<sellobj.TempPriceList.length;i++){
            $(".isHide").show()
            mallcontent+='<li class="col-50 text-center" style="float: left;list-style: none;padding-right: 20px;padding-bottom:                      5px;">'+sellobj.TempPriceList[i].FormatAreaPrice+'</li>'
        }
    }else{
        $(".isHide").hide()
        $(".isShow").show()
    }

    //判断单位是否含有小数点
    var difference=(sellobj.TotalWeight*Math.pow(10,6)-sellobj.ResidualWeight*Math.pow(10,6))/Math.pow(10,6);
    if(sellobj.TradeUnitNumber.toString().indexOf(".") == -1)
    {
        mallTotal=difference+haveval*sellobj.TradeUnitNumber;
        // console.log(mallTotal)
        $(".mall-have").html(haveval+"批"+"/"+(haveval*sellobj.TradeUnitNumber)+"吨")//可售量
        $(".mall-small").html(smallval+"批"+"/"+(smallval*sellobj.TradeUnitNumber)+"吨");//最小交收量
    }else{
        var Lenth=sellobj.TradeUnitNumber.toString().split(".")[1].length;
        // console.log(Lenth)
        mallTotal=(difference*Math.pow(10,Lenth+1)+haveval*(sellobj.TradeUnitNumber*Math.pow(10,Lenth+1)))/Math.pow(10,Lenth+1);
        // console.log(mallTotal)
        $(".mall-have").html(haveval+"批"+"/"+(haveval*(sellobj.TradeUnitNumber*Math.pow(10,Lenth+1)))/Math.pow(10,Lenth+1)+"吨         ")//可售量
        $(".mall-small").html(smallval+"批"+"/"+(smallval*(sellobj.TradeUnitNumber*Math.pow(10,Lenth+1)))/Math.pow(10,Lenth+1)           +"吨");//最小交收量
    }
if(mallTotal.toString().indexOf(".") == -1)
{
    mallTotal=mallTotal;
}else{
    var Totallenth=mallTotal.toString().split(".")[1].length;
    mallTotal=(mallTotal*Math.pow(10,Totallenth+1))/Math.pow(10,Totallenth+1)
}



    //如果是现货 保证金比例和到货日期隐藏
    if(sellobj.FormatGoodsType == "现货"){
        $(".mall-warehouseGrop").parent(".row").hide();//仓库组隐藏
        $(".mall-sure").parent(".row").hide();//保证金隐藏
        $(".mall-chargedate").parent(".row").hide();//到货日期隐藏
        if(sellobj.FormatSourcePlaceType == "国产")
        {//现货进口
            $(".yellow-bg").hide();
            $(".mall-forwarder").parent(".row").hide();//货代隐藏
        }else{
            $(".blue-bg").hide();
        }
    }else if(sellobj.FormatGoodsType == "预售")
    {
        if(sellobj.FormatSourcePlaceType == "国产")
        {//预售国产
            $(".yellow-bg").hide();
            $(".mall-forwarder").parent(".row").hide();//货代隐藏
            $(".mall-warehouseGrop").parent(".row").hide();//仓库组隐藏
        }else if(sellobj.FormatSourcePlaceType == "进口")
        {//预售进口
            $(".blue-bg").hide();
            if(sellobj.WHAddress == "")
            {//预售仓库组
                $(".mall-warehouse").parent(".row").hide()//仓库地址隐藏
                $(".mall-placename").parent(".row").hide()//仓库名称隐藏
                $(".mall-place").parent(".row").hide()//交货地隐藏
                $(".mall-phone").parent(".row").hide()//仓库联系人-电话
            }else{
                $(".mall-warehouseGrop").parent(".row").hide();//仓库组隐藏
            }
        }
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
    $(".mall-quality").html(mallTotal+"吨");//总量
    $(".mall-unit").html(sellobj.TradeUnitNumber+"吨/批");//交易单位
    $(".mall-price").html(priceval+"元/吨");//价格
    $(".mall-place").html(sellobj.DeliveryPlace);//交货地
    $(".mall-warehouse").html(sellobj.StoreHouseFN);//仓库地址
    $(".mall-phone").html(sellobj.StoreContactName);//仓库联系人及电话
    $(".mall-way").html(sellobj.FormatSettlementMethod);//交收方式
    $(".mall-date").html(sellobj.FormatDeliveryDate);//交收时间
    $(".mall-orderdate ").html(sellobj.FormatCreateDate);//报盘时间
    $(".mall-charge").html(sellobj.FormatExtraLogisticsCost+"元/吨");//买家提货附加费
    $(".mall-sure").html(sellobj.FormatDepositRate);//保证金比例
    $(".mall-qualified").html(sellobj.FormatConformProduct)//货物质量
    $(".mall-smallcharge").html(sellobj.FormatPettyCost+"元/吨")//小单费
    $(".mall-smallrange").html("每笔交收量"+sellobj.FormatPettyCostRange+"吨")//小单范围
    $(".mall-moderange").html(mallcontent)//配送范围
    $(".mall-chargedate").html((sellobj.FormatDeliveryStartDate)+" - "+(sellobj.FormatDeliveryEndDate));//到货日期
    $(".price-input input").val(sellobj.Price);
    $(".have-input input").val(sellobj.Quantity);
    $(".small-input input").val(sellobj.MinQuantity);
    // $(".mall-smallWeight").html()//吨数
    // $(".mall-haveWeight").html(Math.floor(haveval*sellobj.TradeUnitNumber*100)/100+"吨")//可售吨数
    $(".mall-Totalmoney").html((priceval*sellobj.TradeUnitNumber*haveval*sellobj.DepositRate).toFixed(2)+"元");//扣款总金额
    $(".mall-Suremoney").html((priceval*sellobj.TradeUnitNumber*haveval*sellobj.DepositRate).toFixed(2)+"元");//保证金金额
    $(".mall-standard").html(sellobj.DepositRate*100+"%");//保证金收费标准
    var FirmID=sellobj.FirmID;
    console.log(FirmID);
   //获取账户余额
var moenyparam = {
        "MethodName":"queryOneFirmBanlance",
            "ServiceName":"shcem.finance.service.IBalanceMgrService",
            "Params": "{\"FIRMID\":'"+FirmID+"'}"
    }
    jsonAjax(moenyparam,moneycallback,moneyerrorback);
function moneycallback(data) {
    var moneyobj=JSON.parse(data.DATA);
    $(".mall-havemoeny").html(moneyobj.USERBALANCEFromOra+"元")

    //修改我的卖盘
    $(".redact").click(function () {//点击确定
        var Diffparam={
            "MethodName":"GetAndCheckEditLeadsExpenses",
            "ServiceName":"Shcem.Trade.ServiceContract.IExpensesService",
            "Params" :"{\"ID\":"+sellobj.ID+",\"FirmID\":"+sellobj.FirmID+",\" BrandID\":"+sellobj. BrandID+",\"TradeUnitNumber\":"+sellobj.TradeUnitNumber+",\"GoodsType\":"+sellobj.GoodsType+",\"DepositAlgr\":"+sellobj.DepositAlgr+",\"Quantity\":"+haveval+",\"Price\":"+priceval+",\"DepositRate\":"+sellobj.DepositRate+"}"
        }
        jsonAjax(Diffparam,Diffcallback,Differrorback);
        function Diffcallback(data) {
            var diffobj=JSON.parse(data.DATA);
            if(diffobj.IsBalanceEnough == 0)
            {
                popTipShow.alert('提示','当前余额不足，请确认后重新操作', ['确定'],
                    function(e){
                        $(".mask").show();
                        //callback 处理按钮事件
                        var button = $(e.target).attr('class');
                        if(button == 'ok'){
                            //按下确定按钮执行的操作
                            //todo ....
                            this.hide();
                            $(".mask").hide();
                            if (/(iPhone|iPad|iPod|iOS)/i.test(navigator.userAgent)) {  //判断iPhone|iPad|iPod|iOS

                                window.location.href="mall_detail?Sure="+0;
                            } else if (/(Android)/i.test(navigator.userAgent)) {   //判断Android
                                //alert(navigator.userAgent);
                                window.nativeObj.finish();
                            } else {  //pc

                            }
                        }
                    }
                );
            }else{
                var Sellparam={
                    "MethodName":"ModifyLeads",
                    "ServiceName":"Shcem.Trade.ServiceContract.ILeadsService",
                    "Params" :"{\"ID\":"+sellobj.ID+",\"UserCode\":"+sellobj.UserCode+",\"Quantity\":"+haveval+",\"MinQuantity\":"+smallval+",\"Price\":"+priceval+",\"TradeDeposit\":"+diffobj.TwoDecimalDeposit+",\"TradeDepositCalculate\":"+diffobj.Deposit+"}"}
                // console.log(Sellparam);
                jsonAjax(Sellparam,Sellcallback,Sellerrorback);
                function Sellcallback(data) {

                        webToast("修改成功","middle",1000);
                        if (/(iPhone|iPad|iPod|iOS)/i.test(navigator.userAgent)) {  //判断iPhone|iPad|iPod|iOS

                            window.location.href="mall_detail?Sure="+0;
                        } else if (/(Android)/i.test(navigator.userAgent)) {   //判断Android
                            //alert(navigator.userAgent);
                            window.nativeObj.finish();
                        } else {  //pc

                        }
                }
                function Sellerrorback(data) {
                    webToast("网络错误","middle",1000);
                }
            }
        }
        function Differrorback(data) {
            // console.log(data);
        }

    })
}
function moneyerrorback(data) {
    webToast("网络错误","middle",1000);
}

}
function Sellordererrorback(data) {
    webToast("网络错误","middle",1000);
}