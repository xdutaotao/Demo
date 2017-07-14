/**
 * Created by guzhenfu on 2017/7/12.
 */

$(function(){
    $('#query').click(function () {
        toastFunction("成功调用!");
        // location.href = 'queryTwo.html';
    })





    $('#go').click(function () {
        var value = $('.name').val();
        alert(value);
        // location.href = 'main.html';
    })

    $('#buy').click(function () {
        location.href = 'three.html';
    })

    $("#action").click(function () {
        layer.open({
            title: [
                '购机协议',
            ],
            content: '同时存在同一页面移动版和PC版不能同时存在同一页面移动版和PC版不能同时存在同一页面移动版' +
            '和PC版不能同时存在同一页面移动版和PC版不能同时存在同一页面移动版和PC版不能同时存在同一页面移动版和',
            btn: '确定'
        });
    })
});