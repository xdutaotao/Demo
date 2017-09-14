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


    var isDisplay = 1;
    $("#selectAction").click(function () {

        if (isDisplay == 0){
            $("#select").css('visibility','hidden')
            isDisplay = 1;
        }else{
            $("#select").css('visibility','visible');
            isDisplay = 0;
        }

    })


    $('#action').click(function () {
        if ($('.phone-input').val() == ''){
            toastFunction("请输入手机号");
            return;
        }

        if ($('.phone-input').val().length != 11){
            toastFunction("手机号码输入错误");
            return;
        }

        if (isDisplay == 1){
            toastFunction("请选择套餐");
            return;
        }

        toastFunction("查询中...");

        var data = {
            phone: $('.phone-input').val()
        };

        $.ajax({
            type: 'POST',
            url: 'https://ydts.ews.m.jaeapp.com/manage/index.php?g=Portal&m=Index&a=searchPhone',
            data: data,
            dataType: 'json',
            success: function (data) {
                alert(data)

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
                    //alert(JSON.stringify(params));
                    Tida.customization.buildOrder(params);
                    //Tida.hideLoading();
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