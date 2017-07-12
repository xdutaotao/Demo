//获取URL
(function ($) {
function GetQueryString(name)
{
	var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if(r!=null)return  unescape(r[2]); return null;
}
var LeadsID=GetQueryString("ID");
var Firm=GetQueryString("Firm");
var Dt=GetQueryString("Dt");
// console.log(Firm)
// console.log(Dt);
$(".mall-company").html(unescape(Firm));
// $(".mall-givedate").html(unescape(Dt.split(" ")[0]))
// 参数
	var mallparam = {
		"MethodName":"GetLeadsDetailInfo",
		"ServiceName":"Shcem.Trade.ServiceContract.ILeadsService",
		"Params":"{\"LeadsID\":"+LeadsID+"}"
	};
//	
//调用
	jsonAjax(mallparam,callback,errorback);
//调用成功
	function callback(data){
		console.log(data.CODE);
		if(data.CODE == "MSG00000"){
		var obj = JSON.parse(data.DATA);
		$(".isShow").hide()//默认隐藏
		$(".isShow").hide();
		if(obj.FormatSettlementMethod == GOODS_DELIVERY)
		{//如果是中石化现货配送
                $(".isShow").hide();
                $(".isHide").hide()
                var mallcontent=""
                for(var i=0;i<obj.TempPriceList.length;i++){
                    $(".isHide").show()
                    mallcontent+='<li class="col-50 text-center" style="float: left;list-style: none;padding-right: 20px;padding-bottom:                      5px;">'+obj.TempPriceList[i].FormatAreaPrice+'</li>'
                }
		}else{
                $(".isHide").hide()
                $(".isShow").show()
        }

		if(obj.FormatGoodsType == "现货")
		{//现货
				$(".mall-sure").parent(".row").hide();//保证金隐藏
				$(".mall-date").parent(".row").hide();//日期隐藏
				$(".mall-warehouseGrop").parent(".row").hide();//仓库组隐藏
		if(obj.FormatSourcePlaceType=="国产"){//现货进口
				$(".yellow-bg").hide();
			    $(".mall-forwarder").parent(".row").hide();//货代隐藏
				}else{
				$(".blue-bg").hide();
				}
		}else if(obj.FormatGoodsType=="预售"){
			if(obj.FormatSourcePlaceType=="国产"){//预售国产
                	$(".yellow-bg").hide();
					$(".mall-forwarder").parent(".row").hide();//货代隐藏
					$(".mall-warehouseGrop").parent(".row").hide();//仓库组隐藏
		}else if(obj.FormatSourcePlaceType=="进口"){//预售进口
                $(".blue-bg").hide();
				if(obj.FormatWHType == "仓库组")
				{//预售仓库组
					$(".mall-warehouse").parent(".row").hide()//仓库地址隐藏
					$(".mall-placename").parent(".row").hide()//仓库名称隐藏
					$(".mall-place").parent(".row").hide()//交货地隐藏
					$(".mall-phone").parent(".row").hide()//仓库联系人-电话
					}
					else if(obj.FormatWHType=="仓库地址")
					{
						$(".mall-warehouseGrop").parent(".row").hide();//仓库组隐藏
					}

				}
			}
//现在时间
		var NowTime=new Date();
			var YY=NowTime.getFullYear()
			var MM=NowTime.getMonth()+1;
			var DD=NowTime.getDate();
			var EndTime=obj.REC_CREATETIME
		EndTime = new Date(Date.parse(EndTime.replace(/-/g, "/")));
			var ThirdTime=new Date(YY, MM, DD, 24, 0, 0)
			var tm=ThirdTime-EndTime//截止到今天晚上12点到参数的毫秒数
			var ts=NowTime-EndTime//计算剩余毫秒数
		 	var D=parseInt(ts/1000/60/60/24);//计算天数
//var d=parseInt(ts/1000/60/60/24);//计算天数
		var h = parseInt(ts / 1000 / 60 / 60 % 24);//计算剩余的小时数
		var m = parseInt(ts / 1000 / 60 % 60);//计算剩余的分钟数
			var s = parseInt(ts / 1000 % 60);//计算剩余的秒数
 				if(D>0){
 					$(".col-right").html(D+"天前");
				}else{
				if(h>0){
					$(".col-right").html(h+"小时前");
				}else{
 					if(m>0){
 					$(".col-right").html(m+"分钟前");
				}else{
 					$(".col-right").html("刚刚");
			}
					}
 					}
 	if(obj.FormatSourcePlaceType=="进口"){
		$(".mall-warehouse,.mall-kind,.mall-placename").show();
	}
//页面元素赋值
	$(".cus-category").html((obj.FormatCategoryBrand))
	$(".cus-way").html(obj.FormatSourcePlaceType)	//预售or现货
	$(".cus-tag").html(obj.FormatLeadsStatus);
	$(".spots").html(obj.FormatGoodsType);
	$(".mall-pack").html(obj.FormatPackageStandard);//包装
	$(".mall-address").html(obj.SourcePlaceName);
	$(".mall-kind").html(obj.FormatPalletType);//托盘种类
	$(".mall-placename").html(obj.StoreHouseFN)//仓库名称
	$(".mall-warehouse").html(obj.WHAddress)//仓库地址
	$(".mall-forwarder").html(obj.FormatCargoAgent)//货代
	$(".mall-qualified").html(obj.FormatConformProduct)
	$(".mall-warehouseGrop").html(obj.WHGruopName)//仓库组
	$(".mall-company").html(obj.FirmShowName);
	$(".mall-quality").html(obj.Quantity+"批/"+obj.TotalWeight+"吨");
	$(".mall-have").html(obj.ResidualWeight+"吨");
	$(".mall-small").html(obj.MinQuantity+"批/"+obj.MinWeight+"吨");
	$(".mall-unit").html(obj.TradeUnitNumber+"吨/批");
	$(".mall-price").html(obj.Price+"元/吨");
	$(".mall-place").html(obj.FormatDeliveryPlace);//交货地
	$(".mall-phone").html(obj.StoreContactName);
	$(".mall-way").html(obj.FormatSettlementMethod);
	$(".mall-givedate").html(obj.FormatDeliveryEndDate)
	$(".mall-date").html((obj.FormatDeliveryStartDate)+"-"+(obj.FormatDeliveryEndDate));
	$(".mall-charge").html(obj.FormatExtraLogisticsCost+"元/吨")
	$(".mall-sure").html(obj.FormatDepositRate);
	$(".mall-smallcharge").html(obj.FormatPettyCost+"元/吨")
	$(".mall-smallrange").html("每笔交收量"+obj.FormatPettyCostRange+"吨")
	$(".mall-moderange").html(mallcontent)
        }else if(data.CODE.indexOf("10012") >= 0){//用户信息过期
            window.location.href='key?ID='+-1;
		}
	};
// 调用失败
	function errorback(){
		alert(0);
		webToast("网络请求失败","middle",1000);
	}
})(jQuery);

	
