/**
 * Created by guzhenfu on 2017/7/12.
 */


function GetQueryString(name) {
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
}

$(function(){

    // //生成1900年-2100年
    // for(var i = 2017; i<=2030;i++){
    //     var option = document.createElement('option');
    //     option.setAttribute('value',i);
    //     option.innerHTML = i;
    //     sel1.appendChild(option);
    // }
    // //生成1月-12月
    // for(var i = 1; i <=12; i++){
    //     var option = document.createElement('option');
    //     option.setAttribute('value',i);
    //     option.innerHTML = i;
    //     sel2.appendChild(option);
    // }
    // //生成1日—31日
    // for(var i = 1; i <=31; i++){
    //     var option = document.createElement('option');
    //     option.setAttribute('value',i);
    //     option.innerHTML = i;
    //     sel3.appendChild(option);
    // }


    Tida.ready({
        sellerNick:"移动天帅通信专卖店" // 商家名称
    }, function(){

    })


    $('.number').text(GetQueryString('infor'))
    $('.name').text(sessionStorage.nameStr)
    $('.phone-input').val(GetQueryString('phone'))
    $('.preTime').text(GetQueryString('expire'))
    $('.preSpeed').text(GetQueryString('bandwidth'))
    $('.preAdd').text(sessionStorage.address)


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
        if ($('.phone-input').val().length == 0){
            toastFunction("手机号码不能为空");
            return;
        }

        if ($('.phone-input').val().length != 11){
            toastFunction("手机号码输入错误");
            return;
        }

        // if ($('.year option:selected').text() == '年' ||
        //             $('.month option:selected').text() == '月' ||
        //                 $('.day option:selected').text() == '日'){
        //     toastFunction("请选择年月日");
        //     return;
        // }


        //var speed =  $('.speed option:selected').text();
        var time = $('.time option:selected').text();


        var params = {
            tradeExToken:'',
            tradeToken: GetQueryString("tradeToken")
        };


        data.ex_property.id_number = '';
        data.ex_property.installation_address = $('.preAdd').text();
        data.ex_property.mobile = $('.phone-input').val();
        data.ex_property.name = $('.name').text();
        data.ex_property.broadband_rate = $('.preSpeed').text();
        data.ex_property.contract_period = $('.time option:selected').text();
        // data.ex_property.effective_date = $('.year option:selected').text()+"年"+$('.month option:selected').text()+"月"+$('.day option:selected').text()+"号零点";
        data.ex_property.effective_date = "";

        //Tida.showLoading("加载中...");

        $.ajax({
            type: 'POST',
            url: 'https://ydts.ews.m.jaeapp.com/php/trade.php',
            data: data,
            dataType: 'json',
            success: function (data) {

                params.tradeExToken = data.trade_extend_token;
                //alert(JSON.stringify(params));
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
                    //alert(JSON.stringify(params))
                    Tida.customization.buildOrder(params);
                    //Tida.hideLoading();
                });


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