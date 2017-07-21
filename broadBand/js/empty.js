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
        $('.info').text(GetQueryString('info'))
        $('.name').text(GetQueryString('name'))
        $('.time').text(GetQueryString('time'))
        $('.speed').text(GetQueryString('speed'))
        $('.price').text(GetQueryString('price'))
    }

});