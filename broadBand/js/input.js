/**
 * Created by guzhenfu on 2017/7/12.
 */


function GetQueryString(name)
{
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
}

Date.prototype.Format = function (fmt) { //author: meizz
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

$(function(){
    var speedList = ['1', '2', '3', '4']
   $('.speed').change(function () {
       $('#money').text(speedList[$('.speed option:selected').val()]);
       data.ex_property.broadband_rate = $('.speed option:selected').text();
   });

    $('.time').change(function () {
        data.ex_property.contract_period = $('.time option:selected').text();
    });

    var data = {
        ex_property :{
            broadband_account: "18768144976",
            broadband_rate: "50M",
            contract_period: "1年",
            effective_date: "2017年11月11号零点",
            id_number: "430221199107154119",
            installation_address: "浙江杭州xxxxx",
            mobile: "18768144976",
            monthly_fee: "78.89",
            name: "涂耀",
            operators: "运营商名称",
            package_details: "套餐详情描述",
            package_name: "套餐名称",
            package_price: "89.89",
            preferential_info: "9.5折，送200元话费",
            province: "浙江",
            service_type: "broadband",
            source: "detail"
        },
        detail: {
            mixUserId: GetQueryString("mixUserId"),
            itemId: GetQueryString("itemId"),
            totalPrice: "1",
            expiryDate: new Date().Format("yyyy-MM-dd HH:mm:ss"),
            pluginInstanceId: GetQueryString("instance_id"),
            merchantOrderId: '23678698',
        }

    };


    $('#action').click(function () {
        // if ($('.name-input').val() == ''){
        //     toastFunction("户主姓名不能为空");
        //     return;
        // }
        //
        // if ($('.phone-input').val() == ''){
        //     toastFunction("电话不能为空");
        //     return;
        // }
        //
        // if ($('.address-input').val() == ''){
        //     toastFunction("地址不能为空");
        //     return;
        // }
        //
        // if ($('.personal-input').val() == ''){
        //     toastFunction("身份证号不能为空");
        //     return;
        // }

        var speed =  $('.speed option:selected').text();
        var time = $('.time option:selected').text();


        var params = {
            tradeExToken:'',
            tradeToken: GetQueryString("tradeToken")
        };

        $.ajax({
            type: 'POST',
            url: 'https://ydts.ews.m.jaeapp.com/php/trade.php',
            data: data,
            dataType: 'json',
            success: function (data) {
                //console.log(JSON.stringify(data));

                //$("#agreement").append("88888888888888888");

                if (data.code == '15'){
                    //console.log(data.msg);
                }

                params.tradeExToken = data.trade_extend_token;

                Tida.ready({
                    //share 为分享模块，如果需要分享功能，需要写上
                    //customization 为定制模块，必填
                    //device 为设备模块，如果需要手机拍照功能，需要填上
                    //提示，请按需加载，加快页面载入速度。
                    //console:1,
                    module:["customization"], // 需要的模块
                    debug: 0,
                    combo: 0
                }, function(e){
                    Tida.customization.buildOrder(params);
                });

            },
            error:function(msg)
            {
                //$("#agreement").append(JSON.stringify(msg));
                //console.log(JSON.stringify(msg));
            }

        });

    })

});