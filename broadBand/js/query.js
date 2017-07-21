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


    $('#query').click(function () {
        var name = $('.name').val();
        var phone = $('.phone').val();
        var infor = $('.infor').val();
        var personal = $('.personal').val();

        if (name == ''){
            toastFunction("姓名不能为空");
            return;
        }

        if (phone == ''){
            toastFunction("手机号码不能为空");
            return;
        }

        if (phone.length != 11){
            toastFunction("手机号码输入错误");
            return;
        }

        if (infor == ''){
            toastFunction("宽带信息不能为空");
            return;
        }

        if (personal == ''){
            toastFunction("身份证号不能为空");
            return;
        }

        if (personal.length != 18){
            toastFunction("身份证号输入错误");
            return;
        }

        // location.href = 'main.html?'+link;
        location.href = 'empty.html?info='+infor+'&name='+name+'&phone='+phone;

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