/**
 * Created by guzhenfu on 2017/7/12.
 */

$(function(){
    $('#query').click(function () {
        toastFunction("成功调用!");
        // location.href = 'benefit.html';
    })





    $('#go').click(function () {
        var value = $('.name').val();
        alert(value);
        // location.href = 'main.html';
    })

    $('#buy').click(function () {
        location.href = '../input.html';
    })

    $("#action").click(function () {
        layer.open({
            title: [
                '购机协议',
                'margin: 0px'
            ],
            content: $('#content').html(),
            btn: '确定'
        });
    })
});