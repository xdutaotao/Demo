/**
 * Created by guzhenfu on 2017/7/12.
 */

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

        location.href = 'benefitPage.html?'+link;

    })

    $('#query_rule').click(function () {
        location.href = 'rule.html';
    })

});