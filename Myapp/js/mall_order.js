//获取URL
function GetQueryString(name)
{
	var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if(r!=null)return  unescape(r[2]); return null;
}
var LeadesID=GetQueryString("ID")
var Orderparam = {
	"MethodName":"GetLeadsDetailInfo",
	"ServiceName":"Shcem.Trade.ServiceContract.ILeadsService",
	"Params":"{\"LeadsID\":"+LeadesID+"}"
};
jsonAjax(Orderparam,Ordercallback,Ordererrorback);
function Ordercallback(data) {
	var obj=JSON.parse(data.DATA);
	// console.log(obj);
	if(obj.FormatSourcePlaceType == "国产"){
		$(".yellow-bg").hide();
	}
	else{
		$(".blue-bg").hide();
	}
	$(".cus-category").html((obj.FormatCategoryBrand));
	$(".cus-way").html(obj.FormatSourcePlaceType)//国产or进口
	$(".cus-tag").html(obj.FormatGoodsType);
	$(".mall-quality").html(obj.Quantity+"批"+"/"+obj.TotalAvailableWeight+"吨");
	$(".mall-small").html(obj.MinQuantity+"批"+"/"+obj.MinWeight+"吨");
	$(".mall-unit").html(obj.TradeUnitNumber+"吨/批");
	$(".mall-price").html(obj.Price+"元/吨");
}
function Ordererrorback(data) {
	webToast("网络请求失败","middle",1000);
}
