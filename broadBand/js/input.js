/**
 * Created by guzhenfu on 2017/7/12.
 */


function GetQueryString(name) {
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
}

$(function(){

    window.onload = function () {
        Tida.ready({
            sellerNick:"移动天帅通信专卖店" // 商家名称
        }, function(){

        })
    }

    var speedList = ['1', '2', '3', '4']
    $('.speed').change(function () {
        $('#money').text(speedList[$('.speed option:selected').val()]);
        data.ex_property.broadband_rate = $('.speed option:selected').text();
    });

    $('.time').change(function () {
        data.ex_property.contract_period = $('.time option:selected').text();
    });



    var nowDate = new Date();
    var data = {
        ex_property :{
            broadband_rate: "50M",
            contract_period: "1年",
            effective_date: nowDate.getFullYear()+"年"+nowDate.getMonth()+"月"+nowDate.getDay()+"号零点",
            id_number: $('.personal-input').val(),
            installation_address: $('.address-input').val(),
            mobile: $('.phone-input').val(),
            name: $('.name-input').val(),
            operators: "移动天帅通信专卖店",
            package_details: "安装宽带",
            package_name: "宽带新装",
            package_price: '1',
            preferential_info: "宽带优惠",
            province: "浙江",
            service_type: "broadband"
        },
        detail: {
            mixUserId: GetQueryString("mixUserId"),
            itemId: GetQueryString("itemId"),
            totalPrice: "1",
            pluginInstanceId: GetQueryString("instance_id"),
            merchantOrderId: (new Date()).getTime(),
        }

    };


    $('#action').click(function () {
        if ($('.name-input').val() == ''){
            toastFunction("户主姓名不能为空");
            return;
        }

        if ($('.phone-input').val() == ''){
            toastFunction("电话不能为空");
            return;
        }

        if ($('.address-input').val() == ''){
            toastFunction("地址不能为空");
            return;
        }

        if ($('.personal-input').val() == ''){
            toastFunction("身份证号不能为空");
            return;
        }

        if ($('.contact-name-input').val() == ''){
            toastFunction("联系人不能为空");
            return;
        }

        if ($('.contact-phone-input').val() == ''){
            toastFunction("联系方式不能为空");
            return;
        }


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

                params.tradeExToken = data.trade_extend_token;

                Tida.ready({
                    //share 为分享模块，如果需要分享功能，需要写上
                    //customization 为定制模块，必填
                    //device 为设备模块，如果需要手机拍照功能，需要填上
                    //提示，请按需加载，加快页面载入速度。
                    //console:1,
                    module:["customization", 'im'], // 需要的模块
                    debug: 0,
                    combo: 0
                }, function(e){
                    Tida.customization.buildOrder(params);
                });

            },
            error:function(msg) {
                //$("#agreement").append(JSON.stringify(msg));
                //console.log(JSON.stringify(msg));
            }

        });

    })
    
    $('.im').click(function () {
        var options = {
            sellerNick:"移动天帅通信专卖店",
            itemId :GetQueryString("itemId"),
            shopId : GetQueryString("shopId"),
            orderId :""
        };

        Tida.wangwang(options,function(data){

        });
    })

});