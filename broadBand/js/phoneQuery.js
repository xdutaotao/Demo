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


    var params = {
        name: '',
        phone: '',
        personal: ''
    }
    $('#query').click(function () {
        var name = $('.name').val();
        var phone = $('.phone').val();
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

        if (personal == ''){
            toastFunction("身份证号不能为空");
            return;
        }

        if (personal.length != 18){
            toastFunction("身份证号输入错误");
            return;
        }

        params.name = name;
        params.phone = phone;
        params.personal = personal;

        $.ajax({
            type: 'POST',
            url: 'https://ydts.ews.m.jaeapp.com/manage/index.php?g=Portal&m=Index&a=getBroadband',
            data: params,
            dataType: 'json',
            success: function (data) {

                //Tida.hideLoading();
                if (data.code == 0){
                    layer.open({
                        title: ['查询结果','margin: 0px'],
                        btn: '确定',
                        content: '没有查询到相关信息',
                        yes: function () {
                            layer.close( );
                        }
                    });
                }else{

                    sessionStorage.phoneName=name;

                    location.href = 'benefit.html?'+link+"&phone="+phone+"&personal="+personal;
                }
            },
            error:function(msg) {
                //Tida.hideLoading();
                toastFunction("请稍后重试")
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

    $('.query_rule').click(function () {
        //alert('1')
        location.href = 'rule.html'
    })

});