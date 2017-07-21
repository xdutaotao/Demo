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

    var HEIGHT = $('.main-div').height();
    $(window).resize(function() {
        $('.main-div').height(HEIGHT);
    });


    var nowDate = new Date();
    nowDate.setTime(nowDate.getTime() + 24*60*60*1000);
    var data = {
        ex_property :{
            broadband_rate: "50M",
            contract_period: "1年",
            effective_date: '',
            id_number: '',
            installation_address: '',
            mobile: '',
            name: '',
            operators: "移动天帅通信专卖店",
            package_details: "宽带续约",
            package_name: "宽带续约",
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
            merchantOrderId: (new Date()).getTime()
        }

    };


    $('#buy').click(function () {
        if ($('.number').val() == ''){
            toastFunction("宽带账号不能为空");
            return;
        }

        if ($('.name').val() == ''){
            toastFunction("名字不能为空");
            return;
        }

        if ($('.phone-input').val().length != 11){
            toastFunction("手机号码输入错误");
            return;
        }

        // if ($('.personal-input').val() == ''){
        //     toastFunction("身份证号不能为空");
        //     return;
        // }
        //
        // if ($('.personal-input').val().length != 18){
        //     toastFunction("身份证号输入错误");
        //     return;
        // }
        //
        // if ($('.address-input').val() == ''){
        //     toastFunction("地址不能为空");
        //     return;
        // }


        var speed =  $('.speed option:selected').text();
        var time = $('.time option:selected').text();


        var params = {
            tradeExToken:'',
            tradeToken: GetQueryString("tradeToken")
        };

        data.ex_property.id_number = $('.personal-input').val();
        data.ex_property.installation_address = $('.address-input').val();
        data.ex_property.mobile = $('.phone-input').val();
        data.ex_property.name = $('.name').val();
        data.ex_property.broadband_rate = $('.speed option:selected').text();
        data.ex_property.contract_period = $('.time option:selected').text();
        data.ex_property.effective_date = nowDate.getFullYear()+"年"+(nowDate.getMonth()+1)+"月"+nowDate.getDate()+"号零点";

        //Tida.showLoading("加载中...");

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
                    //Tida.hideLoading();
                });

            }

        });
    })

});