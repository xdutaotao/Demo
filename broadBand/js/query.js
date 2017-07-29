/**
 * Created by guzhenfu on 2017/7/12.
 */


function GetQueryString(name) {
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
}
// var code = GetQueryString("code");
// var error = GetQueryString("error");
// if (!code && (error == undefined || error == null)){
//     location.href = 'https://oauth.taobao.com/authorize?response_type=code&client_id=24545499&redirect_uri=http://ydts.ews.m.jaeapp.com/broadBand/query.html&state=1212&view=wap'
// }

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

    var params = {
        name: '',
        phone: '',
        infor: '',
    }


    $('#query').click(function () {
        var name = $('.name').val();
        var phone = $('.phone').val();
        var infor = $('.infor').val();
        //var personal = $('.personal').val();

        if (name == ''){
            //Tida.toast("姓名不能为空")
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

        /*  if (personal == ''){
         toastFunction("身份证号不能为空");
         return;
         }*/

        /*  if (personal.length != 18){
         toastFunction("身份证号输入错误");
         return;
         }*/

        // location.href = 'main.html?'+link;
        //location.href = 'empty.html?info='+infor+'&name='+name+'&phone='+phone;
        // Tida.doAuth(function(data){
        //     if(data.finish){
        //         // 授权成功 可以顺利调用需要授权的接口了
        //     }else {
        //         // 未能成功授权
        //     }
        // }); https://oauth.taobao.com/authorize?response_type=code&client_id=24545499&redirect_uri=http://ydts.ews.m.jaeapp.com/broadBand/empty.html

        params.name = name;
        params.infor = infor;
        params.phone = phone;
        //params.personal = personal;


        //Tida.showLoading("加载中...");


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
                        btn: ['重新办理','重新输入'],
                        content: '没有查询到相关信息',
                        yes: function () {
                            layer.close( );
                            location.href = 'input.html?'+link;
                        }
                    });
                }else{

                    sessionStorage.nameStr=name;
                    sessionStorage.address = data.data.address;
                    location.href = 'main.html?'+link+'&infor='+infor+'&phone='+phone+"&expire="+
                        data.data.expire+'&bandwidth='+data.data.expenses.split("有线宽带")[1].split("包年")[0];
                }
            },
            error:function(msg) {
                //Tida.hideLoading();
                toastFunction("请稍后重试")
            }

        });

        //location.href = 'https://oauth.taobao.com/authorize?response_type=code&client_id=24545499&redirect_uri=http://ydts.ews.m.jaeapp.com/broadBand/query.html&state=1212&view=web'

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