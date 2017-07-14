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
});