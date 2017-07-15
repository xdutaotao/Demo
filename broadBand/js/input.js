/**
 * Created by guzhenfu on 2017/7/12.
 */

$(function(){
    $('#query').click(function () {
        if ($('.name-input').val() == ''){
            toastFunction("户主姓名不能为空");
            return;
        }

        if ($('.phone-input').val() == ''){
            toastFunction("电话不能为空");
            return;
        }

        if ($('.internet-input').val() == ''){
            toastFunction("宽带信息不能为空");
            return;
        }

        if ($('.personal-input').val() == ''){
            toastFunction("身份证号不能为空");
            return;
        }

        alert('111')
    })



});