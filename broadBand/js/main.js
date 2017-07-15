/**
 * Created by guzhenfu on 2017/7/12.
 */

$(function(){

    window.onload = function () {
        Tida.ready({
            sellerNick:"移动天帅通讯专卖店" // 商家名称
        }, function(){

        })
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
            mixUserId: "AAHrb-fwAAgOrLpFIt9ATdjc",
            itemId: "100889988",
            totalPrice: "1024.55",
            expiryDate: "2016-07-29 12:12:12",
            pluginInstanceId: '60',
            merchantOrderId: '23678698',
        }

    };

    $('#buy').click(function () {
        $.ajax({
            type: 'POST',
            url: 'http://ydts.ews.m.jaeapp.com/php/trade.php',
            data: data,
            success: function (data) {
                console.log(data);
                if (data.code == '15'){
                    console.log(data.msg);
                }
            },
            dataType: 'json'
        });
    })

});