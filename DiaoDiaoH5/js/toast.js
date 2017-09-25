/**
 * Created by GUZHENFU on 2017/7/14.
 */

/**
 * 模仿android里面的Toast效果，主要是用于在不打断程序正常执行的情况下显示提示数据
 * @param config
 * @return
 */
var Toast = function(config){
    this.context = config.context==null?$('body'):config.context;//上下文
    this.message = config.message;//显示内容
    this.time = config.time==null?2000:config.time;//持续时间
    this.left = config.left;//距容器左边的距离
    this.top = config.top;//距容器上方的距离
    this.init();
}
var msgEntity;
Toast.prototype = {
    //初始化显示的位置内容等
    init : function(){
        $("#toastMessage").remove();
        //设置消息体
        var msgDIV = new Array();
        msgDIV.push('<div id="toastMessage">');
        msgDIV.push('<span>'+this.message+'</span>');
        msgDIV.push('</div>');
        msgEntity = $(msgDIV.join('')).appendTo(this.context);
        //设置消息样式
        var toastMessageWidth = $('#toastMessage').innerWidth();
        var toastMessageHeight = $('#toastMessage').innerHeight();
        var windowWidth = $(window).width();
        var windowHeight = $(window).height();
        var newWidth = (windowWidth - toastMessageWidth - 80) / 400 + "rem";
        var newHeight = (windowHeight - toastMessageHeight - 80) / 400 + "rem";
        msgEntity.css({
            'left':'0.52rem',
            'z-index':'999999',
            'top':'3.1rem',
            'background-color':'black',
            'color':'white',
            'padding':'0.13rem',
            'font-size':'0.14rem',
            'alpha': '0.8'
        });
    },
    //显示动画
    show :function(){
        msgEntity.fadeIn(this.time/2);
        msgEntity.fadeOut(this.time/2);
    }
}
function toastFunction(messageString){
    new Toast({context:$('body'),message:messageString}).show();
}