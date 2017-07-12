//获取URL
var url = window.location.href; //获取url中"?"符后的字串
var item;

if (url.indexOf("?") != -1)
{
	var str = url.split("?");
	strs = str[1];
	var theIndex = strs.substr(4,1);
	var theProduct = strs.substr(10,1);
}
// console.log("点击了第"+theIndex);
// console.log("产品是"+theProduct);
RequestInfoPriceDetaiData();
var id=document.getElementById("chart_detail");
var booel=true;
//切换
$(".chart-time span").click(function(){
	$(".chart-time span").removeClass("active");
	$(this).addClass("active");
	var ind=$(".chart-time span").index($(this));
	// console.log(ind);
	if(ind == 0)
	{
		getMonthDateFromNow(30);
		RequestKChartDeatilData(item, startDate);

	} else if(ind == 1) {
		getMonthDateFromNow(60);
		RequestKChartDeatilData(item, startDate);

	} else if(ind == 2) {
		getLastYearYestdy();
		RequestKChartDeatilData(item, startDateYears);

	}
});


function RequestInfoPriceDetaiData() {
	var paramPriceDetail = {
		"MethodName":"getScemQuo",
		"ServiceName":"Shcem.Inform.ServiceContract.IQueryInfoService",
		"Params":"{\"ProductID\":"+theProduct+",\"TOP\":8}"
	};
	jsonAjax(paramPriceDetail, callbackPrice, errorbackPrice);
	function callbackPrice(data) {
		var obj = JSON.parse(data.DATA);
		item = obj[theIndex];

		getMonthDateFromNow(30);
		RequestKChartDeatilData(item, startDate);

		var detailKChart = '';
		detailKChart += '<h3 class="cus-red ">'+item.SQRank+' - '+item.SQNo+' / '+item.SQType+'</h3>';
		$(".chart-title").html(detailKChart);
	}
	function errorbackPrice(data) {
		webToast("网络请求失败","middle",1000);
	}

}

function RequestKChartDeatilData(theItem, startTime) {
	var paramKChartDetail = {
		"MethodName": "getScemQuoDiagram",
		"ServiceName": "Shcem.Inform.ServiceContract.IQueryInfoService",
		"Params": JSON.stringify({
			'EndDate': theItem.SQDate,
			'StartDate': startTime,
			'Rank': theItem.SQRank,
			'Type': theItem.SQType,
			'ProductID': theProduct,
			'ProductName': "",
			'NO': theItem.SQNo
		})
	};
	jsonAjax(paramKChartDetail, callBackKChart, errorBackKChart);
	function callBackKChart(data) {
		var obj = JSON.parse(data.DATA);
		minKChartData = obj.ScemQuoPriceList[0];
		for (var j = 1; j < obj.ScemQuoPriceList.length; j++) {
			if (obj.ScemQuoPriceList[j] < minKChartData) {
				minKChartData = obj.ScemQuoPriceList[j];
			}
		}
		commonCharts(id, booel, obj.ScemQuoDateList, obj.ScemQuoPriceList);
	}

	function errorBackKChart(data) {
		webToast("网络请求失败","middle",1000);
	}
}

var startDateYears;
//获取当前时间的前一年时间
function getLastYearYestdy(){
	var myDate = new Date();
	var strYear = myDate.getFullYear() - 1;
	var strDay = myDate.getDate();
	var strMonth = myDate.getMonth()+1;
	if(strMonth<10)
	{
		strMonth="0"+strMonth;
	}
	if(strDay<10)
	{
		strDay="0"+strDay;
	}
	datastr = strYear+"-"+strMonth+"-"+strDay;
	startDateYears = datastr;
	return datastr;
}