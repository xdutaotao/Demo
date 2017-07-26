/**
 * Created by guzhenfu on 2017/7/12.
 */


function GetQueryString(name)
{
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
}


$(function(){

    var name = getUrlParame('name');
    var order = getUrlParame('order');
    var phone = getUrlParame('phone');

    var flag = true;
    if (flag){
        $('#no_benefit').css('display', 'none');
    }else{
        $('#benefit_price').css('display', 'none');
        $('#benefit_money').css('display', 'none');
    }

    var data = {
        ex_property :{
            broadband_account: "18768144976",
            broadband_rate: "100M",
            contract_period: "2年",
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
            totalPrice: "1024.55",
            expiryDate: "2017-07-29 12:12:12",
            pluginInstanceId: GetQueryString("instance_id"),
            merchantOrderId: '23678698',
        }

    };



    $('#action').click(function () {



        var params = {
            tradeExToken:'',
            tradeToken: GetQueryString("tradeToken")
        };


        //$("#agreement").append(JSON.stringify(data));


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





        // $.ajax({
        //     type: 'POST',
        //     url: 'http://ydts.ews.m.jaeapp.com/php/trade.php',
        //     data: data,
        //     success: function (data) {
        //         console.log(data);
        //         if (data.code == '15'){
        //             console.log(data.msg);
        //         }
        //     },
        //     dataType: 'json'
        // });

    })


    $("#agreement").click(function () {
        layer.open({
            title: [
                '购机协议',
                'margin: 0px'
            ],
            content: $('#content').html(),
            btn: '确定'
        });
    })

    function getUrlParame(name) {
        var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if(r!=null)return  unescape(r[2]); return null;
    }
});