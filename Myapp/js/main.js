
var same=document.body.clientHeight;
// console.log(same)
window.onload=function () {
  //------------------------------------------------------------------成交
var param = {
	"MethodName":"GetOrderHomeList",
	"ServiceName":"Shcem.Trade.ServiceContract.IOrderService",
	"Params":"{\"PageSize\":5,\"KeyWords\":\"\",\"PageIndex\":1}"};
 jsonAjax(param,callback,errorback);
 function callback(data){
 		var detailcontent='';
		var obj = JSON.parse(data.DATA);
			$.each(obj.list,function(i,item){
		detailcontent+='<li class="row">'+'<div class="col text-center" >'+item.BrandShow+'</div>'+'<div class="col text-center">'+item.DeliveryPlace+'</div>'+'<div class="col cus-red text-center">'+item.Price+'元/吨'+'</div>'+'<div class="col cus-blue text-center">'+item.FromatCreateTime+'</div>'+'</li>';

		$(".cus-main-detail").html(detailcontent);
			})
 };
 function errorback(data){
	 webToast("网络请求失败","middle",1000);
 }
//-----------------------------------------------------------------------报盘
var param = {
 	"ServiceName":"Shcem.Trade.ServiceContract.ILeadsService",
	"MethodName":"GetLeadsList",
	"Params":"{\"KeyWords\":\"\",\"LeadsStatusList\":[],\"PageIndex\":1,\"PageSize\":5,\"OrderBy\":\"1\",\"SortDirect\":1,\"CategorySpecialIds\":[],\"SourcePlaceIds\":[],\"DeliveryPlaceIds\":[],\"GoodsType\":-1,\"SettlementMethod\":-1,\"SourcePlaceType\":-1,\"DeliveryScopeIds\":[],\"ProductID\":\"\",\"CategoryParentID\":0,\"TOP\":\"\"}"};
 jsonAjax(param,callback2,errorback2);
 function callback2(data){
	 var ordercontent="";			//设置文本
 	 var obj2 = JSON.parse(data.DATA);
		$.each(obj2,function(i,item){  //遍历
		if(item.SourcePlaceType == 0&&item.GoodsTypeShow == "预售")
		{//如果是预售进口
			if(item.WHGruopName == "")
			{
				item.DeliveryPlace=item.DeliveryPlace;
			}else{
				item.DeliveryPlace=item.WHGruopName;
			}
		}else if(item.DeliveryPlace == undefined||item.DeliveryPlace == "")
		{//地址为空的时候
			item.DeliveryPlace="-"
		}
     //遍历添加元素
		ordercontent+='<li class="row"  id="'+item.ID+'">'+'<div class="col text-center" >'+item.BrandShow+'</div>'+'<div class="col text-center">'+item.DeliveryPlace+'</div>'+'<div class="col cus-blue text-center">'+item.ResidualWeight+'吨'+'</div>'+'<div class="col cus-red text-center">'+item.Price+'元/吨'+'</div>'+'<div class="col cus-display" style="display: none;">'+item.FirmShowName+'</div>'+'<div class="col cus-timedisplay" style="display: none" >'+item.REC_CREATETIMEShow+'</div>'+'<div class="col cus-place" style="display: none" >'+item.SettlementMethodShow+'</div>'+'</li>';
})
	 //添加页面元素
	 $(".cus-main-order").html(ordercontent);
	 //页面点击跳转事件
	 $(".cus-main-order li").bind("click",function(){
		 //修改默认的点击时候状态
		 var ind=$(".cus-main-order li").index($(this));
		 // if($(this).find(".cus-place").html() == "中石化现货配送")
		 // { //中石化弹框
			//  // alert(0)
			//  window.location.href='key?ID='+-2;
		 // }else{   //页面传ID
			 var dash=escape($(this).find(".cus-display").html());//中文字符编码
			 var time=escape($(this).find(".cus-timedisplay").html());//中文字符编码
			 var ID=this.id;
			 var token=window.localStorage.getItem("token");
			 window.location.href='mall_detail.html?ID='+ID+'&Firm='+dash+'&Dt='+time+'&token='+token;//传值到下级页面
		 // }
	 });
 }
 function errorback2(data){
	 webToast("网络请求失败","middle",1000);
 }
//-----------------------------------------------------------------tab点击
$(".cus-table-tab span").click(function(){
	var ind=$(".cus-table-tab span").index($(this));
	$(".cus-table-tab span.active").removeClass("active"); 
   		$(this).addClass("active"); //点击的元素高亮
			if(ind == 0)
			{
//如果是全部则默认
			jsonAjax(param,callback2,errorback2); 
	}else if(ind == 1){
//如果是PP
			var param1 = {
				"ServiceName":"Shcem.Trade.ServiceContract.ILeadsService",
				"MethodName":"GetLeadsList",
				"Params":"{\"KeyWords\":\"\",\"LeadsStatusList\":[],\"PageIndex\":1,\"PageSize\":5,\"OrderBy\":\"1\",\"SortDirect\":1,\"CategorySpecialIds\":[18],\"SourcePlaceIds\":[],\"DeliveryPlaceIds\":[],\"GoodsType\":-1,\"SettlementMethod\":-1,\"SourcePlaceType\":-1,\"DeliveryScopeIds\":[],\"ProductID\":\"\",\"CategoryParentID\":0,\"TOP\":\"\"}"
			}
		jsonAjax(param1,callback2,errorback2);
	}else if(ind==2){
//如果是PE
	var param2 = {
		"ServiceName":"Shcem.Trade.ServiceContract.ILeadsService",
		"MethodName":"GetLeadsList",
		"Params":"{\"KeyWords\":\"\",\"LeadsStatusList\":[],\"PageIndex\":1,\"PageSize\":5,\"OrderBy\":\"1\",\"SortDirect\":1,\"CategorySpecialIds\":[37],\"SourcePlaceIds\":[],\"DeliveryPlaceIds\":[],\"GoodsType\":-1,\"SettlementMethod\":-1,\"SourcePlaceType\":-1,\"DeliveryScopeIds\":[],\"ProductID\":\"\",\"CategoryParentID\":0,\"TOP\":\"\"}"
	}
	jsonAjax(param2,callback2,errorback2); 
	}else{
		//如果是更多
		window.location.href='key?ID='+-1;
	}    
})
function removeState(){
	$(".cus-main-order li").css({"background":"#fff"})
}
}
