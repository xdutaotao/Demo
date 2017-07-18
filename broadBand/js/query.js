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
        var name = $('.queryName').val();
        var order = $('.queryOrder').val();
        var phone = $('.queryPhone').val();

        if (name == ''){
            toastFunction("机主姓名不能为空");
            return;
        }

        if (order == ''){
            toastFunction("身份证不能为空");
            return;
        }

        if (phone == ''){
            toastFunction("手机号码不能为空");
            return;
        }

        location.href = 'benefitPage.html?'+link;

    })

    $('#query_rule').click(function () {
        location.href = 'rule.html';
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